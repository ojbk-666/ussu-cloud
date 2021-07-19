<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="groupName">
        <el-input
          v-model="queryParams.groupName"
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
      <el-table-column label="ID" align="center" prop="groupId" width="80"/>
      <el-table-column label="名称" prop="groupName" :show-overflow-tooltip="true"/>
      <el-table-column label="排序号" prop="groupSort" width="60"/>
      <el-table-column label="类被" prop="groupSort">
        <template slot-scope="scope">
          {{ getCatNamePath(scope.row.catId) }}
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="groupDesc" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleLink(scope.row)"
            v-perm="['system:param:edit']"
          >关联
          </el-button>
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
        <el-form-item label="名称" prop="groupName" :required="true">
          <el-input v-model="form.groupName" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="序号" prop="groupSort">
          <el-input type="number" v-model="form.groupSort" placeholder="请输入序号"/>
        </el-form-item>
        <el-form-item label="关联类别">
          <category-cascader
            :paths.sync="form.catIdPath"
            @change="catIdChange"
            custom-style="width:100%;"
          ></category-cascader>
        </el-form-item>
        <el-form-item label="备注" prop="groupDesc">
          <el-input v-model="form.groupDesc" type="textarea" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" size="medium" @click="submitForm">确 定</el-button>
        <el-button size="medium" @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--关联弹层-->
    <el-dialog
      :title="'关联属性:'+ title2"
      :visible.sync="open2"
      width="730px"
      append-to-body
      :destroy-on-close="false"
    >
      <el-form ref="form" :model="form2" :rules="rules2" label-width="80px">
        <el-form-item label="关联属性" prop="featureIdList">
          <el-transfer
            :filterable="true"
            filter-placeholder="请输入"
            :props="transferProps"
            v-model="form2.featureIdList"
            :data="featureOptions"
            :titles="['待选','已关联']"
          >
          </el-transfer>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" size="medium" @click="submitForm2">确 定</el-button>
        <el-button size="medium" @click="cancel2">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {add, del, detail, edit, getList, link} from "@/api/ecps/item/feature-group";
import {getListTree} from "@/api/ecps/item/cat";
import {findParents} from '@/utils/ussu'
import CategoryCascader from "@/components/Item/CategoryCascader";
import {getAllByCatId} from "@/api/ecps/item/feature";

export default {
  name: "FeatureGroup",
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
      // 品牌表格数据
      tableList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 类型数据字典
      typeOptions: [],
      catOptions: [],
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
      form: {},
      // 表单校验
      rules: {
        brandName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ]
      },
      open2: false,
      title2: '',
      form2: {
        groupId: undefined,
        featureIdList: []
      },
      rules2: {
        featureIdList: [
          {required: false, message: "不能为空", trigger: "blur"}
        ]
      },
      featureOptions: [],
      transferProps: {
        key: 'featureId',
        label: 'featureName'
      }
    }
  },
  created() {
    this.getCatOptions();
    this.getList();
  },
  mounted() {
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
    getCatOptions() {
      getListTree().then(res => {
        this.catOptions = res.data;
      })
    },
    getCatNamePath(id) {
      return findParents(this.catOptions, id, 'catId', 'parentId', 'catName').join('/')
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
      this.title = "添加";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.groupId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    catIdChange(v) {
      this.form.catId = v;
    },
    // 关联
    handleLink(row) {
      let f = this.deepClone(row);
      this.title2 = f.groupName;
      this.form2.groupId = f.groupId;
      this.form2.featureIdList = f.featureIdList;
      // 获取可选分类
      getAllByCatId(f.catId, {isSpec: 0}).then(res => {
        this.featureOptions = res.data;
        this.open2 = true;
      })
    },
    cancel2() {
      this.resetObj(this.form2);
      this.open2 = false;
    },
    submitForm2() {
      link(this.form2).then(res => {
        this.msgSuccess("修改成功");
        this.resetObj(this.form2);
        this.open2 = false;
        this.getList();
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      detail(row.groupId).then(res => {
        this.form = res.data;
        this.form.catIdPath = findParents(this.catOptions, this.form.catId, 'catId');
        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.groupId != undefined) {
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
