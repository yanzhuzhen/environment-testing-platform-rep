//查询角色列表
import http from '@/utils/request'

export async function getAlgList(param) {
  return  http.get('/api/alg/list', param);
}

export async function findAlgByAno(id) {
  return  http.get('/api/alg/alg/'+id);
}


export async function addAlg(data) {
  return http.post('/api/alg/add', data);
}


export async function updateAlg(data) {
  return http.put('/api/alg/update/' ,data);
}


export async function deleteAlg(id) {
  return http.delete('/api/alg/delete/' + id)

}

export async function ispermit(id) {
  return http.put('/api/alg/push/' + id)

}
