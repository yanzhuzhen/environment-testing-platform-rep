<template>
  <el-container>
    <el-main>
  <!--    轮播图-->
      <el-carousel :interval="4000" type="card" height="300px">
        <el-carousel-item v-for="item in imgList" :key="item">
          <h3 class="medium"><img style="object-fit: scale-down; width: 100%; height: 100%;" :src="item.idView" alt=""/></h3>
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
    <el-footer>
      <el-card class="foot">
        footer
      </el-card>
    </el-footer>

  </el-container>
</template>

<script>
import  Masonry  from 'vue-masonry';

import CreateArticle from "@/views/articleComponents/create.vue";
import {ref, Ref} from "vue";
import * as article from "@/api/article";

export default {
  name:"homePage",
  components: {CreateArticle},
  data(){
    return{
      demoList:[1, 2, 1, 3, 4],
      imgList: [
        { id: 0, idView: require("/src/assets/1.jpg") },
        { id: 1, idView: require("/src/assets/2.jpg") },
        { id: 2, idView: require("/src/assets/3.jpg") }
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
.waterfall-item {
  break-inside: avoid;
  margin-bottom: 20px;
}

.el-row {
  margin-bottom: 20px;
}

.aside {
  font-size: 10px;
  background-color: #ffffff;
  color: rgba(55, 61, 75, 0.25);
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 7px;
  margin-right: 1px;
  height: fit-content;
  margin-top: 12px;
}

.foot {
  color: white;
  width: 100%;
  height:150px;
  margin-bottom: 10px;
  border: 1px solid #eee;
  border-radius: 5px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.15);
  background-color: rgb(36, 49, 65);
}
.card{
  position: relative;
  width: 100%;
  padding: 40px;
  background-color: white;
  box-shadow: 0 5px 15px rgba(0, 0, 0, .1);
  border-radius: 8px;
  text-align: center;
  margin-right: 50px;
  overflow: hidden;
}

.card:hover{
  color: white;
  background-color: #20a0ff;
}


</style>
