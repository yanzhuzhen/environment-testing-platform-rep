import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const timeKey = 'expireTime'

//获取token
export function getToken() {
  return Cookies.get(TokenKey)
}
//设置token
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}
//删除token
export function removeToken() {
  return Cookies.remove(TokenKey)
}

//清除缓存
export function clearStorage() {
  return sessionStorage.clear();
}

export function setTokeTime(time) {
  return  sessionStorage.setItem(timeKey, time);
}

export function getTokeTime(time) {
  return  sessionStorage.getItem(timeKey);
}

export function removeTokeTime(time) {
  return  sessionStorage.setItem(timeKey, 0);
}
