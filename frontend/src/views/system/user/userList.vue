<template>
  <el-main>
    <el-form :model="searchModel" ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-row :gutter="24">
          <el-col :span="8"><el-input v-model="searchModel.username" placeholder="请输入用户名查询"/></el-col>
          <el-col :span="8"><el-input v-model="searchModel.realname" placeholder="请输入真实姓名查询"/></el-col>
          <el-col :span="8"><el-input v-model="searchModel.phone" placeholder="请输入联系电话查询"/></el-col>
        </el-row>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" type="primary" @click="search(pageNow, pageSize)">查询</el-button>
        <el-button icon="el-icon-delete"  @click="resetValue()">重置</el-button>
        <el-button icon="el-icon-plus" type="success" size="small" @click="openAddWindow()" v-if="hasPermission('system:user:add')">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table :height="tableHeight" :data="userList" border stripe style="width: 100%; margin-bottom: 10px">
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="realname" label="真实姓名"></el-table-column>
      <el-table-column prop="phone" label="联系电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column align="center" width="290" label="操作">
        <template v-slot="scope">
          <el-button type="primary" icon="el-icon-edit" size="small" @click="handleEdit(scope.row)" v-if="hasPermission('system:user:update')">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="handleDelete(scope.row)" v-if="hasPermission('system:user:delete')">删除</el-button>
          <el-button type="warning" icon="el-icon-setting" size="small" @click="assignRole(scope.row)">分配角色</el-button>
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
<!--    修改角色-->
    <system-dialog :title="userDialog.title" :visible="userDialog.visible" :width="userDialog.width" :height="userDialog.height"
                   @onClose="onClose()" @onConfirm="onConfirm()">
      <div slot="content">
        <el-form ref="userForm" :model="user" :rules="rules" label-width="80px" :inline="true" size="small">
          <el-form-item label="用户名" prop="username" size="small">
            <el-input v-model="user.username"></el-input>
          </el-form-item>
          <el-form-item label="密码" size="small" prop="password" v-if="user.uno === ''">
            <el-input type="password" v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item label="真实姓名" size="small" prop="realname" >
            <el-input  v-model="user.realname"></el-input>
          </el-form-item>
          <el-form-item label="电话" size="small" prop="phone" >
            <el-input  v-model="user.phone" ></el-input>
          </el-form-item>
          <el-form-item label="邮箱" size="small" prop="email" >
            <el-input  v-model="user.email" ></el-input>
          </el-form-item>
          <el-form-item label="用户头像">
            <el-upload
              class="avatar-uploader"
              action="http://localhost:3000/api/oss/file/upload?module=avatar"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :on-remove="handleAvatarRemove"
              :before-upload="beforeAvatarUpload"
              :headers="uploadHeader">
              <img v-if="user.avatar" :src="user.avatar" class="avatar" alt="">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>
    </system-dialog>
<!-- 分配角色窗口-->
    <system-dialog :title="assignDialog.title" :height="assignDialog.height" :width="assignDialog.width" :visible="assignDialog.visible" @onClose="onAssignClose" @onConfirm="onAssignConfirm">
    <div slot="content">
<!-- 分配角色数据列表-->
      <el-table ref="assignRoleTable" :data="assignRoleList" border stripe :height="assignHeight" style="width: 100%;margin-bottom: 10px" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="rolecode" label="角色编码"/>
        <el-table-column prop="rolename" label="角色名称"/>
        <el-table-column prop="remark" label="角色描述"/>
      </el-table>
<!--分页工具栏-->
      <el-pagination
        align="center"
        @size-change="assignSizeChange"
        @current-change="assignCurrentChange"
        :currentypage.sync="roleVo.pageNow"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="roleVo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="roleVo.total"
        background>
      </el-pagination>
    </div>
    </system-dialog>
  </el-main>


</template>

<script>

import systemDialog from "@/components/system/systemDialog.vue";
import {getToken} from "@/utils/auth";
import hasPermission from "@/permission/index";
import user from "@/api/user";
import deleteFile from "@/api/ossFile";
export default {
    name: "userList",
    computed: {
      user() {
        return user
      }
    },
    components: {
      systemDialog,
    },
    data() {
      let phoneCheck = (rule, value, callback) => {
        if (!value) {
          callback(new Error("请输入手机号码"));
        } else if (!/^1[345789]\d{9}$/.test(value)) {
          callback(new Error("请输入正确格式得手机号"));
        } else {
          callback();
        }
      }
      let emailCheck = (rule, value, callback) => {
        if (!value) {
          callback(new Error("请输入邮箱"));
        } else if (!/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(value)) {
          callback(new Error("请输入正确格式的邮箱"));
        } else {
          callback();
        }
      }
      return {
        searchModel: {
          username: "",
          realname: "",
          phone: "",
          email: "",
          pageNow: 1,
          pageSize: 10
        },
        tableHeight: 0,
        userList: [],
        pageNow: 1,
        pageSize: 10,
        total: 0,
        userDialog: {
          title: "",
          visible: false,
          height: 230,
          width: 500
        },
        user: {
          uno: "",
          username: "",
          realname: "",
          phone: "",
          email: "",
          avatar: ""
        },
        rules: {
          username: [{required: true, trigger: 'blur', message: "请填写用户名"}],
          password: [{required: true, trigger: 'blur', message: "请填写密码"}],
          realname: [{required: true, trigger: 'blur', message: "请填写真实姓名"}],
          phone: [{required: true, trigger: 'blur', validator: phoneCheck}],
          email:[{required: true, trigger: 'blur', validator: emailCheck}],
        },
        uploadHeader: {"token": getToken()},
        //分配角色窗口属性
        assignDialog: {
          title: "",
          visible: false,
          width: 800,
          height: 410,
        },
        //角色对象
        roleVo: {
          pageNow: 1,
          pageSize: 10,
          uno: "",
          total: 0,
        },
        assignRoleList: [],//角色列表
        assignHeight: 0,//分配角色表格高度
        selectedIds: [],//被选中的角色id
        selectedUserId: "",//被分配角色的用户ID
      }
    },
    created() {
      this.search();
    },
    mounted() {
      this.$nextTick(() => {
        this.tableHeight = window.innerHeight - 220;
        this.assignHeight = window.innerHeight - 350;
      })

    },
    methods: {
      async handleAvatarRemove(){
         let res = await deleteFile(this.user.avatar);
         if(res.success){
           this.user.avatar = "";
           this.$message.success(res.message);
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
        let res = await user.getUserList(this.searchModel);
        if (res.success) {
          this.userList = res.data.records
          this.total = res.data.total
        }
      },
      resetValue() {
        this.searchModel.username = "";
        this.searchModel.realname = "";
        this.searchModel.phone = "";
        this.search();
      },
      openAddWindow() {
        this.$resetForm('userForm', this.user);
        this.userDialog.title = "新增用户";
        this.userDialog.visible = true;
      },
      onClose() {
        this.userDialog.visible = false;
      },
      onConfirm() {
        this.$refs.userForm.validate(async (valid) => {
          if (valid) {
            let res = null;
            if (this.user.uno === "") {
              res = await user.addUser(this.user);
            } else {
              res = await user.updateUser(this.user);
            }
            if (res.success) {
              this.$message.success(res.message);
              await this.search(this.pageNow, this.pageSize);
              this.userDialog.visible = false;
            } else {
              this.$message.error(res.message);
            }
          }
        })
      },
      handleAvatarSuccess(res, file) {
        this.user.avatar = res.data;
        this.$forceUpdate();
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLtM = file.size / 1024 / 1024 < 4;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLtM) {
          this.$message.error('上传头像图片大小不能超过 4 MB!');
        }
        return isJPG && isLtM;
      },
      handleEdit(row) {
        this.userDialog.title = "编辑用户";
        this.userDialog.visible = true;
        //把当前编辑的数据复制到表单数据域，回显数据
        this.$objCopy(row, this.user);
      },
      async handleDelete(row) {
        //提示是否确认删除
        let confirm = await this.$myconfirm("确定删除该用户吗？");
        if (confirm) {
          let res = await user.deleteUser(row.uno);
          if (res.success) {
            this.$message.success(res.message);
            await this.search(this.pageNow, this.pageSize);
          } else {
            this.$message.error(res.message);

          }
        }
      },
      async assignRole(row) {
        //防止回显出现问题
        this.selectedIds = [];
        //被分配用户的id
        this.selectedUserId = row.uno;
        //显示窗口
        this.assignDialog.visible = true;
        //设置标题
        this.assignDialog.title = `给[${row.username}]分配角色`;
        console.log(row);
        //查询当前登录用户的所有角色信息
        await this.getAssignRoleList();
        console.log("1");
        //发送根据用户ID查询角色列表的请求
        let res = await user.getRnoByUno(row.uno);
        console.log(res);
        //如果存在数据
        if (res.success && res.data) {
          //将查询到的角色ID列表交给选中角色数组
          this.selectedIds = res.data;
          //循环遍历
          this.selectedIds.forEach((key) => {
            this.assignRoleList.forEach((item) => {
              if (item.rno === key) {
                this.$refs.assignRoleTable.toggleRowSelection(item, true);
              }
            });
          });
        }else {
          console.log("error")
        }
      },
      onAssignClose() {
        this.assignDialog.visible = false;
      },
      async onAssignConfirm() {
        //判断当前是否选中角色
        if (this.selectedIds.length === 0) {
          let confirm = await this.$myconfirm("确定要收回所有角色吗？");
          if (confirm) {
            let res = await user.deleteRole(this.selectedUserId);
            if (res.success) {
              this.$message.success(res.message);
            } else {
              this.$message.error(res.message);
            }
          }
        }else{
          let params = {
            uno: this.selectedUserId,
            rnoList: this.selectedIds
          }
          let res = await user.assignRoleSave(params);
          if(res.success){
            this.$message.success(res.message);
            this.assignDialog.visible = false;
          }else {
            this.$message.error(res.message);
          }
        }

        },
        //点击多选框时触发
        handleSelectionChange(rows)
        {
          let roleList = [];
          for (let i = 0; i < rows.length; i++) {
            roleList.push(rows[i].rno);
          }
          this.selectedIds = roleList;
        },
        assignSizeChange(size)
        {
          this.roleVo.pageSize = size;
          this.search(this.roleVo.pageNow, size);
        },
        assignCurrentChange(page)
        {
          this.roleVo.pageNow = page;
          this.search(page, this.roleVo.pageSize);
        },
        async getAssignRoleList(pageNow = 1, pageSize = 10)
        {
          this.roleVo.uno = this.$store.getters.uno;
          this.roleVo.pageNow = pageNow;
          this.roleVo.pageSize = pageSize;
          //发送请求
          let res = await user.getAssignRoleList(this.roleVo);
          if (res.success) {
            this.assignRoleList = res.data.records;
            this.roleVo.total = res.data.total;
          }
        }


      }
  }

</script>

<style lang="scss">
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

</style>
