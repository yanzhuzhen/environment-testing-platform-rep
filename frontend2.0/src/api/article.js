import http from "@/utils/request";

export async function getArticleList(param) {
  return  http.get('/api/article/list', param);
}

export async function getArticleListM(param) {
  return  http.get('/api/article/listM', param);
}

export async function getArticleListU(param) {
  return  http.get('/api/article/listU', param);
}

export async function like(id, uno) {
  return  http.post('/api/article/likes/'+ id + '/' + uno);
}

export async function alreadyLike(id, uno) {
  return  http.get('/api/article/alreadyLike/'+ id + '/' + uno);
}

export async function getEditArticle(id) {
  return  http.get('/api/article/getEditArticle/'+ id);
}

export async function getArticle(id) {
  return  http.get('/api/article/getArticle/'+ id);
}

export async function addArticle(data) {
  return http.post('/api/article/add', data);
}


export async function updateArticle(data) {
  return http.put('/api/article/update/' ,data);
}


export async function deleteArticle(id) {
  return http.delete('/api/article/delete/' + id)

}

export async function publishArticle(data) {
  return http.put('/api/article/publish', data)

}

export async function enableArticle(id) {
  return http.put('/api/article/enable/' + id)

}
