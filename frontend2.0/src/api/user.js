import http from '@/utils/request'

//用户登录方法
export async function login(data) {
  return http.login("api/user/login", data);
}
//获取登录用户信息方法
export async function getInfo() {
  return http.get("api/sysUser/getInfo");
}
//退出登录
export async function logout(params) {
  return http.post("api/sysUser/logout",params);
}

export async function getMenuList(){
  return http.get("api/sysUser/getMenuList");
}

export async function refreshToken(params){
  return http.post("api/sysUser/refreshToken", params);
}

export async function signup(params){
  return http.post("api/sysUser/signup", params);
}

export  default {
  async getUserList(params){
    return http.get("api/user/list",params);
  },
  async addUser(params){
    return http.post("api/user/add",params);
  },
  async updateUser(params){
    return http.put("api/user/update",params);
  },
  async deleteUser(params){
    return http.delete("api/user/delete",params);
  },
  async getAssignRoleList(params){
    return http.get("api/user/getRoleListForAssign",params);
  },
  async getRnoByUno(params){
    return http.getRestApi("api/user/getRnoByUno",params);
  },
  async assignRoleSave(params){
    return http.post("api/user/saveUserRole",params);
  },
  async deleteRole(params){
    return http.delete("api/user/deleteRole",params);
  },

}
