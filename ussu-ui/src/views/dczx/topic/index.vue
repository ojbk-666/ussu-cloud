<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="日志名称" prop="questionTypeNm">
        <el-input
            v-model="queryParams.questionTypeNm"
            placeholder="请输入题型名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roleList" :border="true">
      <el-table-column label="" type="index" align="center" width="50"/>
      <el-table-column label="题型" prop="questionTypeNm" :show-overflow-tooltip="true"/>
      <el-table-column label="topicId" prop="topicId" :show-overflow-tooltip="true"/>
      <el-table-column label="topicTypeCd" prop="topicTypeCd" width="80" />
      <el-table-column label="fullTopicTypeCd" prop="fullTopicTypeCd" width="100"/>
      <el-table-column label="topicTypeId" prop="topicTypeId" :show-overflow-tooltip="true"/>
    </el-table>
    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />
  </div>
</template>

<script>
import { getList } from "@/api/dczx/topic";

export default {
  name: "SysLog",
  data() {
    return {
      // 遮罩层
      loading: true,
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
        pageNum: 1,
        pageSize: 10,
        questionTypeNm: undefined
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
      this.queryParams.createTimeStart = s;
      this.queryParams.createTimeEnd = e;
    }
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      getList(this.queryParams).then(res => {
        this.roleList = res.data.records;
        this.total = res.data.pageInfo.total;
        this.loading = false;
      });
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
    }
  }
};
</script>
