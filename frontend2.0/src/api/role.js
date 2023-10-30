import http from '@/utils/request'
import {param} from "@/utils";

export function getRoutes() {

}

//查询角色列表
export async function getRoles(param) {
  return http.get('/api/role/list', param);
}

//添加角色
export async function addRole(data) {
  return http.post('/api/role/add', data);
}

//修改角色
export async function updateRole(id, data) {
  return http.put('/api/role/update/' + id, data);
}

//删除角色
export async function deleteRole(id) {
  return http.delete('/api/role/delete/' + id)

}

//检查角色是否被占用
export async function checkRole(param) {
  return http.getRestApi('/api/role/check/' + param)

}
