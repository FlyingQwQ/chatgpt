<template>
  <div class="home">
    <el-container style="border: 1px solid #eee; flex: 1; height: 100%; border-radius: 10px; overflow: hidden;">
      <el-aside width="250px" style="background-color: #F6F8FC">
        <div class="createArea">
          <el-select v-model="currSelectModel" placeholder="选择GPT模型">
            <el-option v-for="model in modelList" :key="model.label" :label="model.label" :value="model.value"></el-option>
          </el-select>
          <el-button @click="createDialogue">新建对话</el-button>
        </div>
        <History ref="history" @choose="historyChoose"></History>
      </el-aside>

      <el-container style="overflow: hidden; position: relative;">
        <!-- 没选择对话出现遮罩 -->
        <div class="mask" v-if="currDialogueId==''">
          <el-empty description="请在侧边栏新建选择对话"></el-empty>
        </div>

        <el-main ref="messageMain">
          <MessageWindow ref="messageWindow" @msgLoadComplete="msgLoadComplete"></MessageWindow>
        </el-main>

        <el-footer height="120px" class="inputArea">
          <div style="width: 100%;">
            <el-row :gutter="20">
              <el-col :span="22">
                <el-input
                  type="textarea"
                  placeholder="请输入内容"
                  v-model="msgContent">
                </el-input>
              </el-col>
              <el-col :span="2">
                <el-button type="primary" plain style="width: 100%;" @click="sendMsg" :loading="sendLoading">发送</el-button>
              </el-col>
            </el-row>
          </div>
        </el-footer>
      </el-container>

    </el-container>

  </div>
</template>

<script>
import { fetch } from '@/config';
import { host } from "@/config/platform"

export default {
  data() {
    return {
      modelList: [
        {
          label: 'gpt-3.5-turbo 🚀',
          value: 'gpt-3.5-turbo'
        },
        {
          label: 'gpt-3.5-turbo-0613',
          value: 'gpt-3.5-turbo-0613'
        },
        {
          label: 'gpt-3.5-turbo-16k',
          value: 'gpt-3.5-turbo-16k'
        },
        {
          label: 'gpt-4',
          value: 'gpt-4'
        },
        {
          label: 'gpt-4-32k',
          value: 'gpt-4-32k'
        },
      ],

      eventSource: null,
      currDialogueId: '', //当前对话ID
      currSelectModel: 'gpt-3.5-turbo',  //当前选择的模型
      msgContent: '',
      sendLoading: false, // 发送信息后按钮显示loading

      currGPTMessageItem: null, // 当前GPT正在对话的气泡
      gptMessageSplitJoint: '', // GPT返回的对话内容拼接
    }
  },
  mounted() {
    
  },
  methods: {
    async createDialogue() {  // 新建对话
      await this.closeSSE(this.currDialogueId);
      let config = this.$refs.history.createRecord('新建的对话', this.currSelectModel);
      this.currDialogueId = config.id;
      this.connectSSE(config);
    },
    async historyChoose(config) { // 从历史中选择对话
      if(!config) {
        this.currDialogueId = '';
      }
      await this.closeSSE(this.currDialogueId);
      this.currDialogueId = config.id;
      this.connectSSE(config);
    },


    connectSSE(config) {
      this.eventSource = new EventSource(host + "/chatgpt/connect?id=" + config.id + '&model=' + config.model);
      this.eventSource.onmessage = this.onMessage;
    },
    async closeSSE(closeId) {
      if(closeId === '') {
        return;
      }
      return new Promise((resolve) => {
        fetch.close({ id: closeId }).then(res => { // 关闭当前对话
          if(res.code == 1) {
            this.eventSource.close();
            resolve(true);
          }
        })
      });
    },

    async sendMsg() { // 发送对话信息
      if(this.msgContent === '') {
        return;
      }

      // 创建新的对话气泡
      await this.$refs.messageWindow.createMessageItem('user', this.msgContent);   
      this.currGPTMessageItem = await this.$refs.messageWindow.createMessageItem('system', '');
      this.currGPTMessageItem.setLoading(true);
      
      this.$refs.history.setCurrDialogueTitle(this.msgContent);
      
      this.sendLoading = true;
      fetch.sendmsg({
        id: this.currDialogueId,
        content: this.msgContent,
      }).then(res => {
        if(res.code == 1) {
          this.msgContent = '';
        }
      }).finally(() =>{
        this.sendLoading = false;
      });
    },

    msgLoadComplete() { // 当前对话的聊天历史加载完成事件
      let dom = this.$refs.messageMain.$el;
      dom.scrollTop = dom.scrollHeight;
    },

    onMessage(event) {  // 接收GPT对话信息
      let msgContent = JSON.parse(event.data).content;

      if(msgContent == '[start]') {  // 接收到服务端传来的初始化指令
        this.$refs.messageWindow.getMessageItems(this.currDialogueId);
        return;
      } else if(msgContent == '[over]') {  // 对话完成
        this.currGPTMessageItem.setLoading(false);
        return;
      }

      this.currGPTMessageItem.setContent(msgContent);
      if(JSON.parse(event.data).type == 'error') {  // 如果是错误信息，则通过修改Role来显示错误气泡
        this.currGPTMessageItem.setRole('error');
        this.currGPTMessageItem.setLoading(false);
      }
      
      let dom = this.$refs.messageMain.$el;
      dom.scrollTop = dom.scrollHeight;
    }
  }
}
</script>

<style scoped>
  .home {
    height: 100vh;
    padding: 20px;
    box-sizing: border-box;
  
    display: flex;
    flex-direction: column;
  }

  .inputArea {
    display: flex;
    width: 100%;
    align-items: center;
    border-top: 1px solid #EBEEF5;
  }
  
  .createArea {
    display: flex;
    flex-direction: column;
    padding: 10px 10px 20px 10px;
    box-sizing: border-box;
    border-bottom: 1px solid #d8dde7;
  }
  .createArea > .el-button {
    margin-top: 5px;
  }

  .mask {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.419);
    z-index: 999;

    display: flex;
    align-items: center;
    justify-content: center;
  }

  /deep/ textarea {
    max-height: 90px !important;
    height: 90px;
  }
  /deep/ .el-empty__description > p {
    color: rgb(245, 245, 245) !important;
  }
</style>