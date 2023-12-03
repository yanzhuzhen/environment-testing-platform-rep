<template>
  <el-main>
<!--    轮播图-->
    <el-carousel :interval="4000" type="card" height="300px">
      <el-carousel-item v-for="item in imgList" :key="item">
        <h3 class="medium"><img style="object-fit: scale-down; width: 100%; height: 100%;" :src="item.idView" alt=""/></h3>
      </el-carousel-item>
    </el-carousel>
    <el-card class="box-card" shadow="hover">
      <el-container>
        <el-aside width="200px">
            <div>
              最多好评
            </div>
        </el-aside>
        <el-main>
        </el-main>
      </el-container>
    </el-card>


  </el-main>
</template>

<script>


import CreateArticle from "@/views/articleComponents/create.vue";

export default {
  name:"homePage",
  components: {CreateArticle},
  data(){
    return{
      imgList: [
        { id: 0, idView: require("/src/assets/1.jpg") },
        { id: 1, idView: require("/src/assets/2.jpg") },
        { id: 2, idView: require("/src/assets/3.jpg") }
      ]
    }
  }
}
</script>


<style lang="scss">
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
.el-aside {
  background-color: #ffffff;
  color: #1e1f22;
}

</style>
