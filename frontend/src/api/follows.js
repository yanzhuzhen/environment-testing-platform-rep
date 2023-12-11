import http from "@/utils/request";

export async function getFollowsList(param) {
  return  http.get('/api/follows/list', param);
}

export async function addFollows(data) {
  return http.post('/api/follows/add', data);
}

export async function deleteFollows(param) {
  return http.delete('/api/follows/delete/' + param);

}
