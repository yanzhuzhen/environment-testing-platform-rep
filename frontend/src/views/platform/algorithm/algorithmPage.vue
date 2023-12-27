<template>
  <el-container>
    <el-main>
      <el-form :model="searchModel" ref="searchForm" label-width="80px" :inline="true" size="small">
        <el-form-item>
          <el-row :gutter="24">
            <el-col :span="8"><el-input v-model="searchModel.algname" placeholder="请输入算法名查询"/></el-col>
            <el-col :span="8"><el-input v-model="searchModel.createuser" placeholder="请输入发布者名查询"/></el-col>
            <el-col :span="8">
              <el-select v-model="searchModel.lang" clearable placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="search(searchModel.pageNow, searchModel.pageSize)">查询</el-button>
          <el-button icon="el-icon-delete"  @click="resetValue()">重置</el-button>
          <el-button icon="el-icon-plus" type="success" size="small" @click="openAddWindow()">发布我的算法</el-button>
        </el-form-item>
      </el-form>
      <el-table :height="tableHeight" :data="algList" border stripe style="width: 100%; margin-bottom: 10px">
        <el-table-column prop="algname" label="算法名称"></el-table-column>
        <el-table-column prop="remark" label="算法概要"></el-table-column>
        <el-table-column prop="createuser" label="发布者"></el-table-column>
        <el-table-column prop="algtype" label="算法类型"></el-table-column>
        <el-table-column prop="lang" label="算法语言"></el-table-column>
        <el-table-column align="center" width="290" label="算法内容">
          <template v-slot="scope" align="center">
            <el-button type="primary" icon="el-icon-edit" size="small" @click="openView(scope.row.ano)">点击查看</el-button>
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
      <!--编辑算法-->
      <system-dialog :title="algEditDialog.title" :visible="algEditDialog.visible" :width="algEditDialog.width" :height="algEditDialog.height"
                     @onClose="onClose()" @onConfirm="onConfirm()">
        <div slot="content">
          <el-form ref="editForm" :model="algEdit"  label-width="80px"  size="small" >
            <el-form-item label="算法名称" prop="algname" size="small">
              <el-input v-model="algEdit.algname"></el-input>
            </el-form-item>
            <el-form-item label="算法概要" size="small" prop="remark" >
              <el-input  v-model="algEdit.remark"></el-input>
            </el-form-item>
            <el-form-item label="算法类型" size="small" prop="algtype" >
              <el-input  v-model="algEdit.algtype" ></el-input>
            </el-form-item>
            <el-form-item label="算法语言" size="small" >
              <template>
                <el-select v-model="algEdit.lang" clearable placeholder="请选择">
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-form-item>
            <el-form-item label="算法：" size="small" prop="email" >
              <CommonEditor
                :read-only="false"
                :value="algEdit.context"
                :language="algEdit.lang"
                @input="changeTextareaEdit"
                style="height: 500px"
              ></CommonEditor>
            </el-form-item>
          </el-form>
        </div>
      </system-dialog>

      <!--查看算法-->
      <system-dialog :title="algDialog.title" :visible="algDialog.visible" :width="algDialog.width" :height="algDialog.height"
                     @onClose="closeView()" @onConfirm="closeView()">
        <div slot="content">
          <el-button v-clipboard:copy="algContext.context" v-clipboard:success="onCopy">复制代码</el-button>
          <CommonEditor
            :value="algContext.context"
            :language="algContext.lang"
            @input="changeTextareaView"
            style="height: auto"
          ></CommonEditor>
        </div>
      </system-dialog>
    </el-main>
    <el-footer height="150px">
      <myfooter></myfooter>
    </el-footer>
  </el-container>


</template>

<script>
import * as alg from "@/api/alg";



export default {
  name: "algList",
  components: {
    Myfooter:() => import("@/components/footer/index.vue"),
    CommonEditor:() => import("@/views/CommonEditor/CommonEditor.vue"),
    systemDialog:() => import("@/components/system/systemDialog.vue")
  },
  data() {
    return {
      algContext:{
        context: "",
        lang:""
      },
      algEdit: {
        ano: "",
        algname: "",
        remark: "",
        algtype: "",
        lang: "",
        context:""
      },
      algDialog: {
        title: "查看算法",
        visible: false,
        height: 500,
        width: 100
      },
      algEditDialog: {
        title: "发布算法",
        visible: false,
        height: 700,
        width: 100
      },
      algList:[],
      searchModel: {
        algname: "",
        createuser:"",
        lang:"",
        pageNow: 1,
        pageSize: 10,
      },
      tableHeight:0,
      pageNow: 1,
      pageSize: 10,
      total: 0,
      options: [{
        value: 'python',
        label: 'python'
      }, {
        value: 'java',
        label: 'java'
      }, {
        value: 'c',
        label: 'c'
      }, {
        value: 'c++',
        label: 'c++'
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
    openAddWindow(){
      this.algEditDialog.visible = true;
    },
    onCopy(){
      this.$message.success("复制成功！");
    },
    changeTextareaEdit(val) {
      this.algEdit.context = val
    },
    changeTextareaView(val) {
      this.algContext.context = val
    },
    closeView(){
      this.algDialog.visible = false;
    },
    async openView(id){
      let res = null;
      res = await alg.findAlgByAno(id);
      if(res.success){
        this.algContext.context = res.data.context;
        this.algContext.lang = res.data.lang;

      }
      this.algDialog.visible = true;
    },
    onClose() {
      this.algEditDialog.visible = false;
    },
    onConfirm() {
      this.$refs.editForm.validate(async (valid) => {
        if (valid) {
          let res = null;
          res = await alg.addAlg(this.algEdit);
          if (res.success) {
            this.algEditDialog.visible = false;
            this.$message.success(res.message);
          } else {
            this.$message.error(res.message);
          }
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
    async search(pageNow = 1, pageSize = 10) {
      //修改当前页码和当前每页数量
      this.searchModel.pageNow = pageNow;
      this.searchModel.pageSize = pageSize;
      //发送查询请求
      let res = await alg.getAlgList(this.searchModel);
      if (res.success) {
        this.algList = res.data.records
        this.total = res.data.total
      }
    },
    resetValue() {
      this.searchModel.algname = "";
      this.searchModel.lang = "";
      this.searchModel.createuser = "";
      this.search();
    },
    async handleCheck(ispermit, ano){
      if(ispermit === 0){
        let res = await alg.ispermit(ano);
        if (res.success){
          this.$message.success(res.message);
          await this.search();
        }else {
          this.$message.error(res.message);
        }
      }else {
        this.$message.success("已审核");
      }
    }
  }
}

</script>

<style lang="scss">

</style>
