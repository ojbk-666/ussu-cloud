<template>
  <div>
    <el-upload
      :data="uploadOption.data"
      :multiple="true"
      list-type="picture-card"
      :file-list="fileList"
      :limit="limit"
      class="image-uploader"
      :headers="uploadOption.headers"
      :action="uploadOption.action"
      :on-success="uploadSuccess"
      :on-exceed="handleEexceed"
    >
      <i class="el-icon-plus"></i>
      <!--缩略图模板 start-->
      <div slot="file" slot-scope="{file}">
        <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
        <span class="el-upload-list__item-actions">
        <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
          <i class="el-icon-zoom-in"></i>
        </span>
        <span class="el-upload-list__item-delete" @click="handleRemove(file)">
          <i class="el-icon-delete"></i>
        </span>
      </span>
      </div>
      <!--缩略图模板 end-->
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>

import store from "@/store";

export default {
  name: 'MultiImageUpload',
  props: {
    list: {
      type: Array,
      default: () => []
    },
    limit: {
      type: Number,
      default: 10
    }
  },
  data() {
    return {
      tempUrl: '',
      fileList: [],
      dialogVisible: false,
      dialogImageUrl: undefined,
      pathList: [],
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
  },
  watch: {
    list: function(v, ov) {
      // 回显
      this.pathList = v;
      // 构造fileList
      let arr = [];
      for (const it of v) {
        arr.push({
          name: it,
          url: this.getShowImgUrl(it)
        });
      }
      console.log(arr);
      this.fileList = arr;
    },
    dialogVisible: function(v, ov) {
      if (!v) {
        this.dialogImageUrl = undefined;
      }
    },
    pathList(v, ov) {
      // this.$emit('input', v);
      this.$emit("update:list", v);
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
    getShowImgUrl(path) {
      return this.showImg(path);
    },
    uploadSuccess(res, file, fileList) {
      if (res.code === 20000) {
        this.msgSuccess(`文件${res.data.name}}[${res.data.sizeStr}]上传成功`)
        this.tempUrl = res.data.path;
        this.emitInput(this.tempUrl)
        this.fileList = fileList;
        this.pathList.push(res.data.path);
      }
    },
    handleEexceed(file, fileList) {
      console.log('exx', fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemove(file) {
      let idx = this.fileList.indexOf(file);
      if (idx != -1) {
        this.fileList.splice(idx, 1);
        this.pathList.splice(idx, 1);
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
