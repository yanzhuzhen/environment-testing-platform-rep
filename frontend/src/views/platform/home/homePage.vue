<template>
  <el-container>
    <el-main>
  <!--    轮播图-->
      <el-carousel :interval="4000" type="card" height="300px">
        <el-carousel-item v-for="item in imgList" :key="item">
          <template v-if="item.idView != null"><el-image style="object-fit: scale-down; width: 100%; height: 100%;" :src="item.idView" alt="WebPImage"/></template>
        </el-carousel-item>
      </el-carousel>
      <el-card class="box-card" shadow="hover">
        <el-container>
          <el-aside width="300px">
            <div class="aside">
              <div style="font-size: 15px;margin-top: 5px;margin-bottom: 5px">最多好评</div>
              <template>
                <el-table :data="tableData" style="width: 100%">
                  <el-table-column prop="title" label="名称" align="center"></el-table-column>
                  <el-table-column prop="score" label="评分" align="center" class="score"></el-table-column>
                </el-table>
              </template>
              <div style="font-size: 10px;margin-top: 5px;margin-bottom: 5px">[仅展示前20]</div>
            </div>
          </el-aside>
          <el-main>
              <div v-for="i in homepage" :key="i.id" class="waterfall-item">
                <el-card class="card" style="margin-bottom: 10px;" >
                  <div slot="header" class="clearfix">
                    <span>{{ i.title }}</span>
                  </div>
                  <div  class="text item" style="margin-bottom: 10px">
                    {{'文章概述:' + i.contentshort}}
                  </div>
                  <div  class="text item" style="margin-bottom: 10px">
                    {{'文章作者:' + i.author}}
                  </div>
                  <div  class="text item" style="margin-bottom: 10px">
                    <p @click="upto(i.id, i.author)">查看文章</p>
                  </div>
                  <el-rate
                   v-model="i.score"
                   disabled
                   show-score
                   text-color="#ff9900"
                   score-template="{value}">
                  </el-rate>
                </el-card>
              </div>
          </el-main>
        </el-container>
      </el-card>

    </el-main>
    <el-footer height="150px">
      <myfooter></myfooter>
    </el-footer>
  </el-container>
</template>

<script>
import  Masonry  from 'vue-masonry';


import {ref, Ref} from "vue";
import * as article from "@/api/article";

export default {
  name:"homePage",
  components: {Myfooter:() => import("@/components/footer/index.vue")},
  data(){
    return{
      demoList:[1, 2, 1, 3, 4],
      imgList: [
        { id: 0, idView: require("/src/assets/1.webp") },
        { id: 1, idView: require("/src/assets/2.webp") },
        { id: 2, idView: require("/src/assets/3.webp") }
      ],
      count: 0,
      items: [
        { content: 'Item 1' },
        { content: 'Item 2' },
        { content: 'Item 3' },
        ],
      tableData:[],
      homepage:[]
    }
  },
  mounted() {
  },
  created() {
    this.getList();
    this.homeList();
  },
  methods:{
    upto(id, author){
      this.$router.push({
        path:"../article/view",
        query:{
          id:id,
          author:author
        }
      })
    },
    load () {
      this.count += 2
    },
    async getList(){
      let res = await article.Rank();
      if(res.success){
        this.tableData = res.data;
      }

    },
    async homeList(){
      let res = await article.homePage();
      if(res.success){
        this.homepage = res.data;
      }
    }
  }
}
</script>


<style lang="scss">
@import url("./home.scss");


</style>
