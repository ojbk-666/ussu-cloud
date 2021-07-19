<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="品牌名称" prop="brandName">
        <el-input
          v-model="queryParams.brandName"
          placeholder="请输入品牌名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="changeQueryDate"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-perm="['system:param:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-perm="['system:param:delete']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tableList" :border="true" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" align="center" prop="brandId" width="80"/>
      <el-table-column label="品牌名称" prop="brandName" :show-overflow-tooltip="true"/>
      <el-table-column label="排序号" prop="brandSort" width="60"/>
      <el-table-column label="图片" prop="imgs">
        <template slot-scope="scope">
          <el-image
            :src="showImg(scope.row.imgs)"
            :title="scope.row.imgs"
            fit="contain"
            style="width: 100%;height: 60px;"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="网址" prop="website"/>
      <el-table-column label="备注" prop="brandDesc" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-perm="['system:param:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            style="color: red"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-perm="['system:param:delete']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改品牌配置对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :destroy-on-close="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="品牌名称" prop="brandName" :required="true">
          <el-input v-model="form.brandName" placeholder="请输入品牌名称"/>
        </el-form-item>
        <el-form-item label="序号" prop="brandSort">
          <el-input type="number" v-model="form.brandSort" placeholder="请输入选后"/>
        </el-form-item>
        <el-form-item label="品牌图片" prop="imgs">
          <file-upload path="epcs/brand" v-model="form.imgs"></file-upload>
        </el-form-item>
        <el-form-item label="关联类别">
          <category-cascader
            :multiple="true"
            :paths.sync="catIdPathList"
            @change="selectedCatIdsChange"
            custom-class="w100"
          ></category-cascader>
        </el-form-item>
        <el-form-item label="网址" prop="website">
          <el-input v-model="form.website" placeholder="请输入品牌网址"/>
        </el-form-item>
        <el-form-item label="备注" prop="brandDesc">
          <el-input v-model="form.brandDesc" type="textarea" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" size="medium" @click="submitForm">确 定</el-button>
        <el-button size="medium" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getList, add, edit, del, detail} from '@/api/ecps/item/brand';
import FileUpload from "@/components/Upload/FileUpload";
import CategoryCascader from "@/components/Item/CategoryCascader";

export default {
  name: "Brand",
  components: {
    CategoryCascader,
    FileUpload
  },
  data: function() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 品牌表格数据
      tableList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      // 日期范围
      dateRange: [],
      // 查询品牌
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
        configKey: undefined,
        configType: undefined,
        catIds: []
      },
      // 表单品牌
      form: {
        brandId: undefined,
        brandName: undefined,
        brandSort: undefined,
        imgs: undefined,
        website: undefined
      },
      // 表单校验
      rules: {
        brandName: [
          {required: true, message: "品牌名称不能为空", trigger: "blur"}
        ]
      },
      catIdPathList: []
    };
  },
  computed: {
  },
  //存放 过滤器
  filters: {},
  //自定义 私有指令
  directives: {},
  created() {
    this.getList();
  },
  watch: {
  },
  mounted() {
  },
  beforeDestroy() {
  },
  destroyed() {
  },
  methods: {
    changeQueryDate: function(a) {
      let s = '';
      let e = '';
      if (a) {
        s = a[0];
        e = a[1];
      }
      this.queryParams.createTimeStart = s;
      this.queryParams.createTimeEnd = e;
    },
    /** 查询品牌列表 */
    getList() {
      this.loading = true;
      getList(this.queryParams).then(response => {
        this.tableList = response.data.records;
        this.total = response.data.pageInfo.total;
        this.loading = false;
      });
    },
    moduleFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.module);
    },
    selectedCatIdsChange(arr) {
      this.form.catIds = arr;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加品牌";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.brandId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      detail(row.brandId).then(res => {
        this.form = res.data;
        if (this.form.catList) {
          let arr = this.form.catList.map(item => {
            return item.idPathList;
          });
          this.catIdPathList = arr;
        }
        this.open = true;
        this.title = "修改品牌";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.brandId != undefined) {
            edit(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            add(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.brandId || this.ids;
      this.$confirm('是否确认删除品牌?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return del(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/config/export', {
        ...this.queryParams
      }, `config_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>

</style>
