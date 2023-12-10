import http from "@/utils/request";

export async function getLogList(param) {
  return  http.get('/api/log/list', param);
}

export async function deleteLog(id) {
  return http.delete('/api/log/delete/' + id)

}
