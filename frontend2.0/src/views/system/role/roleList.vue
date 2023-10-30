<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form ref="searchForm" :model="searchModel" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input v-model="searchModel.rolename" placeholder="请输入角色名称"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="search(pageNo, pageSize)">查询</el-button>
        <el-button icon="el-icon-refresh-right" @click="restTable()" >重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openAddWindow()">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 数据表格 -->
    <el-table :data="roleList" :height="tableHeight" border stripe style="width: 100%; margin-bottom: 10px">
      <el-table-column align="center" prop="id" label="角色编号" width="100"></el-table-column>
      <el-table-column prop="name" label="角色名称"></el-table-column>
      <el-table-column prop="code" label="角色编码"></el-table-column>
      <el-table-column prop="remark" label="角色备注" width="100"></el-table-column>
      <el-table-column label="操作" align="center" width="290">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="warning" icon="el-icon-setting" size="small" @click="assignRole(scope.row)">分配权限</el-button>"
        </template>
      </el-table-column>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNo"
        :page-sizes="[100, 200, 300, 400]"
        :page-size="100"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-table>

  </el-main>
</template>

<script>
import {getRoles, addRole, updateRole, deleteRole, checkRole} from '@/api/role'
import systemDialog from "@/components/system/systemDialog.vue";
import menu from "@/api/menu";
  export default {
    name:"roleList",
    components:{
      systemDialog
    },
    data(){
      return{
        //查询条件
        searchModel:{
          rolename:"",
          pageNo:1,
          pageSize:10,
          userId:this.$store.getters.userId //当前登录用户ID

        },
        roleList:[],
        tableHeight:0,
        pageNo: 1,
        total:0,
        pageSize: 10,
        rules: {
          roleName:[{ required: true, message: '请输入角色名称', trigger: 'blur' }],
          roleCode:[{ required: true, message: '请输入角色编码', trigger: 'blur' }]
        },
        roleDialog: {
          title:"",
          visible:false,
          height:230,
          width:500
        },
        role: {
          id:"",
          roleCode: "",
          roleName: "",
          remark: "",
          creatUser: this.$store.getters.userId
          }
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
    methods:{
      async search(pageNo = 1, pageSize = 10){
        //修改当前页码和当前每页数量
        this.searchModel.pageNo = pageNo;
        this.searchModel.pageSize = pageSize;
        //发送查询请求
        let res = await getRoles(this.searchModel);
        if(res.success){
          this.roleList = res.data.records;
          this.total = res.data.total;

        }
      },
      handleEdit(row){
        //数据回显
        this.$objCopy(row, this.role);
        this.roleDialog.title = "编辑角色";
        this.roleDialog.visible = true;

      },
      async handleDelete(row) {
        //判断角色是否被占用
        let res = await checkRole({id: row.id});
        if (!res.success) {
          //提示不能删除
          this.$message.warning(res.message);
        } else {
          let confirm = await this.$myconfirm("确定要删除吗？");
          if (confirm) {
            //发送删除请求
            let res = await deleteRole({id: row.id});
            //判断是否成功
            if (res.success) {
              this.$message.success(res.message);
              await this.search(this.pageNo, this.pageSize);
            } else {
              this.$message.error(res.message);
            }
          }
        }
      },
      assignRole(row){

      },
      handleSizeChange(size) {
        this.pageSize = size;
        this.search(this.pageNo, size);
      },
      handleCurrentChange(page) {
        this.pageNo = page
        this.search(page, this.pageSize);
      },
      restTable(){
        this.searchModel.rolename = "";
        this.search();
      },
      openAddWindow(){
        this.$resetForm("roleForm", this.role);
        this.roleDialog.title = "新增角色";
        this.roleDialog.visible = true;

      },
      onClose(){
        this.role.visible = false;
      },
      onConfirm(){
        //表单验证
        this.$refs.roleForm.validate(async (valid) => {
          if (valid) {
            let res = null;
            //判断当前是新增还是修改
            if (this.role.id === "") {
              //发生添加请求
              res = await addRole(this.role)
            } else {
              //发生修改请求
              res = await updateRole(this.role)
            }

            if (res.success) {
              this.$message.success(res.message);
              await this.search(this.pageNo, this.pageSize);
              this.roleDialog.visible = false;
            } else {
              this.$message.error(res.message);

            }

          }
        })
      },

    }
  }
</script>

<style scoped lang="scss">

</style>
