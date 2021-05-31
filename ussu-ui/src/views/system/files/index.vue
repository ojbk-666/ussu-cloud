<template>
  <div class="files-container">
    <div class="operate">
      <div class="operate-top">
        <div class="operate-top-left">
          <el-upload
              :action="uploadOption.action"
              :show-file-list="false"
              :headers="uploadOption.headers"
              :data="uploadOption.data"
              :on-success="uploadSuccess"
              class="mr10"
              style="display: inline-block;">
            <el-button :size="buttonSize">上传<i class="el-icon-upload el-icon--right"></i></el-button>
          </el-upload>
          <el-button :size="buttonSize">新建文件夹</el-button>
          <el-button :size="buttonSize">预览</el-button>
          <el-button :size="buttonSize">删除</el-button>
          <el-button :size="buttonSize" @click="getList">刷新</el-button>
        </div>
        <div class="operate-top-right">
          <div style="display: inline-block;" @click="changeShowMode(showModeGrid)">
            <i class="el-icon-menu" v-if="showModeGrid" title="宫格显示"/>
            <svg-icon icon-class="list1" v-else-if="showModeList" title="列表显示"/>
          </div>
        </div>
      </div>
      <div class="operate-bottom">
        <el-link @click="backFloder">返回上一级</el-link>
        |
        <el-link @click="folderPathList = []">全部文件</el-link>
        <el-link
            v-for="(path, index) in folderPathList"
            :key="path"
            @click="toPath(index)">&nbsp;&gt;&nbsp;{{ path }}
        </el-link>
      </div>
    </div>
    <div class="grid" v-loading="loading" v-if="showModeGrid">
      <!--grid item-->
      <div v-for="item of fileList" :key="item.url" @click="fileClick(item, $event)">
        <div class="icon-item">
          <svg-icon
              icon-class="folder"
              v-if="isFolder(item.type)"
              class-name="file-item-content grid-svg"/>
          <svg-icon
              :icon-class="svgName(item.name)"
              v-else-if="!isFolder(item.type) && !isImg(item.type)"
              class-name="file-item-content grid-svg"/>
          <el-image
              v-else-if="isImg(item.type)"
              :src="item.url"
              fit="cover"
              class="file-item-content"
              :preview-src-list="imagePreviewList"
          ></el-image>
          <el-tooltip effect="light" :content="item.name">
            <span class="file-name">{{ item.name }}</span>
          </el-tooltip>
        </div>
      </div>
    </div>
    <!--listMode-->
    <el-table :data="fileList" v-loading="loading" v-if="showModeList">
      <el-table-column type="selection" label="" prop="name" width="50"/>
      <el-table-column label="文件名" prop="name" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span @click="fileClick(scope.row, $event)">
            <svg-icon icon-class="folder" v-if="isFolder(scope.row.type)" class-name="list-item-icon"/>
            <svg-icon :icon-class="svgName(scope.row.name)" v-else-if="!isFolder(scope.row.type)"
                      class-name="list-item-icon"/>
            &nbsp;{{ scope.row.name }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="大小" prop="sizeStr" width="110"/>
      <el-table-column label="修改日期" prop="sizeStr" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.timestamp) }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import clipboard from '@/utils/clipboard'
import {list, upload} from '@/api/system/files'
import store from "@/store";
import {getSvgByFileExt} from "@/utils/ussu";

export default {
  name: 'SystemFiles',
  data() {
    return {
      loading: false,
      buttonSize: 'medium',
      showMode: 'grid',
      fileList: undefined,
      imagePreviewList: undefined,
      folderPathList: [],
      queryParams: {
        path: ''
      },
      uploadOption: {
        action: process.env.VUE_APP_BASE_API + '/system/files/upload',
        headers: {
          token: store.getters.token
        },
        data: {
          path: undefined
        }
      }
    }
  },
  created() {
    this.getList();
  },
  computed: {
    showModeGrid: function() {
      return this.showMode === 'grid';
    },
    showModeList: function() {
      return this.showMode === 'list';
    }
  },
  watch: {
    folderPathList: function(v, ov) {
      this.queryParams.path = v.join("/");
      this.uploadOption.data.path = this.queryParams.path;
    },
    'queryParams.path': function(v, ov) {
      this.getList();
    }
  },
  methods: {
    getList() {
      this.loading = true;
      list(this.queryParams).then(res => {
        this.fileList = res.data;
        this.imagePreviewList = this.fileList.filter(item => {
          return this.isImg(item.type)
        }).map(item => {
          return item.url;
        })
      }).catch(res => {
        this.msgError(res.msg);
      }).finally(() => {
        this.loading = false;
      });
    },
    uploadFile() {
      upload().then(res => {
        this.getList();
      })
    },
    uploadSuccess(response, file, fileList) {
      if (response.code === 20000) {
        this.getList();
        this.msgSuccess(`文件${response.data.name}}[${response.data.sizeStr}]上传成功`)
      }
    },
    isFolder(type) {
      return type && type === 'folder';
    },
    isImg(type) {
      return type && type.startsWith('image');
    },
    backFloder() {
      if (this.folderPathList.length == 0) {
        return;
      }
      this.folderPathList.splice(this.folderPathList.length - 1, 1);
    },
    changeShowMode(t) {
      this.showMode = t ? 'list' : 'grid';
    },
    svgName(fileName) {
      return getSvgByFileExt(fileName);
    },
    toPath(idx) {
      let len = idx + 1;
      if (len <= this.folderPathList.length) {
        this.folderPathList = this.folderPathList.slice(0, len);
      }
    },
    fileClick(file, e) {
      console.log(file);
      if (file.type === 'folder') {
        this.folderPathList.push(file.name);
      } else if (this.isImg(file.type)) {
        clipboard(file.url, e, '访问链接已复制')
        return;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.files-container {
  margin: 20px;
  overflow: hidden;
  //padding: 10px;

  .operate {
    position: relative;
    padding: 10px;
  }

  .operate-top {

  }

  .operate-top-left {
    display: inline-block;
  }

  .operate-top-right {
    display: inline-block;
    position: absolute;
    right: 0;
  }

  .operate-bottom {
    margin: 10px 0;
  }

  .grid {
    position: relative;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }

  .list {

  }

  .list-item-icon {
    width: 18px;
    height: 18px;
  }

  .icon-item {
    margin: 5px;
    padding: 5px;
    text-align: center;
    width: 110px;
    float: left;
    //font-size: 30px;
    color: #24292e;
    cursor: pointer;
    border-radius: 4px;
  }
  .icon-item:hover {
    background-color: #ffa50015;
  }

  span {
    display: block;
    font-size: 14px;
    margin-top: 4px;
  }

  .disabled {
    pointer-events: none;
  }

  .file-item-content {
    width: 100px;
    height: 84px;
  }

  .grid-svg {
    width: 56px;
    text-align: center;
  }

  .file-name {
    width: 110px;
    font-size: 14px;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>
