<template>
  <div class="createPost-container">
    <el-form ref="postForm" :model="postForm" :rules="rules" class="form-container">
      <sticky :z-index="10" :class-name="'sub-navbar '+postForm.status">
        <CommentDropdown v-model="postForm.commentdisabled" />
        <el-button style="margin-left: 10px;" type="success" @click="submitForm">
          发布
        </el-button>
        <el-button type="warning" @click="draftForm">
          保存为草稿
        </el-button>
      </sticky>
      <div class="createPost-main-container">
        <el-row>
          <el-col :span="24">
            <el-form-item style="margin-bottom: 40px;" prop="title">
              <MDinput v-model="postForm.title" :maxlength="100" name="name" required>
                标题
              </MDinput>
            </el-form-item>
            <div class="postInfo-container">
              <el-row>
                <el-col :span="8">
                  <el-form-item label-width="60px" label="作者" prop="author" class="postInfo-container-item">
                    <el-button type="primary" round>{{ this.postForm.author }}</el-button>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label-width="120px" label="发布时间" class="postInfo-container-item">
                    <el-input v-model="postForm.displaytime" type="date" format="yyyy-MM-dd" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>

        <el-form-item style="margin-bottom: 40px;" label-width="100px" label="文章概要：">
          <el-input v-model="postForm.contentshort" :rows="1" type="textarea" class="article-textarea" autosize placeholder="Please enter the content" />
          <span v-show="contentShortLength" class="word-counter">{{ contentShortLength }} 个字</span>
        </el-form-item>

        <el-form-item prop="content" style="margin-bottom: 30px;">
          <Tinymce ref="editor" v-model="postForm.content" :height="400" />
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>

import * as article from '@/api/article'
import { CommentDropdown, PlatformDropdown, SourceUrlDropdown } from './Dropdown'



export default {
  name: 'editArticle',
  components: { Tinymce:() => import("@/components/Tinymce"),
    MDinput:() => import("@/components/MDinput"),
    Upload:() => import("@/components/Upload/SingleImage3"),
    Sticky:() => import("@/components/Sticky"),
    CommentDropdown,
    PlatformDropdown,
    SourceUrlDropdown },
  data() {
    const validateRequire = (rule, value, callback) => {
      if (value === '') {
        this.$message({
          message: rule.field + '为必传项',
          type: 'error'
        })
        callback(new Error(rule.field + '为必传项'))
      } else {
        callback()
      }
    }
    return {
      aid:this.$route.query.id,
      postForm:{
        status: "draft",
        title:  "", // 文章题目
        content:  "", // 文章内容
        contentshort:  "", // 文章摘要
        displaytime: undefined,
        commentdisabled: false,
      },
      author:"",
      loading: false,
      rules: {
        title: [{ validator: validateRequire }],
        content: [{ validator: validateRequire }],
      },
      tempRoute: {}
    }
  },
  computed: {
    contentShortLength() {
      return this.postForm.contentshort.length
    },
  },
  created() {
    this.getEditArticle();

    this.tempRoute = Object.assign({}, this.$route)

    this.getCurrentTime(); // 初始化时获取当前时间
    setInterval(this.getCurrentTime, 1000); // 每秒钟更新一次时间



  },
  methods: {
    getCurrentTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = this.padZero(now.getMonth() + 1);
      const day = this.padZero(now.getDate());
      this.postForm.displaytime = `${year}-${month}-${day}`
    },
    padZero(value) {
      return value < 10 ? `0${value}` : value;
    },

    async submitForm() {
      await this.$refs.postForm.validate(async valid => {
        if (valid) {
          this.loading = true
          this.postForm.status = 'published'
          let res = await article.publishArticle(this.postForm);
          if(res.success){
            this.$notify({
              title: '成功',
              message: '发布文章成功',
              type: 'success',
              duration: 2000
            })
          }else {
            this.$notify({
              title: '失败',
              message: '发布文章失败',
              type: 'error',
              duration: 2000
            })
          }
          this.loading = false
        }
      })
    },

    async draftForm() {
      if (this.postForm.content.length === 0 || this.postForm.title.length === 0) {
        this.$message({
          message: '请填写必要的标题和内容',
          type: 'warning'
        })
        return
      }
      await this.$refs.postForm.validate(async valid => {
        if (valid) {
          this.loading = true
          this.postForm.status = 'draft'
          let res = await article.updateArticle(this.postForm);
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.error(res.message);
          }
          this.loading = false
        }
      })
    },

    async getEditArticle(){
      let res = await article.getEditArticle(this.aid);
      if(res.success){
        this.postForm = res.data;
        console.log(this.postForm)
      }
    }

  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/mixin.scss";

.createPost-container {
  position: relative;

  .createPost-main-container {
    padding: 40px 45px 20px 50px;

    .postInfo-container {
      position: relative;
      @include clearfix;
      margin-bottom: 10px;

      .postInfo-container-item {
        float: left;
      }
    }
  }

  .word-counter {
    width: 40px;
    position: absolute;
    right: 10px;
    top: 0px;
  }
}

.article-textarea ::v-deep {
  textarea {
    padding-right: 40px;
    resize: none;
    border: none;
    border-radius: 0px;
    border-bottom: 1px solid #bfcbd9;
  }
}
</style>
