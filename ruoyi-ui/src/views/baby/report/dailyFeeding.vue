<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px" class="report-form">
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleQuery"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="20" style="min-height: 36px;">
      <el-col :span="24">
        <div id="dailyFeedingChart" style="width: 100%; height: 400px;"></div>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="feedingList" style="margin-top: 20px;">
      <el-table-column label="日期" align="center" prop="date" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.date }}</span>
        </template>
      </el-table-column>
      <el-table-column label="喂养次数" align="center" prop="count" width="120">
        <template slot-scope="scope">
          <el-tag type="primary">{{ scope.row.count }} 次</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="总喂养量" align="center" prop="totalAmount" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.totalAmount || 0 }} ml</span>
        </template>
      </el-table-column>
      <el-table-column label="母乳次数" align="center" prop="breastCount" width="120">
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.breastCount || 0 }} 次</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="配方奶次数" align="center" prop="formulaCount" width="120">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.formulaCount || 0 }} 次</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDailyFeeding } from '@/api/baby/report'

export default {
  name: 'DailyFeeding',
  data() {
    return {
      loading: false,
      total: 0,
      feedingList: [],
      dateRange: [],
      chart: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  mounted() {
    this.getList()
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
      this.queryParams.pageNum = 1
      this.getList()
    },
    getList() {
      this.loading = true
      const params = this.addDateRange(this.queryParams, this.dateRange)
      getDailyFeeding(params).then(response => {
        this.feedingList = response.rows || []
        this.total = response.total || 0
        this.loading = false
        if (response.data) {
          this.initChart(response.data)
        }
      })
    },
    initChart(data) {
      const dates = data.dates || []
      const amounts = data.amounts || []

      if (!this.chart) {
        this.chart = echarts.init(document.getElementById('dailyFeedingChart'))
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: '喂养量(ml)',
          axisLabel: {
            formatter: '{value} ml'
          }
        },
        series: [
          {
            name: '喂养量',
            type: 'bar',
            data: amounts,
            itemStyle: {
              color: '#409EFF',
              borderRadius: [4, 4, 0, 0]
            },
            barMaxWidth: 40
          }
        ],
        grid: {
          left: 60,
          right: 30,
          bottom: 60,
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