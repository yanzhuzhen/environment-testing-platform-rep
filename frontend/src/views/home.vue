<template>
  <menu-header></menu-header>
<!--  <el-carousel :interval="4000" type="card" height="200px">-->
<!--    <el-carousel-item v-for="item in imgList" :key="item">-->
<!--      <h3 text="2xl" justify="center">{{ item }}</h3>-->
<!--    </el-carousel-item>-->
<!--  </el-carousel>-->
</template>

<script>
import MenuHeader from "@/components/menuHeader";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "home",
  components: {MenuHeader},
  // data(){
  //   return{
  //     imgList:[
  //       {id:0,idView:require("src/assets/1.jpg")},
  //       {id:1,idView:require("src/assets/2.jpg")},
  //       {id:2,idView:require("src/assets/3.jpg")}
  //     ]
  //   }
  // }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>