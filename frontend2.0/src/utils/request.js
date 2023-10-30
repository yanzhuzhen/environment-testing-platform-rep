import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import qs from 'qs';
import message from "element-ui/packages/message";

// 创建axios异步请求的实列
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, //拼接地址
  timeout: 50000 //请求超时时间
})
// 请求前拦截
service.interceptors.request.use(
  config => {
    //判断store中是否存在token
    if (store.getters.token) {
      // 读取token信息，并将其交给请求头
      config.headers['token'] = getToken()
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

//响应是拦截
service.interceptors.response.use(
  response => {
    //获取后端返回的数据
    const res = response.data
    // 如果状态码不是200，则失败
    if (res.code !== 200) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: token不合法; 50012: 其他错误; 50014: token过期;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // 重新登录
        MessageBox.confirm('用户信息过期，请重新登录', '系统提示', {
          confirmButtonText: '登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
//请求方法
const http = {
  //POST请求
  post(url, params){
    return service.post(url, params, {
      transformRequest:[(params) => {
        return JSON.stringify(params)
      }],
      headers: {
        'Content-Type':'application/json'
      }
    })
  },
  //PUT请求
  put(url, params){
    return service.put(url, params, {
      transformRequest:[(params) => {
        return JSON.stringify(params)
      }],
      headers: {
        'Content-Type':'application/json'
      }
    })
  },
  //GET请求
  get(url, params){
    return service.get(url, {
      params:params,
      paramsSerializer:(params) =>{
        return qs.stringify(params)
      }
    })
  },
  //REST风格的GET请求
  getRestApi(url ,params){
    let _params
    if (Object.is(params, null)){
      _params = ''
    }else {
      _params = '/'
      for(const key in params){
        if(params.hasOwnProperty(key) && params[key] !== null && params[key] !== ''){
          _params += `${params[key]}/`
        }
      }
      _params =  _params.substring(0, _params.length - 1)
    }
    if(_params){
      return service.get('${url}${_params}')
    }else {
      return service.get(url)
    }
  },
  //删除请求
  delete(url, params){
    let _params
    if (Object.is(params, null)){
      _params = ''
    }else {
      _params = '/'
      for(const key in params){
        if(params.hasOwnProperty(key) && params[key] !== null && params[key] !== ''){
          _params += `${params[key]}/`
        }
      }
      _params =  _params.substring(0, _params.length - 1)
    }
    if(_params){
      return service.delete('${url}${_params}').catch(err =>{
        message.error(err.msg)
        return Promise.reject(err)
      })
    }else {
      return service.delete(url).catch(err =>{
        message.error(err.msg)
        return Promise.reject(err)
      })
    }

  },
  //文件上传请求
  upload(url, params){
    return service.post(url, params, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  //登录请求
  login(url,params){
    return service.post(url, params, {
      transformRequest:[(params) => {
        return qs.stringify(params)
      }],
      headers: {
        'Content-Type':'application/x-www-form-urlencoded'
      }
    })
  }

}

export default http
