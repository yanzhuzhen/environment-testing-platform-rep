import { createApp } from 'vue'
// import { createPinia } from 'pinia'
import App from './App.vue'
import routers from "@/router/router";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from "axios";
const app = createApp(App)
app.config.globalProperties.$axios = axios
app.config.productionTip = false


app.use(ElementPlus)


// app.use(createPinia())
app.use(routers)

app.mount('#app')
