/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 17:31:36
 * @LastEditTime: 2024-06-19 20:11:20
 * @LastEditors: Alex
 */
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import FormCreate from '@form-create/element-ui'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn' // 中文语言
import * as Icons from "@element-plus/icons-vue";
import App from './App'
import store from "./store";
import '@/styles/index.scss' // 全局样式
import "@/styles/rest.scss";
import "@/styles/global.scss";
import router from './router' // 路由
import SvgIcon from "@/components/SvgIcon/index.vue";
import modal from './utils/modal'
import VuePdfEmbed from 'vue-pdf-embed'
const app = createApp(App)
// 全局方法挂载
app.config.globalProperties.$modal = modal
Object.keys(Icons).forEach(key => {
  app.component(key, Icons[key]);
});
app.use(store);
app.use(VuePdfEmbed)
app.use(FormCreate)
// app.use(ElementPlus)
app.use(ElementPlus, {
    locale: locale,
    // 支持 large、default、small
    size:  'small'
  })
app.use(router)
app.component("svg-icon", SvgIcon)
app.directive("drag", {
    mounted (el, binding, vnode, prevVnode) {
        let oDiv = el; // 获取当前元素
        oDiv.onmousedown = (e) => {
          // 算出鼠标相对元素的位置
          let disX = e.clientX - oDiv.offsetLeft;
          let disY = e.clientY - oDiv.offsetTop;
          document.onmousemove = (e) => {
            // 用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
            let left = e.clientX - disX;
            let top = e.clientY - disY;
            oDiv.style.left = left + 'px';
            oDiv.style.top = top + 'px';
          };
  
          document.onmouseup = (e) => {
            document.onmousemove = null;
            document.onmouseup = null;
          }
        }
    }
})

app.mount('#app')
