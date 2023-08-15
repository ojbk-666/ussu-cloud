<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品名称" prop="itemName">
        <el-input
          v-model="queryParams.itemName"
          placeholder="请输入商品名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品分类">
        <category-cascader :paths="queryParamsCatIdPath" @change="queryCategoryChange"></category-cascader>
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
<!--      <el-table-column label="ID" align="center" prop="itemId" width="80"/>-->
      <el-table-column label="名称" prop="itemName" :show-overflow-tooltip="true"/>
      <el-table-column label="商品编号" prop="itemNo"/>
      <el-table-column label="排序号" prop="itemSort" width="60"/>
      <el-table-column label="图片" prop="imgs">
        <template slot-scope="scope">
          <el-image
            :src="showImg(getFirstImgPath(scope.row.imgs))"
            :title="scope.row.imgs"
            fit="contain"
            style="width: 100%;height: 60px;"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="品牌" prop="brandName" :show-overflow-tooltip="true"/>
      <el-table-column label="商品状态" prop="sales" width="100">
        <template slot-scope="scope">
          {{itemStatusFoarmt(scope.row.auditStatus, scope.row.showStatus)}}
        </template>
      </el-table-column>
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
          <!--<el-button
            size="mini"
            type="text"
            style="color: red"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-perm="['system:param:delete']"
          >删除
          </el-button>-->
          <el-dropdown
            trigger="hover"
            hide-on-click
            @command="((value) => handleRowMore(value, scope.row))"
          >
            <el-button
              type="text"
            >更多<i class="el-icon-arrow-down"></i></el-button>
            <!--<span class="el-dropdown-link">
              更多<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>
            </span>-->
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item icon="el-icon-tickets" command="a">商品详情</el-dropdown-item>
              <el-dropdown-item icon="el-icon-check" command="b">上架</el-dropdown-item>
              <el-dropdown-item icon="el-icon-close" command="c">下架</el-dropdown-item>
              <el-dropdown-item icon="el-icon-check" command="d">哈哈哈</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-check" command="e">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
// import Vue from 'vue';
import {add, del, detail, edit, getList} from "@/api/ecps/item/item";
import CategoryCascader from "@/components/Item/CategoryCascader";

export default {
  name: "Item",
  components: {CategoryCascader},
  data() {
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
      // 表格数据
      tableList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      queryParamsCatIdPath: [],
      // 查询
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
        configKey: undefined,
        configType: undefined,
        catIds: []
      },
      // 表单
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
      }
    }
  },
  created() {
    this.getList();
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
    queryCategoryChange(v) {
      this.queryParams.catId = v;
    },
    getList() {
      this.loading = true;
      getList(this.queryParams).then(response => {
        this.tableList = response.data.records;
        this.total = response.data.pageInfo.total;
        this.loading = false;
      });
    },
    itemStatusFoarmt(auditStatus, showStatus) {
      if (auditStatus === 0) {
        return '待审核';
      } else if (auditStatus === 1) {
        if (showStatus === 1) {
          return '上架';
        } else {
          return '下架';
        }
      } else if (auditStatus === 2) {
        return '审核不通过';
      }
    },
    getFirstImgPath(imgs) {
      let imgArr = JSON.parse(imgs);
      if (imgArr && imgArr.length > 0) {
        return imgArr[0];
      }
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
      this.queryParamsCatIdPath = [];
      this.queryParams.catId = undefined;
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
      detail(row.itemId).then(res => {
        this.form = res.data;
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
      const ids = row.itemId || this.ids;
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
    handleRowMore(command, item) {
      console.log(command, item);
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
