<template>
  <div>{{ this.mycontent }}</div>
</template>

<script>
import * as article from "@/api/article";

export default {
  name:"articleView",
  data(){
    return{
      mycontent:""
    }
  },
  created() {
    this.getContent();
  },
  methods:{
    async getContent(){
      let res = await article.getArticle(this.$route.query.id);
      if(res.success){
        this.mycontent = res.data.content;
      }
    }
  }
}
</script>

<style scoped lang="scss">

</style>
