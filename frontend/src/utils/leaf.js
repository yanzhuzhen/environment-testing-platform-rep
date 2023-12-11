export default function leafUtils(){
  const setLeaf = (leafs) => {
    if(leafs && leafs.length >0){
      for(let i =0; i <leafs.length; i++){
        if (leafs[i].children && leafs[i].children.length > 0){
          leafs[i].open = false;
          setLeaf(leafs[i].children)
        }else {
          leafs[i].open = true;
        }
      }
    }
    return leafs;
  }
  return{
    setLeaf
  }
}
