<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="类别名称" prop="catName">
        <el-input
          v-model="queryParams.catName"
          placeholder="请输入类别名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          v-perm="['system:dept:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="catList"
      row-key="catId"
      :border="true"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column type="index" align="center" width="60"></el-table-column>
      <el-table-column prop="catName" label="类目名称"></el-table-column>
      <el-table-column prop="catId" label="ID" width="100"></el-table-column>
      <el-table-column prop="catLevel" label="类目层级" width="80" align="center"></el-table-column>
      <el-table-column prop="catSort" label="排序" width="80" align="center"></el-table-column>
      <el-table-column prop="catDesc" label="描述"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-perm="['system:dept:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-perm="['system:dept:add']"
          >新增</el-button>
          <el-button
            v-if="scope.row.parentId != 0 && (!scope.row.children || scope.row.children.length === 0)"
            size="mini"
            type="text"
            style="color: red"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-perm="['system:dept:delete']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改类别对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级类别" prop="parentId">
              <treeselect v-model="form.parentId" :options="catTreeOptions" :normalizer="normalizer" placeholder="选择上级类别" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类别名称" prop="catName" :required="true">
              <el-input v-model="form.catName" placeholder="请输入类别名称" maxLength="80"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="排序" prop="catSort">
              <el-input-number v-model="form.catSort" controls-position="right" :min="0" style="width: 100%;"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="catDesc">
              <el-input v-model="form.catDesc" placeholder="请输入备注" maxlength="80" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" size="medium" @click="submitForm">确 定</el-button>
        <el-button size="medium" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCatAll, delDept, addDept, updateDept, listAllDeptExcludeChild } from "@/api/ecps/item/cat";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { deepClone } from '@/utils'

export default {
  name: "Cat",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      catList: [],
      // 类别树选项
      catTreeOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态数据字典
      statusOptions: [],
      // 查询参数
      queryParams: {
        catName: undefined
      },
      // 表单参数
      form: {
        catId: undefined,
        parentId: undefined,
        catName: undefined,
        catSort: undefined,
        keywords: undefined,
        catDesc: undefined
      },
      // 表单校验
      rules: {
        parentId: [
          { required: true, message: "上级类别不能为空", trigger: "blur" }
        ],
        catName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    // this.getDicts("sys_status").then(response => {
    //   this.statusOptions = response.data;
    // });
  },
  methods: {
    /** 查询类别列表 */
    getList() {
      this.loading = true;
      listCatAll(this.queryParams).then(response => {
        this.catList = this.handleTree(response.data, "catId", "parentId", "children", 0);
        this.loading = false;
      });
    },
    /** 转换类别数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.catId,
        label: node.catName,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.resetObj(this.form);
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row != undefined) {
        this.form.parentId = row.id;
      }
      this.open = true;
      this.title = "添加类别";
      listCatAll().then(response => {
        this.catTreeOptions = this.handleTree(response.data, "catId", "parentId","children", 0);
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      // getDept(row.id).then(response => {
      this.form = deepClone(row);
      this.open = true;
      this.title = "修改类别";
      // });
      // listAllDeptExcludeChild(row.id).then(response => {
      //   this.catTreeOptions = this.handleTree(response.data, "id", "parentId", "children", "0");
      // });
      listCatAll().then(response => {
        this.catTreeOptions = this.handleTree(response.data, "catId", "parentId","children", 0);
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateDept(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDept(this.form).then(response => {
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
      this.$confirm('是否确认删除名称为"' + row.name + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delDept(row.id);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    }
  }
};
</script>
