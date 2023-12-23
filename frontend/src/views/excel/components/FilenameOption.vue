<template>
  <div style="display:inline-block;height: 32px;">
    <label class="radio-label" style="padding-left:0;width: 200px;"></label>
    <el-input v-model="filename" placeholder="请输入导出文件名" style="width:200px;height: 32px;" prefix-icon="el-icon-document" />
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    filename: {
      get() {
        return this.value
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  }
}
</script>
