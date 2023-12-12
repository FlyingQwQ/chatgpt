<template>
  <div style="display: flex;">
    <el-avatar class="avatar" size="medium" :src="role === 'user' ? userImg : robotImg"></el-avatar>
    <div :class="{ msgItem: true, me: role === 'user', chatGpt: role === 'system' }">
      <div class="content" v-highlight v-html="mdContent"></div>
    </div>
  </div>
</template>

<script>
import MarkdownIt from "markdown-it";

export default {
  props: {
    index: {
      type: Number,
      required: true
    },
    role: {
      type: String,
      required: true
    },
    content: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      markdown: new MarkdownIt(),
      userImg: require('@/assets/imgs/user.png'),
      robotImg: require('@/assets/imgs/robot.png'),
    }
  },
  methods: {
    setContent(content) {
      this.$emit('onContentChange', this.index, content);
    }
  },
  computed: {
    mdContent() {
      // hljs.highlightAll();
      return this.markdown.render(this.content);
    }
  }
}
</script>

<style scoped>
  .msgItem {
    width: fit-content;
    height: fit-content;
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 20px;
  }

  .avatar {
    margin-right: 10px;
    user-select: none;
  }
  
  .me {
    background-color: #eee4ff;
    border: 1px solid #6700ab82;
  }

  .chatGpt {
    background-color: #E4EFFF;
    border: 1px solid #005bab82;
  }


  /deep/ p {
    margin: 0;
    font-size: 14px;
  }
</style>