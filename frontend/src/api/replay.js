import http from "@/utils/request";

export async function getReplays(param) {
  return  http.get('/api/replay/list', param);
}


export async function addReplays(data) {
  return http.post('/api/replay/add', data);
}


export async function deleteReplays(id) {
  return http.delete('/api/replay/delete/' + id)

}
