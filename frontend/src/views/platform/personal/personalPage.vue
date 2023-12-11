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
          <el-tabs style="height: 633px;"  type="card">
            <el-tab-pane label="我的算法">
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
                <el-table-column prop="lang" label="算法语言"></el-table-column>
                <el-table-column align="center" width="290" label="算法内容">
                  <template v-slot="scope" align="center">
                    <el-button type="primary" icon="el-icon-edit" size="small" @click="openView(scope.row.ano)">点击查看</el-button>
                  </template>
                </el-table-column>

                <el-table-column align="center" width="290" label="操作">
                  <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" size="small" @click="handleAlgEdit(scope.row)">编辑</el-button>
                    <el-button type="danger" icon="el-icon-delete" size="small" @click="handleDelete(scope.row)">删除</el-button>
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
                :total="total" style="margin-top: 10px">
              </el-pagination>
            </el-tab-pane>

            <el-tab-pane label="我的文章">
              <el-table :height="tableHeight" :data="articleList" border fit highlight-current-row style="width: 100%" >
                <el-table-column min-width="100px" prop="title" label="文章标题" align="center"></el-table-column>
                <el-table-column width="300px" label="文章概要" prop="contentshort"></el-table-column>
                <el-table-column label="文章状态" align="center">
                  <template v-slot="scope" >
                    <el-tag size="normal" type="warning" v-if="scope.row.status === 'draft'" >草稿</el-tag>
                    <el-tag size="normal" type="primary" v-if="scope.row.status === 'published'">已发布</el-tag>
                    <el-tag size="normal" type="success" v-if="scope.row.status === 'enable'">已通过</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="是否开启评论" align="center">
                  <template v-slot="scope">
                    <el-button type="success" v-if="scope.row.commentdisable === 0" round size="small">已打开</el-button>
                    <el-button type="danger" v-if="scope.row.commentdisable === 1" round size="small">未打开</el-button>
                  </template>
                </el-table-column>
                <el-table-column min-width="100px" label="查看文章" align="center">
                  <template v-slot="scope">
                    <span class="myLink" @click="upto(scope.row.id, scope.row.author)">查看文章</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="发布日期" prop="displaytime"></el-table-column>
                <el-table-column align="center" label="操作" width="200">
                  <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" size="small" @click="openAddWindow(scope.row.id)">编辑</el-button>
                    <el-button type="danger" icon="el-icon-delete" size="small" @click="handleArticleDelete(scope.row)">删除</el-button>
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
                :total="total" style="margin-top: 10px">
              </el-pagination>
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
        <el-tab-pane label="我的粉丝">
          <el-table :height="tableHeight" :data="followsList" border stripe style="width: 100%; margin-bottom: 10px">
            <el-table-column prop="fno" label="粉丝ID"></el-table-column>
            <el-table-column prop="fname" label="粉丝名称"></el-table-column>
            <el-table-column prop="date" label="关注时间"></el-table-column>
            <el-table-column align="center" width="290" label="操作">
              <template v-slot="scope">
                <el-button type="danger" icon="el-icon-delete" size="small" @click="handleFollowsDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
<!--账号安全-->
        <el-tab-pane label="账号安全">
          <el-tabs style="height: 633px;"  type="card">
            <el-tab-pane label="修改密码">
              <el-form  class="signup-form" ref="emailForm" :model="userEmail" :rules="rules" label-width="80px" :inline="true" size="small" label-position="left">
                <el-row>
                  <el-col >
                    <el-form-item label="密码" size="small" prop="password">
                      <el-input type="password" v-model="userEmail.password" style="width: 200px"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col >
                    <el-form-item label="确认密码" size="small" prop="password">
                      <el-input type="password" v-model="userEmail.confirmPassword" style="width: 200px"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col >
                    <el-form-item label="邮箱" size="small" prop="email">
                      <el-input  v-model="userEmail.email" style="width: 200px"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col>
                    <el-form-item label="验证码" size="small" prop="code">
                      <el-input  v-model="userEmail.code" style="width: 200px"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="35">
                  <el-col :span="9">
                    <el-button type="primary" size="medium" style="height: 32px" class="ml-5" @click="onSendCode">获取验证码</el-button>
                  </el-col >
                  <el-col :span="6">
                    <el-button type="primary" size="medium" style="height: 32px" class="ml-5" @click="onPassword">确认修改</el-button>
                  </el-col >
                </el-row>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="换绑邮箱">换绑邮箱</el-tab-pane>
          </el-tabs>
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

<!--修改算法-->
      <system-dialog :title="algEditDialog.title" :visible="algEditDialog.visible" :width="algEditDialog.width" :height="algEditDialog.height"
                     @onClose="onEditClose()" @onConfirm="onEditConfirm()">
        <div slot="content">
          <el-form ref="editForm" :model="algEdit"  label-width="80px" :inline="true" size="small">
            <el-form-item label="算法名称" prop="username" size="small">
              <el-input v-model="algEdit.algname"></el-input>
            </el-form-item>
            <el-form-item label="算法概要" size="small" prop="realname" >
              <el-input  v-model="algEdit.remark"></el-input>
            </el-form-item>
            <el-form-item label="算法类型" size="small" prop="phone" >
              <el-input  v-model="algEdit.algtype" ></el-input>
            </el-form-item>
            <el-form-item label="算法语言" size="small" prop="email" >
              <el-input  v-model="algEdit.lang" ></el-input>
            </el-form-item>
            <el-form-item label="算法：" size="small" prop="email" >
              <CommonEditor
                :read-only="false"
                :value="algEdit.context"
                :language="algEdit.lang"
                @input="changeTextareaEdit"
                style="height: auto"
              ></CommonEditor>
            </el-form-item>
          </el-form>
        </div>
      </system-dialog>
    </el-main>
    <el-footer>Footer</el-footer>
  </el-container>


</template>

<script>
import systemDialog from "@/components/system/systemDialog.vue";
import user, {activationPassword, sendCode, signup, updatePasswordByEmail} from "@/api/user";
import {getToken} from "@/utils/auth";
import * as alg from "@/api/alg";
import CommonEditor from "@/views/CommonEditor/CommonEditor.vue";
import * as follows from "@/api/follows";
import * as article from "@/api/article";
export  default {
  name:"personalPage",
  components: {CommonEditor, systemDialog},
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
      followsList:[],
      searchModelFollows: {
        pageNow: 1,
        pageSize: 10,
        uno:this.$store.getters.uno,
      },
      searchModel: {
        algname: "",
        pageNow: 1,
        pageSize: 10,
        createuser: this.$store.getters.username,
      },
      pageNow: 1,
      pageSize: 10,
      total:0,
      articleList: [],
      searchModelArticle: {
        author: this.$store.getters.username,
        pageNow: 1,
        pageSize: 10
      },
      algContext:{
        context: "",
        lang:""
      },
      algList:[],
      ano:"",
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
      algEditDialog: {
        title: "编辑算法",
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
      userEmail: {
        id: this.$store.getters.uno,
        password:"",
        confirmPassword:"",
        email:"",
        code:"",

      },
      algEdit: {
        ano: "",
        algname: "",
        remark: "",
        algtype: "",
        lang: "",
        context:""
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
  created() {
    this.search();
  },
  mounted() {
    this.getAvatar();
    this.getRolename();
    this.$nextTick(() => {
      this.tableHeight = window.innerHeight - 450;
    })
  },
  methods:{
    upto(id, author){
      this.$router.push({
        path:"../article/view",
        query:{
          id:id,
          author:author
        }
      })
    },
    openAddWindow(id) {
      this.$router.push({
        path: "../article/edit",
        query:{
          id:id,
        }
      })
    },
    async handleFollowsDelete(row) {
      //提示是否确认删除
      let confirm = await this.$myconfirm("确定删除该粉丝吗？");
      if (confirm) {
        let param = {
          uno:row.uno,
          fno:row.fno
        }
        let res = follows.deleteFollows(param);
        if (res.success) {
          this.$message.success(res.message);
          await this.search(this.pageNow, this.pageSize);
        } else {
          this.$message.error(res.message);
        }
      }
    },
    async onPassword(){
      let para = {"email":this.userEmail.email,"code":this.userEmail.code};
      let res = await activationPassword(para);
      if (res.success) {
        this.$message.success(res.message);
      } else {
        this.$message.error(res.message);
      }

    },
    async onSendCode() {
      let para = Object.assign({}, this.userEmail);
      let res = null;
      res = await updatePasswordByEmail(para);
      if (res.success) {
        this.$message.success(res.message);
      } else {
        this.$message.error(res.message);
      }
    },
    changeTextareaEdit(val) {
      this.algEdit.context = val
    },
    changeTextareaView(val) {
      this.algContext.context = val
    },
    onCopy(){
      this.$message.success("复制成功！");
    },
    async search(pageNow = 1, pageSize = 10) {
      //修改当前页码和当前每页数量
      this.searchModel.pageNow = pageNow;
      this.searchModel.pageSize = pageSize;
      //发送查询请求
      let res1 = await alg.getAlgListM(this.searchModel);
      if (res1.success) {
        this.algList = res1.data.records
        this.total = res1.data.total
      }
      let res2 = await follows.getFollowsList(this.searchModelFollows);
      if (res2.success) {
        this.followsList = res2.data.records
        this.total = res2.data.total
      }
      let res3 = await article.getArticleListU(this.searchModelArticle);
      if(res3.success){
        this.articleList = res3.data.records;
        this.total = res3.data.total;
        console.log(this.articleList)
      }
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.search(this.pageNow, size);
    },
    handleCurrentChange(page) {
      this.pageNow = page
      this.search(page, this.pageSize);
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
    onEditClose() {
      this.algEditDialog.visible = false;
    },
    onEditConfirm() {
      this.$refs.editForm.validate(async (valid) => {
        if (valid) {
          let res = null;
          res = await alg.updateAlg(this.algEdit);
          if (res.success) {
            this.algEditDialog.visible = false;
            await this.search();
            this.$message.success(res.message);
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
      //把当前编辑的数据复制到表单数据域，回显数据
      this.$objCopy(row, this.algEdit);
      this.algEditDialog.visible = true;
    },
    async handleDelete(row) {
      //提示是否确认删除
      let confirm = await this.$myconfirm("确定删除该算法吗？");
      if (confirm) {
        let res = alg.deleteAlg(row.ano)
        if (res.success) {
          this.$message.success(res.message);
          await this.search(this.pageNow, this.pageSize);
        } else {
          this.$message.error(res.message);

        }
      }
    },
    async handleArticleDelete(row) {
      //提示是否确认删除
      let confirm = await this.$myconfirm("确定删除该算法吗？");
      if (confirm) {
        let res = article.deleteArticle(row.id)
        if (res.success) {
          this.$message.success(res.message);
          await this.search(this.pageNow, this.pageSize);
        } else {
          this.$message.error(res.message);

        }
      }
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
    closeView(){
      this.algDialog.visible = false;
    },
  },
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
pre {
  position: relative;
  min-width: 600px;
  margin: 0;
}
.hljs {
  display: block;
  width: 100%;
  margin: 0;
  padding: 1px;
  color: #abb2bf;
  font-weight: 200;
  font-size: 0.75rem;
  font-family: Menlo, Monaco, Consolas, Courier, monospace;
  line-height: 1.5;
  white-space: pre-wrap;
  border: 0;
}
.hljs ol {
  margin: 0 0 0 35px;
  padding: 0;
  list-style-type: decimal;
}
.hljs ol li {
  // padding: 5px;
  // padding-left: 10px;
  color: #abb2bf;
  white-space: pre;
  // list-style: decimal-leading-zero;
  list-style-position: outside;
  border-left: 1px solid #c5c5c5;
}
.signup-form {
  position: relative;
  width: 520px;
  max-width: 100%;
  padding: 20px 35px 0;
  margin: 2px auto;
  overflow: hidden;

}
.myLink:hover {
  color: #20a0ff;
}

</style>
