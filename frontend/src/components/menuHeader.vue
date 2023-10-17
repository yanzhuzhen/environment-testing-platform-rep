<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false">
    <el-menu-item index="0">西南国土环境预测预警服务平台</el-menu-item>
    <el-menu-item class="flex-grow" index="spacer"></el-menu-item>
    <el-menu-item index="1">首页</el-menu-item>
    <el-menu-item index="2">智能技术</el-menu-item>
    <el-menu-item index="3">算法模型</el-menu-item>
    <el-menu-item index="4">应用服务</el-menu-item>
    <el-menu-item index="5">人才培养</el-menu-item>
    <el-sub-menu index="6" title="个人">
      <el-menu-item index="2-1">个人主页</el-menu-item>
      <el-menu-item index="2-2">互动讨论</el-menu-item>
      <el-menu-item index="2-3">退出登录</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script  setup>
import { ref } from 'vue'

const activeIndex = ref('1')

</script>

<style>
.flex-grow {
  flex-grow: 1;
}
</style>
