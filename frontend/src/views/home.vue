<template>
  <div class="common-layout">
    <el-container>
      <el-header><menu-header></menu-header></el-header>
      <el-main>
        <el-carousel :interval="4000" type="card" height="400px">
          <el-carousel-item v-for="item in imgList" :key="item">
            <h3 justify="center"><img style="object-fit: scale-down; width: 100%; height: 100%;" :src="item.idView" alt=""/></h3>

          </el-carousel-item>
        </el-carousel>
        <el-divider />
        <el-space direction="horizontal" fill="fill" size="large">
          <el-row :gutter="85" class="card" justify="space-evenly">
            <el-col :span="8">
              <el-card shadow="hover" class="ccard">
                <el-text class="mx-1" size="large">AI智能技术</el-text>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" class="ccard">
                <el-text class="mx-1" size="large">AI算法模型</el-text>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" class="ccard">
                <el-text class="mx-1" size="large">AI应用服务</el-text>
              </el-card>
            </el-col>
          </el-row>
          <el-row  justify="space-between">
            <el-col span="6" >
              <el-card :body-style="{ padding: '0px' }">
                <img style="object-fit: scale-down; width: 100%; height: 100%;"
                     src="https://picdl.sunbangyan.cn/2023/10/24/962262d6366cd95f97bcffdeb38ce7b3.png"
                    class="image"
                 alt=""/>
                <div style="padding: 14px">
                  <span>算法1</span>
                  <div class="bottom">
                    <el-button text class="button">浏览</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col span="6">
              <el-card :body-style="{ padding: '0px' }">
                <img style="object-fit: scale-down; width: 100%; height: 100%;"
                     src="https://picdl.sunbangyan.cn/2023/10/24/962262d6366cd95f97bcffdeb38ce7b3.png"
                    class="image"
                    alt=""/>
                <div style="padding: 14px">
                  <span>算法2</span>
                  <div class="bottom">

                    <el-button text class="button">浏览</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col span="6">
              <el-card :body-style="{ padding: '0px' }">
                <img style="object-fit: scale-down; width: 100%; height: 100%;"
                     src="https://picdl.sunbangyan.cn/2023/10/24/962262d6366cd95f97bcffdeb38ce7b3.png"
                    class="image"
                    alt=""/>
                <div style="padding: 14px">
                  <span>算法3</span>
                  <div class="bottom">

                    <el-button text class="button">浏览</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col span="6">
              <el-card :body-style="{ padding: '0px' }">
                <img style="object-fit: scale-down; width: 100%; height: 100%;"
                     src="https://picdl.sunbangyan.cn/2023/10/24/962262d6366cd95f97bcffdeb38ce7b3.png"
                    class="image"
                    alt=""/>
                <div style="padding: 14px">
                  <span>算法4</span>
                  <div class="bottom">

                    <el-button text class="button">浏览</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-space>
      </el-main>
      <el-footer></el-footer>
    </el-container>
    <el-backtop :right="100" :bottom="100" />
  </div>


</template>

<script>
import { ref } from 'vue';
import MenuHeader from "@/components/menuHeader";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "home",
  components: { MenuHeader },
  setup() {
    const fill = ref(true);

    return {
      fill,
      imgList: [
        { id: 0, idView: require("/src/assets/1.jpg") },
        { id: 1, idView: require("/src/assets/2.jpg") },
        { id: 2, idView: require("/src/assets/3.jpg") }
      ]
    }
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
.card{
  text-align: center;

}
.ccard{
  width: 450px;

}
.time {
  font-size: 12px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button {
  padding: 0;
  min-height: auto;
}

.image {
  width: 100%;
  display: block;
}
</style>