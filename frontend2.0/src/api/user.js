import http from '@/utils/request'


//用户登录方法
export async function login(data) {
  return http.login("api/login", data);
}
//获取登录用户信息方法
export async function getInfo() {
  return http.get("api/sysUser/getInfo");
}
//退出登录
export async function logout(parmas) {
  return http.post("api/sysUser/logout",parmas);
}

export async function getMenuList(){
  return http.get("api/sysUser/getMenuList");
}
