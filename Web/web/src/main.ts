import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'
const VueApp = createApp(App)

VueApp.use(ElementPlus,{locale})
VueApp.use(router)
VueApp.mount('#app')
