<template>
  <div>
    <el-form
        :rules="rules"
        ref="loginForm"
        v-loading="loading"
        element-loading-text="正在登录..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        :model="loginForm"
        class="login-container">
      <h3 class="login-title">系统登录</h3>
      <el-form-item prop="username">
        <el-input size="default" type="text" v-model="loginForm.myusername" auto-complete="off"
                  placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input size="default" type="password" v-model="loginForm.mypassword" auto-complete="off"
                  placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input size="default" type="text" v-model="loginForm.mycode" auto-complete="off"
                  placeholder="点击更换验证码" @keydown.enter="submitLogin" style="width: 250px"></el-input>
        <pic-code :width="200" :height="60" v-model:Code="loginForm.code" ></pic-code>
      </el-form-item>
      <el-checkbox size="default" class="login-remember" v-model="checked"></el-checkbox>
      <el-button size="default" type="primary" style="width: 100%;" @click="submitLogin">
        登录
      </el-button>
    </el-form>
  </div>
</template>

<script>
import PicCode from "@/components/PicCode/PicCode.vue";
import axios from "axios";
import {InputTest} from "@/utils/CommonUtils";
// import { defineComponent} from "vue";


export default{
  // eslint-disable-next-line vue/multi-word-component-names
  name: "login",
  components: {PicCode},
  defineComponent:{
                    components: { PicCode },
  },
  data () {
    return {
      loading: false,
      loginForm: {
        myusername: '',
        mypassword: '',
        code: '',
        mycode:'',
        ErrorMessage:''
      },
      checked: true,
      rules: {
        myusername: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        mypassword: [{required: true, message: '请输入密码', trigger: 'blur'}],
        mycode: [{required: true, message: '请输入验证码', trigger: 'blur'}]
      }
    }
  },
  methods: {
    LoginInputTest() {
      // eslint-disable-next-line no-empty
      if (!InputTest(this.loginForm.myusername, this.loginForm.myusername)) {
        return false;
      }
    },


    submitLogin() {

      if(this.LoginInputTest()) {
        axios.post('/api/loginController', {
          myusername: this.loginForm.myusername,
          mypassword: this.loginForm.mypassword
        })
            .then(response => {
              localStorage.setItem('token', response.data.token);
              this.$router.push('/home')
            })
            .catch(error => {
              console.error(error);
            })
        // eslint-disable-next-line no-empty
      }else {

      }
      }

    }

  }

</script>


<style scoped>
  .login-container{
    border-radius: 30px;
    background-clip: padding-box;
    margin: 180px 500px;
    padding: 35px 35px 15px 35px;
    background: white;
    border: 1px solid white;
    box-shadow: 0 0 25px gray;
  }
  .login-title{
    margin: 0px auto 40px auto;
    text-align: center;
    color:black;
  }
  .login-remember{
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>