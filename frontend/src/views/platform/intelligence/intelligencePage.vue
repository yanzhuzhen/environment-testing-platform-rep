<template>
  <el-container>
    <el-main>
      <el-form :model="searchModel" ref="searchForm" label-width="80px" :inline="true" size="small">
        <el-form-item>
          <el-row :gutter="24">
            <el-col :span="12"><el-input v-model="searchModel.title" placeholder="请输入文章标题查询"/></el-col>
            <el-col :span="12"><el-input v-model="searchModel.author" placeholder="请输入作者查询"/></el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="search(pageNow, pageSize)">查询</el-button>
          <el-button icon="el-icon-delete"  @click="resetValue()">重置</el-button>
          <el-button icon="el-icon-plus" type="success" size="small" @click="openAddWindow()">发布我的文章</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="12" >
        <!-- date遍历循环的数据 -->
        <el-col :span="6" v-for="item in articleList" :key="item.id">
          <el-card  :body-style="{ height: '200px' }" class="my-el-card" shadow="hover" ><!--style="background-color: #5daf34"  灰 #e1e1e1 绿 #5daf34-->
            <!-- 卡片的头部位 -->
            <template #header>
              <div class="card-header">
                  <span>{{ item.title }}</span>
              </div>
            </template>
            <!-- 卡片显示的内容 -->
            <div  class="text-item" >
              {{item.contentshort}}
            </div>

            <div class="fix-btn">
              <div  class="text-item-author" >
                {{item.author}}
              </div>
              <el-row :gutter="20" >
                <el-col :span="12" >
                  <span style="padding-left: 3px;">{{ item.displaytime }}</span>
                  <div style="width: 200px;">
                    <el-rate
                      v-model="item.score"
                      disabled
                      show-score
                      :colors="colors"
                      text-color="#ff9900"
                      score-template="{value}">
                    </el-rate>
                  </div>
                </el-col>
                <el-col :span="6" :offset="6">
                  <el-button round plain type="primary" class="myLink" @click="upto(item.id, item.author)"><i class="el-icon-view"></i></el-button>
                </el-col>
              </el-row>

            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-pagination
        align="center"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNow"
        :page-sizes="[12, 24, 36, 48]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-main>
    <el-footer height="150px" >
      <myfooter></myfooter>
    </el-footer>
  </el-container>

</template>

<script>

import {parseTime} from "@/utils/index.js";
import * as article from "@/api/article";
import Myfooter from "@/components/footer/index.vue";

export default {
  name: 'ArticleList',
  components: {Myfooter},
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      articleList: [],
      total: 0,
      listLoading: false,
      pageNow: 1,
      pageSize: 12,
      searchModel: {
        title: "",
        author: "",
        displaytime:"",
        contentshort:"",
        pageNow: 1,
        pageSize: 12
      },
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      scoring:null,
    }
  },
  created() {
    this.search();
  },
  mounted() {

  },
  methods: {
    upto(id, author){
      console.log(typeof id);
      this.$router.push({
        path:"../article/view",
        query:{
          id:id,
          author:author
        }
      })
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.search(this.pageNow, size);
    },
    handleCurrentChange(page) {
      this.pageNow = page
      this.search(page, this.pageSize);
    },
    openAddWindow() {
      this.$router.push({
        path: "../article/create",
      })
    },
    resetValue() {
      this.searchModel.title = "";
      this.searchModel.author = "";
      this.search();
    },
    isFollows(){

    },
    parseTime,
    async search() {
      this.listLoading = true
      let res = await article.getArticleList(this.searchModel);
      if(res.success){
        this.articleList = res.data.records;
        console.log(res.data.records)
        this.total = res.data.total;
      }
    }
  }
}
</script>

<style scoped>
@import url("./intell.scss");
</style>

