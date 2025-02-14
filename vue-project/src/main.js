import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/global.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn' // 导入中文语言包
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(router)
app.use(ElementPlus, {
    locale: zhCn // 使用中文语言配置
})
app.mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}