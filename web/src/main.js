import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/base.css'
import '@/components/index'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

// MarkDown代码块高亮
import hljs from 'highlight.js'
import 'highlight.js/styles/hybrid.css'
Vue.directive('highlight',function (el) {
  let blocks = el.querySelectorAll('pre code');
  blocks.forEach((block)=>{
    hljs.highlightBlock(block)
  })
})


Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
