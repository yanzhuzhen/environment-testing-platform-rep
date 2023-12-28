<template>
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
      </el-form-item>
    </el-form>
    <el-table :height="tableHeight" :data="articleList" border fit highlight-current-row style="width: 100%">
      <el-table-column width="180px" align="center" label="发布日期" >
        <template v-slot="scope">
          <span>{{ scope.row.displaytime }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120px" align="center" label="作者" >
        <template v-slot="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="200px" label="文章标题" >
        <template v-slot="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column width="300px" label="文章概要">
        <template v-slot="scope">
          <el-popover
            width="200"
            trigger="hover"
            :content="scope.row.contentshort">
            <div class="ellipsis" slot="reference">{{ scope.row.contentshort }}</div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column min-width="100px" label="查看文章" align="center">
        <template v-slot="scope">
          <span class="myLink" @click="upto(scope.row.id, scope.row.author)">查看文章</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="审核" width="200">
        <template v-slot="scope">
          <el-button type="success" icon="el-icon-circle-check" size="small" circle @click="enable(scope.row.id)" v-if="scope.row.status === 'published'"></el-button>
          <el-button type="info" icon="el-icon-circle-check" size="small" @click="enabled()" v-if="scope.row.status === 'enable'" round>已审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      align="center"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNow"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
  </el-main>
</template>

<script>
import Pagination from '@/components/Pagination'
import {parseTime} from "@/utils/index.js";
import * as article from "@/api/article";
import Myfooter from "@/components/footer/index.vue"; // Secondary package based on el-pagination

export default {
  name: 'ArticleList',
  components: {Myfooter, Pagination},
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
      nameScroll: false,
      tableHeight:0,
      articleList: [],
      total: 0,
      listLoading: false,
      pageNow: 1,
      pageSize: 10,
      searchModel: {
        title: "",
        author: "",
        displaytime:"",
        contentshort:"",
        pageNow: 1,
        pageSize: 10
      },
    }
  },
  created() {
    this.search();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    });
    this.nameScroll = this.$refs.songName1.clientWidth > this.$refs.first.clientWidth;
  },
  methods: {
    upto(id, author){
      this.$router.push({
        path:"../article/view",
        query:{
          id:id,
          author:author
        }
      })
    },
    async enable(id){
      let res = await article.enableArticle(id);
      if(res.success){
        await this.search();
        this.$message.success(res.message);
      }else {
        this.$message.error(res.message);
      }
    },
    enabled(){
      this.$message.success("该文章已审核");
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
        path: "./create",
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
      let res = await article.getArticleListM(this.searchModel);
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
.myLink:hover {
  color: #20a0ff;
}
.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: auto; /* 根据实际情况设置宽度 */
  animation: scroll 10s linear infinite;
}

</style>

