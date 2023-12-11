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

export async function sendCode(params){
  return http.post("api/sysUser/sendCode", params);
}

export async function updatePasswordByEmail(params){
  return http.post("api/sysUser/updatePasswordByEmail", params);
}

export async function activationPassword(params){
  return http.post("api/sysUser/activationPassword", params);
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
  async editUser(params){
    return http.put("api/user/edit",params);
  },
  async deleteUser(id){
    return http.delete("api/user/delete/"+id);
  },
  async getAssignRoleList(params){
    return http.get("api/user/getRoleListForAssign",params);
  },
  async getRnoByUno(id){
    return http.getRestApi("api/user/getRnoByUno/"+id);
  },
  async assignRoleSave(params){
    return http.post("api/user/saveUserRole",params);
  },
  async deleteRole(id){
    return http.delete("api/user/deleteRole/"+id);
  },
  async getAvatar(id){
    return http.get("api/user/getAvatar/"+id);
  },
  async getRolename(id){
    return http.get("api/user/getRolename/"+id);
  },

}
