<template>
  <el-main>
    <el-form :model="searchModel" ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-select v-model="searchModel.logname" placeholder="请选择日志名称">
              <el-option
                v-for="item in lognameOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="8"><el-input v-model="searchModel.logclass" placeholder="请输入日志所在类查询"/></el-col>
          <el-col :span="8">
            <el-select v-model="searchModel.loglevel" placeholder="请选择日志级别">
              <el-option
                v-for="item in loglevelOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" type="primary" @click="search(pageNow, pageSize)">查询</el-button>
        <el-button icon="el-icon-delete"  @click="resetValue()">重置</el-button>
      </el-form-item>
    </el-form>
    <FilenameOption v-model="filename" />
    <BookTypeOption v-model="bookType" />
    <el-button style="margin:0 0 20px 20px;" type="primary" icon="el-icon-document" @click="handleDownload">
      导出Excel
    </el-button>
    <el-table :height="tableHeight" :data="logList" border stripe style="width: 100%; margin-bottom: 10px" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" />
      <el-table-column prop="id" label="日志ID"></el-table-column>
      <el-table-column prop="loglevel" label="日志级别"></el-table-column>
      <el-table-column prop="logname" label="日志名称">
        <template v-slot="scope">
          <el-tag v-if="scope.row.logname === 'Exmpl.Utils.oparateLogUtils'">操作日志</el-tag>
          <el-tag v-if="scope.row.logname === 'Exmpl.Security.handler.loginSuccessHandler'" type="success">登录日志</el-tag>
          <el-tag v-if="scope.row.logname === 'Exmpl.Utils.systemLogUtils'" type="danger">系统日志</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="logcontent" label="日志内容"></el-table-column>
      <el-table-column prop="logtime" label="生成时间"></el-table-column>
      <el-table-column prop="logclass" label="日志所在类"></el-table-column>
      <el-table-column prop="logthread" label="日志所在线程"></el-table-column>
      <el-table-column prop="trackid" label="全局追踪ID"></el-table-column>

      <el-table-column align="center" label="操作">
        <template v-slot="scope">
          <el-button type="danger" icon="el-icon-delete" size="small" @click="handleDelete(scope.row)" v-if="hasPermission('system:user:delete')">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
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

import systemDialog from "@/components/system/systemDialog.vue";
import {getToken} from "@/utils/auth";
import hasPermission from "@/permission/index";
import * as log from "@/api/log";
import deleteFile from "@/api/ossFile";
import BookTypeOption from "@/views/excel/components/BookTypeOption.vue";
import FilenameOption from "@/views/excel/components/FilenameOption.vue";


export default {
  name: "logList",
  components: {FilenameOption, BookTypeOption},

  data() {
    return {
      filename: '',
      autoWidth: true,
      bookType: 'xlsx',
      multipleSelection:[],
      searchModel: {
        loglevel: "",
        logtime: "",
        logclass: "",
        logthread: "",
        logname: "",
        trackid: "",
        pageNow: 1,
        pageSize: 10
      },
      tableHeight: 0,
      logList: [],
      pageNow: 1,
      pageSize: 10,
      total: 0,
      lognameOptions: [{
        value: 'Exmpl.Security.handler.loginSuccessHandler',
        label: '登录日志'
      },
      {
        value: 'Exmpl.Utils.systemLogUtils',
        label: '系统日志'
      },
        {
          value: 'Exmpl.Utils.oparateLogUtils',
          label: '操作日志'
        }
      ],
      loglevelOptions: [{
        value: 'info',
        label: '信息级别'
      },
      {
        value: 'debug',
        label: '调试级别'
      },
      {
        value: 'error',
        label: '错误级别'
      },
      {
        value: 'trace',
        label: '追踪级别'
      },
      {
        value: 'fatal',
        label: '严重错误级别'
      }],
    }
  },
  created() {
    this.search();
  },
  mounted() {
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 220;
    })

  },
  methods: {
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => v[j]))
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleDownload() {
      if (this.multipleSelection.length) {
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['日志ID', '日志级别', '日志名称', '日志内容','生成时间', '日志所在类', '日志所在线程', '全局追踪ID']
          const filterVal = ['id', 'loglevel', 'logname', 'logcontent','logtime', 'logclass', 'logthread', 'trackid']
          const list = this.multipleSelection
          console.log(list)
          for (let i = 0; i < list.length; i++) {
            if(list[i].logname === 'Exmpl.Utils.oparateLogUtils' ) list[i].logname = '操作日志';
            if(list[i].logname === 'Exmpl.Security.handler.loginSuccessHandler' ) list[i].logname = '登录日志';
            if(list[i].logname === 'Exmpl.Utils.systemLogUtils' ) list[i].logname = '系统日志';
          }
          const data = this.formatJson(filterVal, list)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: this.filename
          })
          this.$refs.roles.clearSelection()
        })
      } else {
        this.$message({
          message:'请选择要导出的行',
          type: 'warning'
        })
      }
    },
    hasPermission,
    handleSizeChange(size) {
      this.pageSize = size;
      this.search(this.pageNow, size);
    },
    handleCurrentChange(page) {
      this.pageNow = page
      this.search(page, this.pageSize);
    },
    async search(pageNow = 1, pageSize = 10) {
      //修改当前页码和当前每页数量
      this.searchModel.pageNow = pageNow;
      this.searchModel.pageSize = pageSize;
      //发送查询请求
      let res = await log.getLogList(this.searchModel);
      if (res.success) {
        this.logList = res.data.records
        this.total = res.data.total
      }
    },
    resetValue() {
      this.searchModel.loglevel = "";
      this.searchModel.logname = "";
      this.searchModel.logclass = "";
      this.search();
    },

    async handleDelete(row) {
      //提示是否确认删除
      let confirm = await this.$myconfirm("确定删除该日志吗？");
      if (confirm) {
        let res = await log.deleteLog(row.id);
        if (res.success) {
          this.$message.success(res.message);
          await this.search(this.pageNow, this.pageSize);
        } else {
          this.$message.error(res.message);

        }
      }
    },


  }
}

</script>

<style lang="scss">

</style>
