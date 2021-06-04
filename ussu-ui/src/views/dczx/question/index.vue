<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="科目" prop="courseId">
        <el-select v-model="queryParams.courseIdStr" placeholder="请选择科目" clearable size="small">
          <el-option
              v-for="item in courseList"
              :key="item.courseId"
              :label="item.courseName"
              :value="item.courseId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="题目名称" prop="questionTitle">
        <el-input
            v-model="queryParams.questionTitle"
            placeholder="请输入题目名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="题型" prop="topicIdLong">
        <el-select v-model="queryParams.topicIdLong" placeholder="请选择题型" clearable size="small">
          <el-option
              v-for="item in topicList"
              :key="item.id"
              :label="item.questionTypeNm"
              :value="item.id"
          />
        </el-select>
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
      <el-table-column label="题目" prop="questionTitle"/>
      <el-table-column label="选项" prop="questionTitle">
        <template slot-scope="scope">
          <div v-for="(option, index) in scope.row.options" :key="option.id">
            <el-tag size="mini" type="" v-if="(scope.row.topic.topicTypeCd === '001' || scope.row.topic.topicTypeCd === '002') && option.istrue===true">
              {{String.fromCharCode(65+index)}}：{{option.optionContent}}
            </el-tag>
            <el-tag size="mini" type="info" v-else-if="(scope.row.topic.topicTypeCd === '001' || scope.row.topic.topicTypeCd === '002')">
              {{String.fromCharCode(65+index)}}：{{option.optionContent}}
            </el-tag>
            <el-tag size="mini" type="success" v-else-if="scope.row.topic.topicTypeCd === '004' && option.istrue===true">
              正确
            </el-tag>
            <el-tag size="mini" type="danger" v-else-if="scope.row.topic.topicTypeCd === '004'">
              错误
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="科目" prop="course.courseName" width="140" :show-overflow-tooltip="true"/>
      <el-table-column label="题型" prop="topic.questionTypeNm" width="100"/>
<!--      <el-table-column label="创建时间" prop="createTime" width="160" :show-overflow-tooltip="true"/>-->
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
import { getList } from "@/api/dczx/question";
import { allIdTitle } from "@/api/dczx/course";
import { allIdTitle as allTopic } from "@/api/dczx/topic";

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
      courseList: [],
      topicList: [],
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
        questionTitle: undefined,
        courseIdStr: undefined,
        topicIdLong: undefined
      }
    };
  },
  created() {
    this.getAllCourse();
    this.getAllTopic();
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
    // 获取科目列表
    getAllCourse() {
      allIdTitle().then(res => {
        this.courseList = res.data;
      })
    },
    getAllTopic() {
      allTopic().then(res => {
        this.topicList = res.data;
      })
    },
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
