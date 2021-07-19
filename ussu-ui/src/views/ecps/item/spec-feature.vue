<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="featureName">
        <el-input
          v-model="queryParams.featureName"
          placeholder="请输入名称"
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
      <el-table-column label="ID" align="center" prop="featureId" width="80"/>
      <el-table-column label="名称" prop="featureName" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="排序号" prop="featureSort" width="60"/>
      <el-table-column label="筛选" prop="isSelect" width="60">
        <template slot-scope="scope">
          <i class="el-icon-success" style="color: green;" v-if="scope.row.isSelect == 1"></i>
          <i class="el-icon-error" style="color: red;" v-else></i>
        </template>
      </el-table-column>
      <el-table-column label="显示" prop="isShow" width="60">
        <template slot-scope="scope">
          <i class="el-icon-success" style="color: green;" v-if="scope.row.isShow == 1"></i>
          <i class="el-icon-error" style="color: red;" v-else></i>
        </template>
      </el-table-column>
      <el-table-column label="可选值" prop="selectValues">
        <template slot-scope="scope">
          <el-tag
            v-for="(item, index) in scope.row.selectValues.split(',')"
            :key="index"
            effect="plain"
            type="info"
            style="margin: 2px;"
          >
            {{ item }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="类别" prop="catName" width="80" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
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
        <el-form-item label="名称" prop="featureName" :required="true">
          <el-input v-model="form.featureName" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="序号" prop="featureSort">
          <el-input type="number" v-model="form.featureSort" placeholder="请输入序号"/>
        </el-form-item>
        <el-form-item label="选择方式" prop="inputType">
          <el-switch
            v-model="form.inputType"
            :active-value="1"
            :inactive-value="0"
            active-text="多选"
            inactive-text="单选"
          >
          </el-switch>
        </el-form-item>
        <el-form-item label="可选值" prop="selectValues">
          <el-select
            v-model="selectValuesArr"
            multiple
            filterable
            clearable
            allow-create
            placeholder="请选择"
            class="w100"
          >
            <el-option
              v-for="item in selectValuesOptions"
              :key="item"
              :value="item"
              :label="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关联类别">
          <category-cascader
            :paths.sync="form.catIdPath"
            @change="catIdChange"
            custom-style="width:100%;"
          ></category-cascader>
        </el-form-item>
        <el-form-item label="是否显示" prop="isShow">
          <el-switch
            v-model="form.isShow"
            :active-value="1"
            :inactive-value="0"
          >
          </el-switch>
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
import CategoryCascader from '@/components/Item/CategoryCascader'
import {getListTree} from "@/api/ecps/item/cat";
import {add, del, detail, edit, getList} from "@/api/ecps/item/feature";
import {findParents} from "@/utils/ussu";
import {getListByCatId} from "@/api/ecps/item/feature-group";

export default {
  name: "ItemFeature",
  components: {
    CategoryCascader
  },
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
      // 品牌表格数据
      tableList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      catOptions: [],
      featureGroupOptions: [],
      selectValuesOptions: [],
      selectValuesArr: [],
      // 日期范围
      dateRange: [],
      // 查询品牌
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        isSpec: 1,
        featureName: undefined
      },
      // 表单品牌
      form: {
        featureId: undefined,
        isShow: 1,
        isSelect: 0,
        isSpec: 1,
        inputType: 0,
        selectValues: undefined,
        selectValuesArr: []
      },
      // 表单校验
      rules: {
        featureName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ]
      }
    }
  },
  created() {
    this.getCatOptions();
    this.getList();
  },
  watch: {
    selectValuesArr: function(v, ov) {
      if (v && v.length > 0) {
        this.form.selectValues = v.join(',');
      } else {
        this.form.selectValues = undefined;
      }
    }
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
    getCatOptions() {
      getListTree().then(res => {
        this.catOptions = res.data;
      })
    },
    // 获取分组
    getFeatureGroupOptions(catId) {
      if (!catId || catId === '') {
        this.featureGroupOptions = [];
      } else {
        getListByCatId(catId).then(res => {
          this.featureGroupOptions = res.data;
        })
      }
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      getList(this.queryParams).then(response => {
        this.tableList = response.data.records;
        this.total = response.data.pageInfo.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.resetObj(this.form);
      this.selectValuesOptions = [];
      this.selectValuesArr = [];
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
    catIdChange(v) {
      this.form.catId = v;
      // this.form.groupId = undefined;
      // this.getFeatureGroupOptions(v);
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.featureId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      detail(row.featureId).then(res => {
        let f = res.data;
        this.form = f;
        if (f.selectValues && f.selectValues !== '') {
          this.selectValuesOptions = f.selectValues.split(',');
          this.selectValuesArr = this.selectValuesOptions;
        }
        this.form.catIdPath = findParents(this.catOptions, f.catId, 'catId');
        // this.getFeatureGroupOptions(f.catId);
        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.isSpec = 1;
          if (this.form.featureId != undefined) {
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
      const ids = row.featureId || this.ids;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return del(ids);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    }
  }
}
</script>

<style scoped>

</style>
