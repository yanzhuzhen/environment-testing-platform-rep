<template>
  <div class="login-container">
  <!--登录表单-->
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">西南地区国土环境检测学习平台</h3>
      </div>
      <!--用户名-->
      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="请输入你的用户名"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
        />
      </el-form-item>
      <!--密码-->
      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="请输入你的密码"
            name="password"
            tabindex="2"
            autocomplete="on"
            @blur="capsTooltip = false"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
      </el-tooltip>
      <el-row :gutter="20">
        <el-col :span="16">
          <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
        </el-col>
        <el-col :span="8">
          <el-button :loading="loading" style="width:100%;margin-bottom:30px;" @click="openAddWindow()">注册</el-button>
        </el-col>
      </el-row>
    </el-form>

    <system-dialog :title="userDialog.title" :visible="userDialog.visible" :width="userDialog.width" :height="userDialog.height"
                   @onClose="onClose()" @onConfirm="onConfirm()" style="margin-top: 200px">
      <div slot="content">

        <el-form ref="userForm" :model="user" :rules="rules" label-width="80px" :inline="true" size="small">
          <el-row>
            <el-col :span="24">
              <el-form-item label="用户名" prop="username" size="small">
                <el-input v-model="user.username"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="密码" size="small" prop="password">
                <el-input type="password" v-model="user.password"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="电话" size="small" prop="phone">
                <el-input  v-model="user.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </system-dialog>

<!--    <el-dialog title="Or connect with" :visible.sync="showDialog">-->
<!--      Can not be simulated on local, so please combine you own business simulation! ! !-->
<!--      <br>-->
<!--      <br>-->
<!--      <br>-->
<!--      <social-sign />-->
<!--    </el-dialog>-->
  </div>
</template>

<script>
// import SocialSign from './components/SocialSignin'

import systemDialog from "@/components/system/systemDialog.vue";
import {signup} from "@/api/user";

export default {
  name: 'Login',
  components: {systemDialog},
  // components: { SocialSign },
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
    return {
      //登录表单对象
      loginForm: {
        username: '',
        password: '',
      },
      //表单验证规则
      loginRules: {
        username: [{ required: true, trigger: 'blur', message:"请输入正确的用户名" }],
        password: [{ required: true, trigger: 'blur', message:"请输入正确的密码" }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {},
      userDialog: {
        title: "",
        visible: false,
        height: 400,
        width: 100
      },
      user: {
        id: "",
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
        phone: [{trigger: 'blur', validator: phoneCheck}],

      },
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  mounted() {
    //如果用户名为空，则焦点聚焦到用户名输入框
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
      //如果密码为空，则焦点聚焦到密码输入框
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },

  methods: {
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
    openAddWindow() {
      this.$resetForm('userForm', this.user);
      this.userDialog.title = "注册";
      this.userDialog.visible = true;
    },
    onClose() {
      this.userDialog.visible = false;
    },
    onConfirm() {
      this.$refs.userForm.validate(async (valid) => {
        if (valid) {
          let res = null;
            res = await signup(this.user);
          if (res.success) {
            this.$message.success(res.message);
            this.userDialog.visible = false;
          } else {
            this.$message.error(res.message);
          }
        }
      })
    },
    //是否显示密码
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    //处理登录请求
    handleLogin() {
      //进行表单验证
      this.$refs.loginForm.validate(valid => {
        //如果valid为true即表单验证通过
        if (valid) {
          this.loading = true //显示加载动画
          //调用vuex中的user/login方法，发送请求
          this.$store.dispatch("user/login", this.loginForm)
            .then(() => {
              console.log("login success")
              console.log("login success"+this.redirect);
              // this.$router.push({ path: '/system'})
              this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
              this.loading = false
              console.log("login success 2"+this.redirect);
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
    // afterQRScan() {
    //   if (e.key === 'x-admin-oauth-code') {
    //     const code = getQueryObject(e.newValue)
    //     const codeMap = {
    //       wechat: 'code',
    //       tencent: 'code'
    //     }
    //     const type = codeMap[this.auth_type]
    //     const codeName = code[type]
    //     if (codeName) {
    //       this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
    //         this.$router.push({ path: this.redirect || '/' })
    //       })
    //     } else {
    //       alert('第三方登录失败')
    //     }
    //   }
    // }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
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
