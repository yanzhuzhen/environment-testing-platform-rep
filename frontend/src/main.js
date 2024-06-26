import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'
// import enLang from 'element-ui/lib/locale/lang/en'// 如果使用中文语言包请默认支持，无需额外引入，请删除该依赖

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log

import * as filters from './filters' // global filters
import myconfirm from "@/utils/myconfirm";
Vue.prototype.$myconfirm = myconfirm;

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
})

import {VueMasonryPlugin} from 'vue-masonry';
Vue.use(VueMasonryPlugin)


// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

import objCopy from '@/utils/objCopy'
Vue.prototype.$objCopy = objCopy

import resetForm from "@/utils/resetForm";

Vue.prototype.$resetForm = resetForm

Vue.config.productionTip = false

// 在你的组件中引入 highlight.js 和样式


//复制板
import VueClipboard from 'vue-clipboard2'
Vue.use(VueClipboard)


new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
