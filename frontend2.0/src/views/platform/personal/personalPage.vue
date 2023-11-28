<template>

  <el-container>
    <el-main>
      <el-card shadow="hover" @click="handleEdit">
        <el-row :gutter="5">
          <el-col :span="4">
            <div class="demo-basic--circle">
              <div class="block"><el-avatar :size="100" :src=avatar></el-avatar></div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="usertext">用户名：{{ this.username }}</div>
            <div class="usertext">权限：{{ this.roles[0]}}，{{this.roles[1]}}</div>
            <el-button class="edit" type="primary" icon="el-icon-edit" size="small" @click="handleEdit()" >修改个人信息</el-button>
          </el-col>
        </el-row>
      </el-card>
      <el-tabs type="border-card" style="margin-top: 20px">
        <el-tab-pane label="我的发布">
          <el-table :height="tableHeight" :data="algList" border stripe style="width: 100%; margin-bottom: 10px">
            <el-table-column prop="algname" label="算法名称"></el-table-column>
            <el-table-column prop="remark" label="算法概要"></el-table-column>
            <el-table-column prop="ispermit" label="审核状态" align="center">
              <template v-slot="scope" align="center">
                <el-tag size="normal" type="success" v-if="scope.row.ispermit === 1">已通过</el-tag>
                <el-tag size="normal" type="warning" v-else-if="scope.row.ispermit === 0">未通过</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="algtype" label="算法类型"></el-table-column>
            <el-table-column align="center" width="290" label="算法内容">
              <el-button type="primary" icon="el-icon-edit" size="small" @click="openView">点击查看</el-button>
            </el-table-column>
            <el-table-column align="center" width="290" label="操作">
              <template v-slot="scope">
                <el-button type="primary" icon="el-icon-edit" size="small" @click="handleAlgEdit(scope.row)">编辑</el-button>
                <el-button type="danger" icon="el-icon-delete" size="small" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="我的粉丝">
          我的粉丝
        </el-tab-pane>
        <el-tab-pane label="账号安全">
          账号安全
        </el-tab-pane>

      </el-tabs>

<!--修改个人信息-->
      <system-dialog :title="userDialog.title" :visible="userDialog.visible" :width="userDialog.width" :height="userDialog.height"
                     @onClose="onClose()" @onConfirm="onConfirm()">
        <div slot="content">
          <el-form ref="userForm" :model="user" :rules="rules" label-width="80px" :inline="true" size="small">
            <el-form-item label="用户名" prop="username" size="small">
              <el-input v-model="user.username"></el-input>
            </el-form-item>
<!--            <el-form-item label="密码" size="small" prop="password">-->
<!--              <el-input type="password" v-model="user.password"></el-input>-->
<!--            </el-form-item>-->
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
                :before-upload="beforeAvatarUpload"
                :data="uploadHeader">
                <img v-if="user.avatar" :src="user.avatar" class="avatar" alt="">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
<!--            <div class="usertext">如需重置密码请联系管理员</div>-->
          </el-form>
        </div>
      </system-dialog>

<!--查看算法-->
      <system-dialog :data="algContext" :title="algDialog.title" :visible="algDialog.visible" :width="algDialog.width" :height="algDialog.height"
                     @onClose="closeView()" @onConfirm="closeView()">
        <div slot="content">
          <pre>
            <code>
              {{algContext}}
            </code>
          </pre>
        </div>
      </system-dialog>

    </el-main>
    <el-footer>Footer</el-footer>
  </el-container>


</template>

<script>
import systemDialog from "@/components/system/systemDialog.vue";
import user from "@/api/user";
import {getToken} from "@/utils/auth";
import * as alg from "@/api/alg";


export  default {
  name:"personalPage",
  components: {systemDialog},
  data(){
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
    return{
      searchModel: {
        algname: "",
        pageNow: 1,
        pageSize: 10,
        unonow:this.$store.getters.uno,
      },
      algContext:"",
      algList:[],
      tableHeight: 0,
      avatar:"",
      uno:this.$store.getters.uno,
      username:this.$store.getters.username,
      roles:"",
      userDialog: {
        title: "",
        visible: false,
        height: 500,
        width: 100
      },
      algDialog: {
        title: "查看算法内容",
        visible: false,
        height: 500,
        width: 100
      },
      uploadHeader: {"token": getToken()},
      user: {
        uno: "",
        username: "",
        realname: "",
        phone: "",
        email: "",
        avatar: "",
      },
      rules: {
        username: [{trigger: 'blur', message: "请填写用户名"}],
        password: [{trigger: 'blur', message: "请填写密码"}],
        realname: [{trigger: 'blur', message: "请填写真实姓名"}],
        phone: [{trigger: 'blur', validator: phoneCheck}],
        email:[{trigger: 'blur', validator: emailCheck}],
      },
    }
  },
  mounted() {
    this.search();
    this.getAvatar();
    this.getRolename();
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 400;
    })
  },
  methods:{
    async search(pageNow = 1, pageSize = 10) {
      //修改当前页码和当前每页数量
      this.searchModel.pageNow = pageNow;
      this.searchModel.pageSize = pageSize;
      //发送查询请求
      let res = await alg.getAlgList(this.searchModel);
      if (res.success) {
        this.algList = res.data.records
        this.total = res.data.total
        this.algContext = res.data.records.context;
      }
    },
    async getAvatar(){
      let res = null;
      res = await user.getAvatar(this.uno);
      if(res.success){
        this.avatar = res.data;
      }else {
        this.avatar = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png";
      }
    },
    async getRolename(){
      let res = null;
      res = await user.getRolename(this.uno);
      if(res.success){
        this.roles = res.data;
      }

    },
    handleAvatarSuccess(res, file) {
      this.user.avatar = res.data;
      this.$forceUpdate();
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    onClose() {
      this.userDialog.visible = false;
      this.resetUser();
    },
    onConfirm() {
      this.$refs.userForm.validate(async (valid) => {
        if (valid) {
          let res = null;
          res = await user.editUser(this.user);
          if (res.success) {
            this.userDialog.visible = false;
            this.resetUser();
          } else {
            this.$message.error(res.message);
          }
        }
      })
    },
    handleEdit() {
      this.userDialog.title = "修改个人信息";
      this.userDialog.visible = true;
      this.resetUser();
    },
    resetUser(){
      this.user.uno=this.$store.getters.uno;
      this.user.username=this.$store.getters.username;
      this.user.realname=this.$store.getters.realname;
      this.user.phone=this.$store.getters.phone;
      this.user.email=this.$store.getters.email;
      this.user.avatar=this.$store.getters.avatar;
    },
    handleAlgEdit(row) {
      this.userDialog.title = "编辑用户";
      this.userDialog.visible = true;
      //把当前编辑的数据复制到表单数据域，回显数据
      this.$objCopy(row, this.user);
    },
    async handleDelete(row) {
      //提示是否确认删除
      let confirm = await this.$myconfirm("确定删除该用户吗？");
      if (confirm) {
        console.log(row.uno)
        let res = user.deleteUser(row.uno);
        console.log(res.success)
        if (res.success) {
          this.$message.success(res.message);
          await this.search(this.pageNow, this.pageSize);
        } else {
          this.$message.error(res.message);

        }
      }
    },
    openView(){
      this.algDialog.visible = true;
    },
    closeView(){
      this.algDialog.visible = false;
    },
  }
}

</script>


<style lang="scss">
.usertext {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  margin-top: 10px;
  margin-left: 20px;
}

.demo-basic--circle {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100px;
  width: 200px;
  font-size: 28px;

}
.edit{
  margin-top: 10px;
  margin-left: 20px;
}
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
