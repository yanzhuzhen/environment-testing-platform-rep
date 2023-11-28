//查询角色列表
import http from '@/utils/request'

export async function getAlgList(param) {
  return  http.get('/api/alg/list', param);
}
