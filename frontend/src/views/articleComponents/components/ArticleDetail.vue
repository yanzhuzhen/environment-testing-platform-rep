<template>
  <div class="createPost-container">
    <el-form ref="postForm" :model="postForm" :rules="rules" class="form-container">
      <sticky :z-index="10" :class-name="'sub-navbar '+postForm.status">
        <CommentDropdown v-model="postForm.commentdisabled" />
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">
          发布
        </el-button>
        <el-button v-loading="loading" type="warning" @click="draftForm">
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
                    <el-input v-model="postForm.displaytime" type="datetime" format="yyyy-MM-dd HH:mm:ss" />
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
import Tinymce from '@/components/Tinymce'
import Upload from '@/components/Upload/SingleImage3'
import MDinput from '@/components/MDinput'
import Sticky from '@/components/Sticky' // 粘性header组件
import { validURL } from '@/utils/validate'
import * as article from '@/api/article'
import { searchUser } from '@/api/remote-search'
import { CommentDropdown, PlatformDropdown, SourceUrlDropdown } from '../Dropdown'

const defaultForm = {
  status: 'draft',
  title: '', // 文章题目
  content: '', // 文章内容
  contentshort: '', // 文章摘要
  displaytime: undefined,
  id: undefined,
  commentdisabled: false,
}

export default {
  name: 'ArticleDetail',
  components: { Tinymce, MDinput, Upload, Sticky, Warning, CommentDropdown, PlatformDropdown, SourceUrlDropdown },
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
      postForm: Object.assign({}, defaultForm),
      author:"",
      loading: false,
      userListOptions: [],
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
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    }
    this.tempRoute = Object.assign({}, this.$route)

    this.getCurrentTime(); // 初始化时获取当前时间
    setInterval(this.getCurrentTime, 1000); // 每秒钟更新一次时间

    this.getAuthor();

  },
  methods: {
    getCurrentTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = this.padZero(now.getMonth() + 1);
      const day = this.padZero(now.getDate());
      const hours = this.padZero(now.getHours());
      const minutes = this.padZero(now.getMinutes());
      const seconds = this.padZero(now.getSeconds());

      this.postForm.displaytime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
          let res = await article.addArticle(this.postForm);
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.error(res.message);
          }
          this.loading = false
        }
      })
    },

    getAuthor(){
      this.postForm.author = this.$store.getters.username;
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
