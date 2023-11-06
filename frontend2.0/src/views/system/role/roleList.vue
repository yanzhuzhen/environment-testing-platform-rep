<template>
  <el-main>
    <!-- 查询条件 -->
    <el-form ref="searchForm" :model="searchModel" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input v-model="searchModel.rolename" placeholder="请输入角色名称"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="search(pageNow, pageSize)">查询</el-button>
        <el-button icon="el-icon-refresh-right" @click="restTable()" >重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openAddWindow()" v-if="hasPermission('system:role:add')">新增</el-button>
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
          <el-button type="primary" icon="el-icon-edit" size="small" @click="handleEdit(scope.row)" v-if="hasPermission('system:role:update')">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="handleDelete(scope.row)" v-if="hasPermission('system:role:delete')">删除</el-button>
          <el-button type="warning" icon="el-icon-setting" size="small" @click="assignRole(scope.row)">分配权限</el-button>"
        </template>
      </el-table-column>
      <!-- 分页  -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNow"
        :page-sizes="[100, 200, 300, 400]"
        :page-size="100"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
      <!-- 添加和修改角色 -->
      <system-dialog :title="roleDialog.title" :visible="roleDialog.visible" :width="roleDialog.width" :height="roleForm.height"
      @onClose="onClose()" @onConfirm="onConfirm()">
      <div slot="content">
        <el-form ref="role" :model="roleForm" :rules="rules" label-width="80px" :inline="false" size="small">
          <el-form-item label="角色编码" size="small" prop="roleCode">
            <el-input v-model="roleForm.roleCode"></el-input>
          </el-form-item>
          <el-form-item label="角色名称" size="small" prop="roleName">
            <el-input v-model="roleForm.roleName"></el-input>
          </el-form-item>
          <el-form-item label="角色描述" size="small" prop="roleName" >
            <el-input type="textarea"  v-model="roleForm.roleName" :rows="5"></el-input>
          </el-form-item>

        </el-form>
      </div>
      </system-dialog>

      <!-- 分配权限窗口 -->
      <system-dialog :title="roleDialog.title" :visible="roleDialog.visible" :width="roleDialog.width" :height="roleDialog.height"
                     @onClose="onAssignClose()" @onConfirm="onAssignConfirm()">
        <div slot="content">
          <el-tree ref="assignTree" :data="assignTreeData" node-key="id" :props="defaultProps" empty-text="暂无数据"
                   show-checkbox="true" :highlight-current="true" default-expand-all>
          </el-tree>
        </div>
      </system-dialog>
    </el-table>

  </el-main>
</template>

<script>
import {addRole, checkRole, deleteRole, getAssignMenuTree, getRoles, updateRole, saveAssign} from '@/api/role'
import systemDialog from "@/components/system/systemDialog.vue";
import myIcon from "@/components/system/myIcon.vue";
import leafUtils from "@/utils/leaf";
import row from "element-ui/packages/row";
import hasPermission from "@/permission";

export default {
    name:"roleList",
    components:{
      myIcon,
      systemDialog
    },
    data(){
      return{
        //查询条件
        searchModel:{
          rolename:"",
          pageNow:1,
          pageSize:10,
          userId:this.$store.getters.userId //当前登录用户ID

        },
        roleList:[],
        tableHeight:0,
        pageNow: 1,
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
        roleForm: {
          id:"",
          roleCode: "",
          roleName: "",
          remark: "",
          creatUser: this.$store.getters.userId
          },
        //分配权限窗口属性
        assignDialog:{
          title:"",
          visible:false,
          height:450,
          width:300
        },

        rno:"",
        assignTreeData:[], //树节点数据
        //树节点属性
        defaultProps:{
          children:"children",
          label:"label"
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
      hasPermission,
      async search(pageNow = 1, pageSize = 10){
        //修改当前页码和当前每页数量
        this.searchModel.pageNow = pageNow;
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
              await this.search(this.pageNow, this.pageSize);
            } else {
              this.$message.error(res.message);
            }
          }
        }
      },
      //分配权限
      async assignRole(row) {

        let params = {
          rno: row.id,
          uno: this.$store.getters.userId
        }
        //发送查询权限分配菜单的请求
        let res = await getAssignMenuTree(params);
        //判断是否成功,如果成功则获取当前登录用户的菜单权限
        if(res.success){
          //获取当前登录用户所有的菜单权限
          let menuList = res.data.menuList;
          //获取当前用户被分配角色已经拥有的菜单权限
          let {checkList} = res.data;
          //判断当前菜单是否为最后一级
          let {setLeaf} = leafUtils();
          //设置菜单列表
          //设置树节点数据
          this.assignTreeData = setLeaf(menuList);
          //将回调延迟到一下次DOM更新循环之后，再修改数据之后立即调用该方法，然后等待DOM更新
          this.$nextTick(() => {
            //获取菜单节点数据
            let nodes = this.$refs.assignTree.children;
            //设置子节点
            this.setChild(nodes, checkList);
          })

        }

        this.roleDialog.title = `给[${row.roleName}]分配权限`;
        this.roleDialog.visible = true;

      },
      handleSizeChange(size) {
        this.pageSize = size;
        this.search(this.pageNow, size);
      },
      handleCurrentChange(page) {
        this.pageNow = page
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
        this.$refs.role.validate(async (valid) => {
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
              await this.search(this.pageNow, this.pageSize);
              this.roleDialog.visible = false;
            } else {
              this.$message.error(res.message);

            }

          }
        })
      },
      onAssignClose(){
        this.role.visible = false;
      },
      async onAssignConfirm(){
        this.rno = row.id;
        //获取选中的树节点的key
        let ids = this.$refs.assignTree.getCheckedKeys();
        //获取选中节点的父节点id
        let pids = this.$refs.assignTree.getHalfCheckedKeys();
        //组装选中节点的id
        let listId = ids.concat(pids);
        let params = {
          list: listId,
          rno: this.rno,
        }
        //发送请求
        let res = await saveAssign(params);
        if(res.success){
          this.assignDialog.visible = false;
          this.$message.success(res.message);
        }else {
          this.$message.error(res.message);
        }

      },
      //设置子节点
      setChild(childNodes, checkList) {
        if(childNodes && childNodes.length >0){
          for (let i = 0; i < childNodes.length; i++) {
            //根据data或者key获取树组件中的node节点
            let node = this.$refs.assignTree.getNode(childNodes[i]);
            //判断是否已经拥有对应的角色权限
            if(checkList && checkList.length>0){
              for (let j = 0; j < checkList.length; j++) {
                //判断是否拥有该权限
                if(childNodes[i].id === checkList[j]){
                  //设置选中
                  if(childNodes[i].open){
                    this.$refs.assignTree.setChecked(node, true);
                    break;
                  }
                }
              }
            }
            //判断如果存在子节点，则递归选择
            if(childNodes[i].children ){
              this.setChild(childNodes[i].children, checkList);
            }
          }
        }
      },
    }
  }
</script>

<style scoped lang="scss">

</style>
