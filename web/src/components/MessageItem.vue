<template>
  <div>
    <div style="display: flex;">
      <el-avatar class="avatar" size="medium" :icon="avatarIcon" v-loading="loading" element-loading-spinner="el-icon-loading"></el-avatar>
      <div :class="{ msgItem: true, me: newRole === 'user', chatGpt: newRole === 'system', error: newRole === 'error' }">
        <div class="content" v-highlight v-html="mdContent"></div>
      </div>
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

      newRole: this.role,
      loading: false,
    }
  },
  methods: {
    setContent(content) {
      this.$emit('onContentChange', this.index, content);
    },
    setRole(role) {
      this.newRole = role;
    },
    setLoading(isLoading) {
      this.loading = isLoading;
    }
  },
  computed: {
    mdContent() {
      return this.markdown.render(this.content);
    },
    avatarIcon() {
      let icon = 'el-icon-user-solid';
      switch(this.newRole) {
        case 'user':
          icon = 'el-icon-user-solid';
          break;
        case 'system':
          icon = 'el-icon-s-promotion';
          break;
        case 'error':
          icon = 'el-icon-warning';
          break;
      }
      return icon;
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

  .error {
    background-color: #ffe4e4;
    border: 1px solid #ab000082;
  }


  /deep/ p {
    margin: 0;
    font-size: 14px;
  }
</style>