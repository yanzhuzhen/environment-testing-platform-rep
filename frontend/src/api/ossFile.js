import http from "@/utils/request";

export default async function deleteFile(url) {
  return http.delete('/api/oss/file/delete/' + url);

}
