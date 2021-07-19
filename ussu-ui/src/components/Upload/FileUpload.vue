<template>
  <div class="singleImageUpload2 upload-container">
    <el-upload
      :data="uploadOption.data"
      :multiple="false"
      :show-file-list="false"
      class="image-uploader"
      drag
      :headers="uploadOption.headers"
      :action="uploadOption.action"
      :on-success="uploadSuccess"
    >
      <i class="el-icon-upload" />
      <div class="el-upload__text">
        拖拽到这里或<em>点击上传</em>
      </div>
    </el-upload>
    <div v-show="value.length>0" class="image-preview">
      <div v-show="value.length>1" class="image-preview-wrapper">
        <img :src="imageUrl">
        <div class="image-preview-action">
          <i class="el-icon-delete" @click="rmImage" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import store from "@/store";

export default {
  name: 'FileUpload',
  props: {
    value: {
      type: String,
      default: ''
    },
    path: {
      type: String,
      default: undefined
    }
  },
  data() {
    return {
      tempUrl: '',
      uploadOption: {
        action: process.env.VUE_APP_BASE_API + '/files/upload',
        headers: {
          token: store.getters.token
        },
        data: {
          path: this.path
        }
      }
    }
  },
  computed: {
    imageUrl() {
      return process.env.VUE_APP_FILE_SERVER + this.value
    }
  },
  methods: {
    rmImage() {
      this.emitInput('')
    },
    emitInput(val) {
      this.$emit('input', val)
    },
    handleImageSuccess() {
      this.emitInput(this.tempUrl)
    },
    beforeUpload() {
    },
    uploadSuccess(res, file, fileList) {
      if (res.code === 20000) {
        this.msgSuccess(`文件${res.data.name}}[${res.data.sizeStr}]上传成功`)
        this.tempUrl = res.data.path;
        this.emitInput(this.tempUrl)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.upload-container {
  width: 100%;
  height: 100%;
  position: relative;
  .image-uploader {
    height: 100%;
  }
  .image-preview {
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0px;
    top: 0px;
    border: 1px dashed #d9d9d9;
    .image-preview-wrapper {
      position: relative;
      width: 100%;
      height: 100%;
      img {
        width: 100%;
        height: 100%;
      }
    }
    .image-preview-action {
      position: absolute;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      cursor: default;
      text-align: center;
      color: #fff;
      opacity: 0;
      font-size: 20px;
      background-color: rgba(0, 0, 0, .5);
      transition: opacity .3s;
      cursor: pointer;
      text-align: center;
      line-height: 200px;
      .el-icon-delete {
        font-size: 36px;
      }
    }
    &:hover {
      .image-preview-action {
        opacity: 1;
      }
    }
  }
}
</style>
