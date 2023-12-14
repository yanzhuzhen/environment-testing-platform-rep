import http from "@/utils/request";

export async function getComments(param) {
  return  http.get('/api/comment/list', param);
}


export async function addComments(data) {
  return http.post('/api/comment/add', data);
}


export async function deleteComments(id) {
  return http.delete('/api/comment/delete/' + id)

}
