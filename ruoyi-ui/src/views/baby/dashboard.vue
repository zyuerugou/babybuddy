<template>
  <div class="app-container">
    <el-row :gutter="20" class="dashboard-cards">
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card feeding-card">
          <div class="stat-icon">
            <svg-icon icon-class="list" />
          </div>
          <div class="stat-info">
            <div class="stat-title">今日喂奶次数</div>
            <div class="stat-value">{{ stats.feedingCount }}</div>
            <div class="stat-unit">次</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card diaper-card">
          <div class="stat-icon">
            <svg-icon icon-class="tickets" />
          </div>
          <div class="stat-info">
            <div class="stat-title">今日换尿布次数</div>
            <div class="stat-value">{{ stats.diaperCount }}</div>
            <div class="stat-unit">次</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card play-card">
          <div class="stat-icon">
            <svg-icon icon-class="component" />
          </div>
          <div class="stat-info">
            <div class="stat-title">今日玩耍次数</div>
            <div class="stat-value">{{ stats.playCount }}</div>
            <div class="stat-unit">次</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card shadow="hover" class="stat-card total-card">
          <div class="stat-icon">
            <svg-icon icon-class="money" />
          </div>
          <div class="stat-info">
            <div class="stat-title">今日总喂养量</div>
            <div class="stat-value">{{ stats.totalAmount || 0 }}</div>
            <div class="stat-unit">ml</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :sm="24" :lg="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-milk-tea"></i> 哺乳状态</span>
          </div>
          <div class="feeding-status">
            <div class="status-row">
              <span class="status-label">最近一次喂养：</span>
              <span class="status-value">{{ lastFeedingTime || '暂无记录' }}</span>
            </div>
            <div class="status-row">
              <span class="status-label">预计下次喂养：</span>
              <span class="status-value next-feeding">{{ nextFeedingTime || '暂无数据' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :sm="24" :lg="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-data-line"></i> 最新生长数据</span>
          </div>
          <div class="growth-status">
            <div class="status-row">
              <span class="status-label">最新身高：</span>
              <span class="status-value">{{ latestHeight || '暂无记录' }}</span>
            </div>
            <div class="status-row">
              <span class="status-label">最新体重：</span>
              <span class="status-value">{{ latestWeight || '暂无记录' }}</span>
            </div>
            <div class="status-row">
              <span class="status-label">测量日期：</span>
              <span class="status-value">{{ latestMeasureDate || '-' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-s-operation"></i> 快速操作</span>
          </div>
          <div class="quick-actions">
            <el-button type="primary" icon="el-icon-time" @click="goTo('/baby/timer')">计时器</el-button>
            <el-button type="success" icon="el-icon-milk-tea" @click="goTo('/baby/behavior/behavior-list', 'feeding')">添加喂奶</el-button>
            <el-button type="warning" icon="el-icon-tickets" @click="goTo('/baby/behavior/behavior-list', 'diaper')">添加换尿布</el-button>
            <el-button type="info" icon="el-icon-data-line" @click="goTo('/baby/measurement/measurement-list', 'height')">添加身高</el-button>
            <el-button type="danger" icon="el-icon-data-line" @click="goTo('/baby/measurement/measurement-list', 'weight')">添加体重</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listBehavior } from '@/api/baby/behavior'
import { listMeasurement } from '@/api/baby/measurement'

export default {
  name: 'BabyDashboard',
  data() {
    return {
      stats: {
        feedingCount: 0,
        diaperCount: 0,
        playCount: 0,
        totalAmount: 0
      },
      lastFeedingTime: '',
      nextFeedingTime: '',
      latestHeight: '',
      latestWeight: '',
      latestMeasureDate: ''
    }
  },
  created() {
    this.loadDashboard()
  },
  methods: {
    loadDashboard() {
      this.loadTodayStats()
    },
    loadTodayStats() {
      const today = this.getTodayStr()
      const todayEnd = today + ' 23:59:59'

      listBehavior({ behaviorType: 'feeding', params: { beginTime: today, endTime: todayEnd } }).then(res => {
        this.stats.feedingCount = res.total || 0
        let totalAmount = 0
        if (res.rows && res.rows.length > 0) {
          res.rows.forEach(item => {
            totalAmount += item.feedAmount || 0
          })
          const last = res.rows[0]
          this.lastFeedingTime = this.parseTime(last.startTime)
          const nextTime = new Date(last.startTime)
          nextTime.setHours(nextTime.getHours() + 3)
          this.nextFeedingTime = this.parseTime(nextTime)
        }
        this.stats.totalAmount = totalAmount
      }).catch(() => {})

      listBehavior({ behaviorType: 'diaper', params: { beginTime: today, endTime: todayEnd } }).then(res => {
        this.stats.diaperCount = res.total || 0
      }).catch(() => {})

      listBehavior({ behaviorType: 'play', params: { beginTime: today, endTime: todayEnd } }).then(res => {
        this.stats.playCount = res.total || 0
      }).catch(() => {})

      listMeasurement({ pageNum: 1, pageSize: 1, measureType: 'height' }).then(res => {
        if (res.rows && res.rows.length > 0) {
          const item = res.rows[0]
          this.latestHeight = item.value + ' cm'
          this.latestMeasureDate = item.measureDate
        }
      }).catch(() => {})

      listMeasurement({ pageNum: 1, pageSize: 1, measureType: 'weight' }).then(res => {
        if (res.rows && res.rows.length > 0) {
          const item = res.rows[0]
          this.latestWeight = item.value + ' kg'
          if (!this.latestMeasureDate) {
            this.latestMeasureDate = item.measureDate
          }
        }
      }).catch(() => {})
    },
    getTodayStr() {
      const now = new Date()
      const y = now.getFullYear()
      const m = String(now.getMonth() + 1).padStart(2, '0')
      const d = String(now.getDate()).padStart(2, '0')
      return y + '-' + m + '-' + d
    },
    goTo(path, openAdd) {
      if (openAdd) {
        this.$router.push({ path, query: { openAdd } }).catch(() => {})
      } else {
        this.$router.push(path).catch(() => {})
      }
    }
  }
}
</script>

<style scoped>
.dashboard-cards {
  margin-bottom: 10px;
}

.stat-card {
  cursor: pointer;
}

.stat-card >>> .el-card__body {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 28px;
}

.feeding-card .stat-icon {
  background: rgba(103, 194, 58, 0.1);
  color: #67C23A;
}

.diaper-card .stat-icon {
  background: rgba(230, 162, 60, 0.1);
  color: #E6A23C;
}

.play-card .stat-icon {
  background: rgba(64, 158, 255, 0.1);
  color: #409EFF;
}

.total-card .stat-icon {
  background: rgba(245, 108, 108, 0.1);
  color: #F56C6C;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 26px;
  font-weight: bold;
  color: #303133;
  display: inline;
}

.stat-unit {
  font-size: 13px;
  color: #909399;
  margin-left: 4px;
  display: inline;
}

.feeding-status,
.growth-status {
  padding: 10px 0;
}

.status-row {
  padding: 8px 0;
  border-bottom: 1px solid #EBEEF5;
}

.status-row:last-child {
  border-bottom: none;
}

.status-label {
  color: #909399;
  font-size: 14px;
}

.status-value {
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}

.next-feeding {
  color: #409EFF;
  font-weight: bold;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.quick-actions .el-button {
  min-width: 120px;
}

@media (max-width: 550px) {
  .stat-card >>> .el-card__body {
    padding: 14px;
  }

  .stat-icon {
    width: 44px;
    height: 44px;
    font-size: 22px;
    margin-right: 10px;
  }

  .stat-value {
    font-size: 20px;
  }
}
</style>