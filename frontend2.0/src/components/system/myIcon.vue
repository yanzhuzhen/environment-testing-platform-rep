<template>
  <div class="chooseIcon">
    <el-popover placement="bottom" width="450" trigger="click">
                <span slot="reference" style="display:inline-block; width: 200px; height: 33px; line-height: 33px;">
                  <i :class="userChooseIcon"></i>
                  {{userChooseIcon}}
                </span>
      <div class="iconList">
        <i v-for="i in iconList" :key="i" :class="i" @click="setIcon(i)" style="font-size: 20px;"></i>
      </div>
    </el-popover>
  </div>
</template>

<script >
  import {elementIcons} from "@/utils/icon";

  export default {
    name:"myIcon",
    data(){
      return{
        iconList:[],
        userChooseIcon:""
      }
    },
    methods:{
      getIconList(){
        this.iconList = elementIcons
      },
      setIcon(icon){
        this.userChooseIcon = icon
        this.$emit("selecticon",icon)
      }
    },
    created() {
      this.getIconList()
    },
  }
</script>


<style scoped lang="scss">
.iconList{
  width: 400px;
  height: 300px;
  overflow-x: hidden;
  overflow-y: scroll;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  i{
    display: inline-block;
    width: 60px;
    height: 45px;
    color: white;
    font-size: 20px;
    border: 1px solid #e6e6e6;
    border-radius: 4px;
    cursor: pointer;
    text-align: center;
    line-height: 45px;
    margin: 5px;
    & :hover{
      color: orange;
      border-color: orange;
    }
  }
}
.chooseIcon{
  width: 175px;
  background-color: white;
  background-image: none;
  border-radius: 4px;
  border: 1px solid #DCDFE6;
  box-sizing: border-box;
  color: #606266;
  display: inline-block;
  font-size: inherit;
  height: 33px;
  line-height: 25px;
  outline: none;
  padding: 0 15px;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}
</style>
