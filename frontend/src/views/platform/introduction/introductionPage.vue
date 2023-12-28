<template>
  <el-container>
    <el-main>
      <el-card class="box-card">
       <div class="title" align="center">关于我们</div>
        <el-skeleton style="margin-top: 20px"  :rows="15" />
      </el-card>
    </el-main>

    <el-footer height="150px">
      <myfooter></myfooter>
    </el-footer>
  </el-container>
</template>

<script>
import Myfooter from "@/components/footer/index.vue";

export default {
  name: 'intro',
  components: {Myfooter},
}
</script>


<style lang="scss">

.title {
  font-size: 30px;
  font-weight: bold;
  color: #373d4b;
  padding: 10px 0;
  border-bottom: 1px solid #243141;
  margin: 10px 0;
}

.box-card {
  width: 100%;
  height:690px;
  margin: 10px auto;
  border: 1px solid #eee;
  border-radius: 5px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.15);
  background-color: #fff;
}


</style>
