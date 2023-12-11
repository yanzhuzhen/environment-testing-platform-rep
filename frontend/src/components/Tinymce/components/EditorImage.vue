<template>
  <div class="upload-container">
    <el-button :style="{background:color,borderColor:color}" icon="el-icon-upload" size="mini" type="primary" @click=" dialogVisible=true">
      上传图片
    </el-button>
    <el-dialog :visible.sync="dialogVisible">
      <el-upload
        :multiple="true"
        :file-list="fileList"
        :show-file-list="true"
        :on-remove="handleRemove"
        :on-success="handleSuccess"
        :before-upload="beforeUpload"
        class="editor-slide-upload"
        :headers="uploadHeader"
        action="http://localhost:3000/api/oss/file/upload?module=pic"
        list-type="picture-card"
      >
        <el-button size="small" type="primary">
          点击上传
        </el-button>
      </el-upload>
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        确认
      </el-button>
    </el-dialog>
  </div>
</template>

<script>
// import { getToken } from 'api/qiniu'

import {getToken} from "@/utils/auth";
import deleteFile from "@/api/ossFile";

export default {
  name: 'EditorSlideUpload',
  props: {
    color: {
      type: String,
      default: '#1890ff'
    }
  },
  data() {
    return {
      dialogVisible: false,
      listObj: {},
      fileList: [],
      uploadHeader: {"token": getToken()},
    }
  },
  methods: {
    checkAllSuccess() {
      return Object.keys(this.listObj).every(item => this.listObj[item].hasSuccess)
    },
    handleSubmit() {
      const arr = Object.keys(this.listObj).map(v => this.listObj[v])
      if (!this.checkAllSuccess()) {
        this.$message('请等待图片上传完成！')
        return
      }
      this.$emit('successCBK', arr)
      this.listObj = {}
      this.fileList = []
      this.dialogVisible = false
    },
    handleSuccess(res, file) {
      const id = file.id
      const objKeyArr = Object.keys(this.listObj)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObj[objKeyArr[i]].id === id) {
          this.listObj[objKeyArr[i]].url = res.data
          this.listObj[objKeyArr[i]].hasSuccess = true
          return
        }
      }
    },
    async handleRemove(file) {
      const id = file.id
      const objKeyArr = Object.keys(this.listObj)
      for (let i = 0, len = objKeyArr.length; i < len; i++) {
        if (this.listObj[objKeyArr[i]].id === id) {
          let res = await deleteFile(this.listObj[objKeyArr[i]]);
          if(res.success){
            this.listObj[objKeyArr[i]] = "";
            this.$message.success(res.message);
          }
        }
      }
    },
    beforeUpload(file) {
      const _self = this
      const fileName = file.id
      this.listObj[fileName] = {}
      const img = new Image()
      img.onload = function() {
        _self.listObj[fileName] = { hasSuccess: false, id: file.id, width: this.width, height: this.height }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.editor-slide-upload {
  margin-bottom: 20px;
  ::v-deep .el-upload--picture-card {
    width: 100%;
  }
}
</style>
