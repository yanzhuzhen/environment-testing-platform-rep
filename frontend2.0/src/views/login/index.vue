<template>
  <div class="login-container">
    <div style="height: 60px; line-height: 60px; font-size: 20px; padding-left: 50px; color: white;
      background-color: rgba(0,0,0,0.2)">西南地区国土环境监测学习平台</div>
    <div style="margin: 150px auto; background: white; width: 500px; border-radius: 10px; overflow: hidden">
      <div class="title-container">
        <h3 class="title">登录</h3>
      </div>
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" style="margin: 20px auto" class="login-form" autocomplete="on" label-position="left">
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
            <el-button type="text" size="mid"  autocomplete="off" @click="handlePass">忘记密码？</el-button>
            <el-row :gutter="20">
              <el-col :span="16">
                <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
              </el-col>
              <el-col :span="8">
                <el-button :loading="loading" style="width:100%;margin-bottom:30px;" @click="openAddWindow()">注册</el-button>
              </el-col>
            </el-row>
          </el-form>
    </div>
    <system-dialog class="login-container" :title="userDialog.title" :visible="userDialog.visible" :width="userDialog.width" :height="userDialog.height"
                   @onClose="onClose()" @onConfirm="onConfirm()" style="margin-top: 200px ">
      <div slot="content">
        <el-form  class="signup-form" ref="userForm" :model="user" :rules="rules" label-width="80px" :inline="true" size="small" label-position="left">
          <el-row>
            <el-col>
              <el-form-item label="用户名" prop="username" size="small">
                <el-input v-model="user.username" style="width: 200px" ></el-input>
              </el-form-item>
            </el-col>
            <el-col >
              <el-form-item label="密码" size="small" prop="password">
                <el-input type="password" v-model="user.password" style="width: 200px"></el-input>
              </el-form-item>
            </el-col>
            <el-col >
              <el-form-item label="确认密码" size="small" prop="password">
                <el-input type="password" v-model="user.confirmPassword" style="width: 200px"></el-input>
              </el-form-item>
            </el-col>
            <el-col >
              <el-form-item label="电话" size="small" prop="phone">
                <el-input  v-model="user.phone" style="width: 200px"></el-input>
              </el-form-item>
            </el-col>
            <el-col >
              <el-form-item label="邮箱" size="small" prop="email">
                <el-input  v-model="user.email" style="width: 200px"></el-input>
              </el-form-item>
            </el-col>
            <el-col >
              <el-row :gutter="20">
                <el-col :span="16">
                  <el-form-item label="验证码" size="small" prop="code">
                    <el-input  v-model="user.code" style="width: 200px"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-button type="primary" size="medium" style="height: 47px" class="ml-5" @click="onSendCode">获取验证码</el-button>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </system-dialog>
  </div>

</template>

<script>
// import SocialSign from './components/SocialSignin'

import systemDialog from "@/components/system/systemDialog.vue";
import {sendCode, signup} from "@/api/user";

export default {
  name: 'Login',
  components: {systemDialog},
  // components: { SocialSign },
  data() {
    let phoneCheck = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入手机号码"));
      } else if (!/^1[345789]\d{9}$/.test(value)) {
        callback(new Error("请输入正确格式的手机号"));
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
      activeName:'first',
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
        password:"",
        confirmPassword:"",
        phone: "",
        email: "",
        code:"",
        avatar: ""
      },
      rules: {
        username: [{required: true, trigger: 'blur', message: "请填写用户名"},{ min: 1, max:10, message: '长度在10个字符以内', trigger: 'blur' }],
        password: [{required: true, trigger: 'blur', message: "请填写密码"},{ min: 1, max: 20, message: '长度在20个字符以内', trigger: 'blur' }],
        realname: [{required: true, trigger: 'blur', message: "请填写真实姓名"},{ min: 1, max: 5, message: '长度在5个字符以内', trigger: 'blur' }],
        phone: [{required: true,trigger: 'blur', validator: phoneCheck}],
        email:[{required: true,trigger: 'blur', validator: emailCheck}],
        code:[{required: true, trigger: 'blur', message: "请填写验证码"},{ min: 1, max: 5, message: '长度为5个字符', trigger: 'blur' }],
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
    openAddWindow() {
      this.$resetForm('userForm', this.user);
      this.userDialog.title = "用户注册";
      this.userDialog.visible = true;
    },
    onClose() {
      this.userDialog.visible = false;
    },
    async onSendCode() {
      let para = Object.assign({}, this.user);
      let res = null;
      res = await sendCode(para);
      if (res.success) {
        this.$message.success(res.message);
      } else {
        this.$message.error(res.message);
      }


    },
    onConfirm() {
      this.$refs.userForm.validate(async (valid) => {
        let para={"email":this.user.email,"code":this.user.code};
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
    },
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray: #3268fc;
$cursor: #3268fc;

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
    background: white;

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
    border: 1px solid rgba(55, 61, 75, 0.25);
    background: rgb(255, 255, 255);
    border-radius: 5px;
    color: #ffffff;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray: #1e1f22;


.signup-form {
  position: relative;
  width: 520px;
  max-width: 100%;
  padding: 20px 35px 0;
  margin: 2px auto;
  overflow: hidden;

}

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 20px 35px 0;
    margin: 2px auto;
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
      margin: 30px auto 40px auto;
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
