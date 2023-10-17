import {createApp} from "vue"
import home from "@/views/home";
import App from '@/App.vue'
import login from "@/views/login";
import {createRouter, createWebHashHistory} from 'vue-router'

    const routes= [
        {
            path: '/',
            name: 'Login',
            component: login,
            hidden: true
        },
        {
            path: '/home',
            name: 'Home',
            component:home ,
            hidden: true,
            meta: {
                roles: ['admin', 'user']
            },

        },
    ]
const routers = createRouter({
    history: createWebHashHistory(),
    routes
})
createApp(App).use(routers);
export default routers;








