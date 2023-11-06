export default function hasPermission(params){
  //标识是否拥有权限
  let tag=false;
  //从sessionStorage中获取codeList权限字段列表
  let codeList = JSON.parse(sessionStorage.getItem("codeList"));
  //循环遍历权限字段列表
  for (let i = 0; i< codeList.length;i++){
    //判断当前权限字段是否与参数传递过来的字段一致
    if(codeList[i] === params)
    {
      tag = true;
      break;
    }
  }

  return tag;
}
