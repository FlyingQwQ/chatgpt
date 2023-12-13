<template>
  <div class="history">
    <div class="item" 
      v-for="item in historyList" 
      :key="item.id" 
      @click="choose(item.id)"
      :class="{ activity: currDialogueId == item.id }">

      <el-row :gutter="20">
        <el-col :span="19" class="title">{{ item.title }}</el-col>
        <el-col :span="5" class="icon" v-show="currDialogueId == item.id"><i @click.stop="remove(item.id)" class="el-icon-close"></i></el-col>
      </el-row>

      <el-tag effect="plain" type="info">{{ item.model }}</el-tag>
    </div>
  </div>
</template>

<script>
import { fetch } from '@/config';
export default {
  props: {
    
  },
  data() {
    return {
      historyList: [
        
      ],
      currDialogueId: ''
    }
  },
  mounted() {
    this.getLocalToList();
  },
  methods: {
    createRecord(title, model) {  // 创建记录
      let randomStr = this.generateRandomString(5);
      let config = {title, id: randomStr, model};
      this.historyList.push(config);
      this.currDialogueId = randomStr;

      this.saveToLocal();

      return config;
    },
    choose(id) {
      if(this.currDialogueId === id) {
        return;
      }
      this.currDialogueId = id;
      this.$emit('choose', this.historyList.find(item => item.id == id));
    },

    remove(id) {  // 删除记录
      fetch.remove({ id }).then(res => {
        if(res.code == 1) {
          this.historyList = this.historyList.filter(item => item.id != id);
          this.choose(this.historyList.length > 0 ? this.historyList[0].id : '');
          this.saveToLocal();
        }
      });
    },

    saveToLocal() { // 将记录保存到本地缓存
      localStorage.setItem('historyList', JSON.stringify(this.historyList));
    },
    getLocalToList() {  // 将本地缓存写入到List
      this.historyList = JSON.parse(localStorage.getItem('historyList')) ?? [];
    },

    setCurrDialogueTitle(title) {
      this.historyList.forEach(item => {
        if (item.id == this.currDialogueId) {
          item.title = title;
        }
      });
      this.saveToLocal();
    },

    generateRandomString(length) {
      let result = '';
      let characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      let charactersLength = characters.length;
      for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
      }
      return result;
    }
  }
}
</script>

<style scoped>
  .history {
    padding: 16px;
    box-sizing: border-box;
    user-select: none;
  }
  .item {
    border-radius: 8px;
    width: 100%;
    background-color: white;
    margin-top: 16px;
    color: #303133;
    font-size: 14px;
    padding: 12px;
    box-sizing: border-box;
    border: 1px solid #EBEEF5;
    cursor: pointer;
    transition: all 0.3s;
    
  }

  .title {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .icon {
    text-align: center;
  }
  .icon:hover {
    color: #18afd7;
  }

  .el-tag {
    margin-top: 10px;
  }

  .activity {
    border: 1px solid #8faae9;
  }
</style>