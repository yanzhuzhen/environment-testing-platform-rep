<template>
  <div>
    <canvas ref="captchaCanvas" width="120" height="40"></canvas>
    <button class="btn" @click="refreshCaptcha">刷新一下</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      captchaText: ""
    };
  },
  mounted() {
    this.refreshCaptcha();
  },
  methods: {
    refreshCaptcha() {
      // 生成随机验证码文本
      this.captchaText = this.generateRandomCaptchaText();

      // 获取canvas元素
      const canvas = this.$refs.captchaCanvas;
      const context = canvas.getContext("2d");

      // 清空画布
      context.clearRect(0, 0, canvas.width, canvas.height);

      function randomNum(min, max) {
        return Math.floor(Math.random() * (min- max) + max);
      }

      // 绘制验证码

      context.font = randomNum(canvas.width / 2, canvas.height/2) + 'px SimHei';
      context.fillStyle = randomNum(50, 160).toString();
      context.fillText(this.captchaText, 10, 30);
    },
    generateRandomCaptchaText() {
      // 生成随机的验证码文本
      // 你可以根据需要自定义生成规则
      const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      let captchaText = "";
      for (let i = 0; i < 4; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        captchaText += characters.charAt(randomIndex);
      }
      return captchaText;
    }

}
};
</script>

<style scoped>
.picyzm {
  width: 100px;
  height: 40px;
  display: inline-block;
  margin: 0 30px;
}
.verifyCodeDemo {
  width: 100%;
  display: flex;
  margin-top: 200px;
  justify-content: center;
}
.btn {
  margin: 30px auto;
  background-color: blue;
  color: #fff;
  border-radius: 5px;
  border: 0;
  width: 100px;
  height: 40px;
}
</style>