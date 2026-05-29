<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px" class="report-form">
      <el-form-item label="时间范围">
        <el-select v-model="queryParams.range" placeholder="请选择时间范围" style="width: 240px" @change="handleQuery">
          <el-option label="最近7天" value="7" />
          <el-option label="最近30天" value="30" />
          <el-option label="最近90天" value="90" />
          <el-option label="全部" value="all" />
        </el-select>
      </el-form-item>
    </el-form>

    <el-row :gutter="20" style="min-height: 36px;">
      <el-col :span="24">
        <div id="growthChart" style="width: 100%; height: 400px;"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getGrowthReport } from '@/api/baby/report'

export default {
  name: 'GrowthReport',
  data() {
    return {
      queryParams: {
        range: '30'
      },
      chart: null
    }
  },
  mounted() {
    this.loadData()
  },
  beforeDestroy() {
    if (this._resizeHandler) {
      window.removeEventListener('resize', this._resizeHandler)
    }
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }
  },
  methods: {
    handleQuery() {
      this.loadData()
    },
    loadData() {
      getGrowthReport(this.queryParams).then(response => {
        this.initChart(response.data)
      })
    },
    initChart(data) {
      const heightData = data.heightList || []
      const weightData = data.weightList || []
      const dates = data.dates || []

      if (!this.chart) {
        this.chart = echarts.init(document.getElementById('growthChart'))
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        legend: {
          data: ['身高(cm)', '体重(kg)']
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisPointer: {
            type: 'shadow'
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '身高(cm)',
            min: function(value) { return Math.floor(value.min - 5) },
            max: function(value) { return Math.ceil(value.max + 5) },
            axisLabel: {
              formatter: '{value} cm'
            }
          },
          {
            type: 'value',
            name: '体重(kg)',
            min: function(value) { return Math.floor(value.min - 2) },
            max: function(value) { return Math.ceil(value.max + 2) },
            axisLabel: {
              formatter: '{value} kg'
            }
          }
        ],
        series: [
          {
            name: '身高(cm)',
            type: 'line',
            data: heightData,
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '体重(kg)',
            type: 'line',
            yAxisIndex: 1,
            data: weightData,
            smooth: true,
            itemStyle: {
              color: '#67C23A'
            }
          }
        ],
        grid: {
          left: 60,
          right: 60,
          bottom: 40,
          top: 40
        }
      }

      this.chart.setOption(option)
    this._resizeHandler = () => {
      if (this.chart) {
        this.chart.resize()
      }
    }
    window.addEventListener('resize', this._resizeHandler)
    }
  }
}
</script>

<style scoped>
.report-form {
  margin-bottom: 10px;
}
</style>