<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-row :gutter="10" class="mb5">
        <el-col :span="12">
          <el-card class="box-card" :shadow="shadow" :body-style="cardBodyStyle">
            <div slot="header" class="clearfix">
              <span>CPU信息</span>
            </div>
            <el-row :gutter="4">
              <el-col :span="12">
                <div class="text item">属性</div>
              </el-col>
              <el-col :span="12">
                <div class="text item">值</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="12">
                <div class="text item">核心</div>
              </el-col>
              <el-col :span="12">
                <div class="text item">{{ cpu.cpuNum }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="12">
                <div class="text item">用户使用率</div>
              </el-col>
              <el-col :span="12">
                <div class="text item">{{ cpu.used }} %</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="12">
                <div class="text item">系统使用率</div>
              </el-col>
              <el-col :span="12">
                <div class="text item">{{ cpu.sys }} %</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="12">
                <div class="text item">当前空闲率</div>
              </el-col>
              <el-col :span="12">
                <div class="text item">{{ cpu.free }} %</div>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="box-card" :shadow="shadow" :body-style="cardBodyStyle">
            <div slot="header" class="clearfix">
              <span>内存信息</span>
            </div>
            <el-row :gutter="4">
              <el-col :span="8">
                <div class="text item">属性</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">内存</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">JVM</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="8">
                <div class="text item">总内存</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ mem.total }} GB</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.total }} MB</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="8">
                <div class="text item">已用内存</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ mem.used }} GB</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.used }} MB</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="8">
                <div class="text item">剩余内存</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ mem.free }} GB</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.free }} MB</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="8">
                <div class="text item">使用率</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ mem.usage }} %</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.usage }} %</div>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
      <!--JAVA虚拟机信息-->
      <el-row :gutter="10" class="mb5">
        <el-col :span="24">
          <el-card class="box-card" :shadow="shadow" :body-style="cardBodyStyle">
            <div slot="header" class="clearfix">
              <span>JAVA虚拟机信息</span>
            </div>
            <el-row :gutter="4">
              <el-col :span="4">
                <div class="text item">Jvm名称</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.name }}</div>
              </el-col>
              <el-col :span="4">
                <div class="text item">Java版本</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.version }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="4">
                <div class="text item">启动时间</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.startTime }}</div>
              </el-col>
              <el-col :span="4">
                <div class="text item">运行时长</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.runTime }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="4">
                <div class="text item">安装路径</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ jvm.home }}</div>
              </el-col>
              <el-col :span="4">
                <div class="text item">项目路径</div>
              </el-col>
              <el-col :span="8">
                <div class="text item">{{ sys.userDir }}</div>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
      <!--服务器信息-->
      <el-row :gutter="10" class="mb5">
        <el-col :span="24">
          <el-card class="box-card" :shadow="shadow" :body-style="cardBodyStyle">
            <div slot="header" class="clearfix">
              <span>服务器信息</span>
            </div>
            <el-row :gutter="4">
              <el-col :span="6">
                <div class="text item">服务器名称</div>
              </el-col>
              <el-col :span="6">
                <div class="text item">{{ sys.computerName }}</div>
              </el-col>
              <el-col :span="6">
                <div class="text item">操作系统</div>
              </el-col>
              <el-col :span="6">
                <div class="text item">{{ sys.osName }}</div>
              </el-col>
            </el-row>
            <el-row :gutter="4">
              <el-col :span="6">
                <div class="text item">服务器IP</div>
              </el-col>
              <el-col :span="6">
                <div class="text item">{{ sys.computerIp }}</div>
              </el-col>
              <el-col :span="6">
                <div class="text item">系统架构</div>
              </el-col>
              <el-col :span="6">
                <div class="text item">{{ sys.osArch }}</div>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
      <!--磁盘信息-->
      <el-row :gutter="10" class="mb5">
        <el-col :span="24">
          <el-card class="box-card" :shadow="shadow">
            <div slot="header" class="clearfix">
              <span>磁盘信息</span>
            </div>
            <!--table-->
            <el-table :data="sysFiles">
              <el-table-column label="盘符路径" prop="dirName" width="100"/>
              <el-table-column label="文件系统" prop="sysTypeName" :show-overflow-tooltip="true" width="150"/>
              <el-table-column label="盘符类型" prop="typeName"/>
              <el-table-column label="总大小" prop="total" :show-overflow-tooltip="true"/>
              <el-table-column label="可用大小" prop="free" :show-overflow-tooltip="true" width="160"/>
              <el-table-column label="已用大小" prop="used" :show-overflow-tooltip="true"/>
              <el-table-column label="已用百分比" prop="usage">
                <template slot-scope="scope">
                  {{ scope.row.usage }} %
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-row>
  </div>
</template>

<script>
import { getSystemInfo } from '@/api/home/systeminfo'

export default {
  name: 'SystemInfo',
  data() {
    return {
      shadow: 'hover',
      cardBodyStyle: {
        'line-height': '30px'
      },
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      cpu: {},
      jvm: {},
      mem: {},
      sys: {},
      sysFiles: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      getSystemInfo().then(
        res => {
          this.cpu = res.data.cpu
          this.jvm = res.data.jvm
          this.mem = res.data.mem
          this.sys = res.data.sys
          this.sysFiles = res.data.sysFiles
          this.loading = false
        }
      )
    }
  }
}
</script>
<style scoped>
.mb5 {
  margin-bottom: 5px;
}

.el-card__body {
  line-height: 30px;
}
</style>
