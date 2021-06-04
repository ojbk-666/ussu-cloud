<template>
  <div class="container">
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:10px;">
      <div ref="courseQuestionNumber" style="width: 100%;height: 400px;"></div>
    </el-row>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:0px;">
      <el-col :span="12">
        <div ref="questionNumByUserid" style="height: 400px;"></div>
      </el-col>
      <el-col :span="12">
        <div ref="abc" style="height: 400px;background-color: red;"></div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import echarts from 'echarts'
import resize from '../mixins/resize'
import {courseNum, questionNumByUserid} from '@/api/home/dczx'
import {objArr2AttrArr, replaceNewAttrName} from "@/utils/ussu";

require('echarts/theme/macarons') // echarts theme

const animationDuration = 2000

export default {
  name: 'DczxHome',
  mixins: [resize],
  data() {
    return {
      chart: undefined,
      chart1: undefined,
      chart2: undefined,
      chart3: undefined,
      courseNumChartsOption: {
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        legend: {
          data: ['单项选择题', '多项选择题', '判断题']
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          show: true,
          type: 'category',
          data: [],
          axisTick: {
            alignWithLabel: false
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '单项选择题',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: [],
          animationDuration
        }, {
          name: '多项选择题',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: [],
          animationDuration
        }, {
          name: '判断题',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: [],
          animationDuration
        }]
      },
      questionNumByUseridOption: {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '试题贡献量',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '40',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: []
          }
        ]
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart1()
      this.initChart2()
    })
  },
  created() {
    this.getCourseNum();
    this.getQuestionNumByUserid();
  },
  beforeDestroy() {
    if (!this.chart1) {
      return
    }
    this.chart1.dispose()
    this.chart1 = null
    if (!this.chart2) {
      return
    }
    this.chart2.dispose()
    this.chart2 = null
  },
  methods: {
    initChart1() {
      this.chart1 = echarts.init(this.$refs.courseQuestionNumber, 'macarons')
      this.chart1.setOption(this.courseNumChartsOption);
    },
    initChart2() {
      this.chart2 = echarts.init(this.$refs.questionNumByUserid, 'macarons')
      this.chart2.setOption(this.questionNumByUseridOption);
    },
    getCourseNum() {
      courseNum().then(res => {
        let data = res.data;
        this.courseNumChartsOption.xAxis[0].data = objArr2AttrArr(data, 'course_name')
        this.courseNumChartsOption.series[0].data = objArr2AttrArr(data, 'num1')
        this.courseNumChartsOption.series[1].data = objArr2AttrArr(data, 'num2')
        this.courseNumChartsOption.series[2].data = objArr2AttrArr(data, 'num3')
        this.chart1.setOption(this.courseNumChartsOption);
      })
    },
    getQuestionNumByUserid() {
      questionNumByUserid().then(res => {
        let data = res.data;
        this.questionNumByUseridOption.series[0].data = replaceNewAttrName(data, ['userid', 'num'], ['name', 'value']);
        this.chart2.setOption(this.questionNumByUseridOption);
      })
    }
  }
}
</script>
<style>
.container {
  padding: 10px;
  background-color: rgb(240, 242, 245);
  position: relative;
}
</style>
