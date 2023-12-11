<template>
  <div class="app-container">
    <el-table :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column align="center" label="文章编号" width="80">
        <template v-slot="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column width="180px" align="center" label="发布日期">
        <template v-slot="scope">
          <span>{{ scope.row.timestamp | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120px" align="center" label="作者">
        <template v-slot="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="200px" label="文章标题">
        <template v-slot="{row}">
          <router-link :to="'/example/edit/'+row.id" class="link-type">
            <span>{{ row.title }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column min-width="300px" label="文章概要">
        <template v-slot="{row}">
          <router-link :to="'/example/edit/'+row.id" class="link-type">
            <span>{{ row.contentshort }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column min-width="100px" label="文章评分">
        <template v-slot="{row}">
          <span>{{ row.score }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200">
        <el-button v-if="this.isFollows()" type="primary" size="small" icon="el-icon-edit">
          关注作者
        </el-button>
        <el-button v-if="this.isFollows()" type="info" size="small" icon="el-icon-check">
          已关注
        </el-button>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import { fetchList } from '@/api/article'
import Pagination from '@/components/Pagination'
import {parseTime} from "@/utils"; // Secondary package based on el-pagination

export default {
  name: 'ArticleList',
  components: { Pagination },
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
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    isFollows(){

    },
    parseTime,
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
