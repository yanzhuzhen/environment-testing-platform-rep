
//清空表单和数据
export default function resetForm(formName, obj){
  if(this.$refs[formName]){
    this.$refs[formName].resetFields();
  }
  Object.keys(obj).forEach(key =>{
    obj[key] = '';
  })
}
