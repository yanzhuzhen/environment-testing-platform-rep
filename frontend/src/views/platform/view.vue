<template>
<el-container>
  <el-header>
    <el-page-header @back="goBack" content="查看文章" style="margin-top: 10px"></el-page-header>
  </el-header>
  <el-main>
    <div class="title">
      {{mytitle}}
    </div>
    <el-container>
      <div class="aside">
        <div style="font-size: 15px;margin-top: 5px;margin-bottom: 5px">该作者排名前十的文章</div>
        <template>
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="title" label="名称" align="center"></el-table-column>
            <el-table-column prop="score" label="评分" align="center" class="score"></el-table-column>
          </el-table>
        </template>
      </div>
      <el-main>
        <el-card class="box-card" style="padding-right: 20px;padding-left: 20px;padding-bottom: 20px">
          <div class="author">
            {{myauthor}}
            <el-rate
              v-model="score"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}">
            </el-rate>
            <el-divider></el-divider>
          </div>
        </el-card>

      </el-main>
      <div class="aside">
        <div style="font-size: 15px;margin-top: 5px;margin-bottom: 5px">评论区</div>
        <template>
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="title" label="名称" align="center"></el-table-column>
            <el-table-column prop="score" label="评分" align="center" class="score"></el-table-column>
          </el-table>
        </template>
      </div>
    </el-container>
  </el-main>
</el-container>
</template>

<script>
import * as article from "@/api/article";
import hljs from 'highlight.js'
import 'highlight.js/styles/default.css' // 选择你喜欢的样式


export default {
  name:"articleView",
  data(){
    return{
      mycontent:"",
      mytitle:"",
      myauthor:"",
      tableData:[],
      score:"",
      scoreT:toString(this.score*5)
    }
  },
  created() {
    this.getContent();
    this.getScore();
  },
  methods:{
    goBack() {
      this.$router.back();
    },
    async getContent(){
      let res = await article.getArticle(this.$route.query.id);
      if(res.success){
        console.log(res.data);
        this.mycontent = res.data.content;
        this.mytitle = res.data.title;
        this.myauthor = res.data.author;
        await this.getRank(this.myauthor);
        let root = document.querySelector(".el-card");
        root.insertAdjacentHTML('beforeend',this.mycontent);
        hljs.highlightAll();
      }
    },
    async getScore(){
      let res = await article.getScore(this.$route.query.id);
      if(res.success){
        this.score = res.data/5;
      }
    },
    async getRank(author){
      let res = await article.getRank(author);
      if(res.success){
        this.tableData = res.data;
      }
    },

  }
}
</script>

<style scoped lang="scss">
.title {
  font-size: 30px;
  background-color: #20a0ff;
  color: white;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  height: 50px;
  border-radius: 10px;
  padding-left: 20px;
  padding-top: 7px;


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
  width: 300px;
  height: fit-content;
  margin-top: 20px;
}

.el-card {
  border-radius: 10px;
  padding-left:50px

}

.author {
  font-size: 20px;
  color: gray;
  text-align: end;
  margin-bottom: 10px;
}

.score {
  color:#ff9900;
}
.pre{
  width: 100%;
}

</style>
