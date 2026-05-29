<template>
  <div class="app-container">
    <el-row :gutter="20" class="summary-cards">
      <el-col :xs="24" :sm="8" :lg="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">平均间隔</div>
            <div class="stat-value">{{ formatDuration(summary.avgInterval) }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8" :lg="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">最长间隔</div>
            <div class="stat-value">{{ formatDuration(summary.maxInterval) }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8" :lg="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <div class="stat-label">最短间隔</div>
            <div class="stat-value">{{ formatDuration(summary.minInterval) }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="intervalList" style="margin-top: 20px;">
      <el-table-column label="序号" type="index" width="60" align="center" />
      <el-table-column label="喂奶时间" align="center" prop="feedingTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.feedingTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="距上次间隔" align="center" prop="interval" width="200">
        <template slot-scope="scope">
          <el-tag :type="getIntervalTag(scope.row.interval)">{{ formatDuration(scope.row.interval) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="喂养方式" align="center" prop="feedMethod" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.feedMethod === 'breast'" type="primary">母乳</el-tag>
          <el-tag v-else-if="scope.row.feedMethod === 'formula'" type="success">配方奶</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="喂养量" align="center" prop="feedAmount" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.feedAmount ? scope.row.feedAmount + 'ml' : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { getFeedingInterval } from '@/api/baby/report'

export default {
  name: 'FeedingInterval',
  data() {
    return {
      loading: false,
      total: 0,
      intervalList: [],
      summary: {
        avgInterval: 0,
        maxInterval: 0,
        minInterval: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getFeedingInterval(this.queryParams).then(response => {
        this.intervalList = response.rows || []
        this.total = response.total || 0
        if (response.data) {
          this.summary = response.data
        }
        this.loading = false
      })
    },
    formatDuration(seconds) {
      if (!seconds || seconds === 0) return '-'
      const h = Math.floor(seconds / 3600)
      const m = Math.floor((seconds % 3600) / 60)
      const s = seconds % 60
      if (h > 0) return h + '时' + m + '分'
      if (m > 0) return m + '分' + s + '秒'
      return s + '秒'
    },
    getIntervalTag(interval) {
      if (!interval) return 'info'
      const m = Math.floor(interval / 60)
      if (m < 120) return 'success'
      if (m < 240) return 'warning'
      return 'danger'
    }
  }
}
</script>

<style scoped>
.summary-cards {
  margin-bottom: 10px;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
</style>