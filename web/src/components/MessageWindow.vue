<template>
  <div class="msgWindow" v-loading="loading">
    <MessageItem ref="messageItem" 
      v-for="(item, index) in dialogueList" 
      :key="index" 
      :index="index" 
      :role="item.role" 
      :content="item.content"
      @onContentChange="onContentChange"></MessageItem>
  </div>
</template>

<script>
import { fetch } from '@/config';
export default {
  props: {
    
  },
  data() {
    return {
      dialogueList: [
        // { role: 'user', content: '你好'  }
      ],
      loading: false,
    }
  },
  methods: {
    async createMessageItem(role, content) {  //新建聊天气泡
      return new Promise((resolve) => {
        this.dialogueList.push({ role, content });

        this.$nextTick(() => {
          let messageItems = this.$refs.messageItem;
          resolve(messageItems[messageItems.length - 1]);
        });
      });
    },
    onContentChange(index, content) {
      this.dialogueList[index].content = content;
    },
    getMessageItems(id) {
      this.loading = true;
      fetch.msgitems({ id }).then(res => {
        if(res.code == 1) {
          this.dialogueList = res.data;
        }
      }).finally(() =>{
        this.loading = false;
        this.$emit('msgLoadComplete');  //聊天历史加载完成回调
      });
    },
  }
}
</script>

<style scoped>
  .msgWindow {
    width: 100%;
    border-radius: 5px;
    padding: 20px;
    box-sizing: border-box;
    color: #333333;
  }
</style>