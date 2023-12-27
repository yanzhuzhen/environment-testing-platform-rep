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
        <el-card class="box-card" style="padding-right: 20px;padding-left: 20px;padding-bottom: 20px;">
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
      <div class="aside" style="width: 300px;">
        <div style="font-size: 15px;margin-top: 5px;margin-bottom: 5px" v-if="!isScored">
          来打分吧！
          <el-rate
            v-model="scoring"
            :colors="colors"
            @change="Score">
          </el-rate>
        </div>
        <div style="font-size: 15px;margin-top: 5px;margin-bottom: 5px" v-if="isScored">
          你已评过分！
          <el-rate
            disabled
            v-model="scoring"
            :colors="colors">
          </el-rate>
        </div>
        <template v-if="isComment === 0" style="">
          <div style="font-size: 15px;margin-top: 10px;margin-bottom: 5px">来评论吧！</div>
          <div class="comment-list">
            <div v-for="(comment, index) in commentList" :key="index" class="comment-item">
              <div class="comment-content">{{comment.username}}:{{ comment.content }}</div>
            </div>
          </div>
          <el-form ref="commentForm" :model="comment" style="width: 300px">
            <el-form-item label="评论:">
              <el-input type="textarea" v-model="comment.content" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitComment">提交评论</el-button>
            </el-form-item>
          </el-form>
        </template>
      </div>
    </el-container>
  </el-main>
</el-container>
</template>

<script>
import * as article from "@/api/article";
import * as comment from "@/api/comment";
import hljs from 'highlight.js'
import 'highlight.js/styles/default.css' // 选择你喜欢的样式


export default {
  name:"articleView",
  data(){
    return{
      webSocketObject: null,
      mycontent:"",
      mytitle:"",
      myauthor:"",
      tableData:[],
      score:"",
      scoreT:toString(this.score*5),
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      scoring:null,
      isComment:0,
      isScored:false,
      commentList:[],
      replayList:[],
      comment:{
        content:"",
        articleid: this.$route.query.id,
        username:this.$store.getters.username
      },
      loading:true,

    }
  },
  created() {
    this.getContent();
    this.getScore();
    this.isscored();
    this.webSocketInit();
    this.getComment();
  },
  mounted() {
  },
  methods:{
    webSocketInit(){
      const webSocketUrl = 'ws://localhost:3000/websocket'
      this.webSocketObject = new WebSocket(webSocketUrl);
      this.webSocketObject.onopen = this.webSocketOnOpen
      this.webSocketObject.onmessage = this.webSocketOnMessage
      this.webSocketObject.onerror = this.webSocketOnError
      this.webSocketObject.onclose = this.webSocketOnClose
    },
    webSocketOnOpen(e){
      console.log('与服务端连接打开->',e)
    },
    webSocketOnMessage(e){
      console.log('来自服务端的消息->',e)
      const receiveMessage = JSON.parse(e.data);
      this.commentList.push(receiveMessage)
    },

    webSocketOnError(e){
      console.log('与服务端连接异常->',e)
    },
    webSocketOnClose(e){
      console.log('与服务端连接关闭->',e)
    },
    goBack() {
      this.$router.back();
    },
    async isscored(){
      let res = await article.isScored(this.$route.query.id, this.$store.getters.uno);
      if (res.success) {
        if(res.data>0){this.isScored = true;}
        this.$forceUpdate();
      }
    },
    async Score(){
      let res = await article.Score(this.$route.query.id, this.$store.getters.uno, this.scoring);
      if (res.success) {
        this.scoring = res.data;
        this.isScored = false;
        this.$forceUpdate();
      }
    },
    async submitComment() {
      let res = await comment.addComments(this.comment);
      if (res.success) {
        await this.getComment();
        const username = this.comment.username
        const message = this.comment.content
        await this.webSocketObject.send(JSON.stringify({
          id: 1,
          message,
          username,
          time: new Date().getTime()
        }))
        this.comment.content = ""
        this.$message.success(res.message);
      }
    },
    async getComment(){
      let param = {
        articleid:this.$route.query.id,
      }
      let res = await comment.getComments(param);
      if(res.success){
        this.commentList = res.data
        console.log(this.commentList);
      }
    },
    async getContent(){
      let res = await article.getArticle(this.$route.query.id);
      if(res.success){
        console.log(res.data);
        this.mycontent = res.data.content;
        this.mytitle = res.data.title;
        this.myauthor = res.data.author;
        this.isComment = res.data.commentdisable;
        await this.getRank(this.myauthor);
        let root = document.querySelector(".el-card");
        root.insertAdjacentHTML('beforeend',this.mycontent);
        hljs.highlightAll();
        loading = false;
      }
    },
    async getScore(){
      let res = await article.getScore(this.$route.query.id);
      if(res.success){
        this.score = res.data;
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

<style lang="scss">
@import url("./view.scss");
</style>
