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
    <el-table :height="tableHeight" :data="logList" border stripe style="width: 100%; margin-bottom: 10px">
      <el-table-column prop="id" label="日志ID"></el-table-column>
      <el-table-column prop="loglevel" label="日志级别"></el-table-column>
      <el-table-column prop="logname" label="日志名称"></el-table-column>
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


export default {
  name: "logList",

  data() {
    return {
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
        value: 'Exmpl.Security.handler',
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
        let res = log.deleteLog(row.id);
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
