<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="日志名称" prop="query_log_name">
        <el-input
          v-model="queryParams.query_log_name"
          placeholder="请输入日志名称"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求IP" prop="query_ip">
        <el-input
          v-model="queryParams.query_ip"
          placeholder="请输入请求IP"
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
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志名称" prop="log_name" width="100" />
      <el-table-column label="业务代码" prop="business_code" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="请求方法" prop="request_method" />
      <el-table-column label="请求参数" prop="request_params" :show-overflow-tooltip="true" />
      <el-table-column label="请求时间" prop="request_time" :show-overflow-tooltip="true" width="160" />
      <el-table-column label="请求路径" prop="request_uri" :show-overflow-tooltip="true" />
      <el-table-column label="请求IP" prop="remote_ip" />
      <el-table-column label="请求用户" prop="user_name" />
      <el-table-column label="浏览器" prop="browser_type" />
      <el-table-column label="响应结果" prop="result_code" />
      <el-table-column label="响应时间" prop="executeTimeStr" width="120" />
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.page"
      :limit.sync="queryParams.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getSysLogList } from "@/api/system/log";

export default {
  name: "SysLog",
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
      // 角色表格数据
      roleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        page: 1,
        limit: 15,
        roleName: undefined,
        roleKey: undefined,
        status: undefined
      },
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  created() {
    this.getList();
  },
  watch: {
    dateRange(v, ov) {
      const s = v ? v[0] : '';
      const e = v ? v[1] : '';
      this.queryParams.startDate = s;
      this.queryParams.endDate = e;
    }
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      getSysLogList(this.queryParams).then(
        res => {
          this.roleList = res.data;
          this.total = res.count;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/role/export', {
        ...this.queryParams
      }, `role_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
