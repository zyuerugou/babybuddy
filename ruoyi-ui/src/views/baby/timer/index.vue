<template>
  <div class="app-container">
    <el-card class="timer-card" shadow="always">
      <div class="timer-display">
        <span class="timer-hours">{{ displayTime.hours }}</span>
        <span class="timer-separator">:</span>
        <span class="timer-minutes">{{ displayTime.minutes }}</span>
        <span class="timer-separator">:</span>
        <span class="timer-seconds">{{ displayTime.seconds }}</span>
      </div>

      <div class="timer-mode">
        <el-radio-group v-model="currentMode" size="medium" :disabled="isRunning">
          <el-radio-button label="feeding">
            <i class="el-icon-milk-tea"></i> 喂奶计时
          </el-radio-button>
          <el-radio-button label="play">
            <i class="el-icon-soccer"></i> 玩耍计时
          </el-radio-button>
        </el-radio-group>
      </div>

      <div class="timer-actions">
        <el-button v-if="!isRunning" type="primary" icon="el-icon-video-play" size="large" @click="startTimer">开始</el-button>
        <el-button v-if="isRunning" type="warning" icon="el-icon-video-pause" size="large" @click="pauseTimer">暂停</el-button>
        <el-button v-if="isRunning || elapsed > 0" type="danger" icon="el-icon-video-stop" size="large" @click="stopTimer">停止</el-button>
        <el-button v-if="elapsed > 0" icon="el-icon-refresh" size="large" @click="resetTimer">重置</el-button>
      </div>
    </el-card>

    <el-dialog title="保存行为记录" :visible.sync="saveDialogVisible" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="saveForm" :model="saveForm" :rules="saveRules" label-width="100px">
        <el-form-item label="行为类型">
          <el-tag v-if="mode === 'feeding'" type="success">喂奶</el-tag>
          <el-tag v-else-if="mode === 'play'" type="primary">玩耍</el-tag>
        </el-form-item>
        <el-form-item label="开始时间">
          <span>{{ saveForm.startTime }}</span>
        </el-form-item>
        <el-form-item label="持续时间">
          <span>{{ formatDuration(elapsed) }}</span>
        </el-form-item>

        <template v-if="mode === 'feeding'">
          <el-form-item label="喂养方式" prop="feedingType">
            <el-select v-model="saveForm.feedingType" placeholder="请选择喂养方式" style="width: 100%">
              <el-option label="母乳" value="breast" />
              <el-option label="配方奶" value="formula" />
            </el-select>
          </el-form-item>
          <el-form-item label="喂养量(ml)" prop="amount">
            <el-input-number v-model="saveForm.amount" :min="0" :max="500" style="width: 100%" />
          </el-form-item>
        </template>

        <template v-if="mode === 'play'">
          <el-form-item label="活动描述" prop="activity">
            <el-input v-model="saveForm.activity" placeholder="请输入活动描述" maxlength="200" />
          </el-form-item>
        </template>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="saveForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSave">保存记录</el-button>
        <el-button @click="saveDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addBehavior } from '@/api/baby/behavior'
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'Timer',
  data() {
    return {
      saveDialogVisible: false,
      saveForm: {
        behaviorType: '',
        feedingType: undefined,
        amount: undefined,
        activity: undefined,
        startTime: '',
        endTime: null,
        remark: undefined
      },
      saveRules: {
        feedingType: [
          { required: true, message: '请选择喂养方式', trigger: 'change' }
        ],
        activity: [
          { required: true, message: '请输入活动描述', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isRunning() {
      return this.$store.state.timer.isRunning
    },
    mode() {
      return this.$store.state.timer.mode
    },
    elapsed() {
      return this.$store.state.timer.elapsed
    },
    startTime() {
      return this.$store.state.timer.startTime
    },
    currentMode: {
      get() {
        return this.$store.state.timer.mode
      },
      set(val) {
        this.$store.commit('timer/SET_MODE', val)
      }
    },
    displayTime() {
      const h = Math.floor(this.elapsed / 3600)
      const m = Math.floor((this.elapsed % 3600) / 60)
      const s = this.elapsed % 60
      return {
        hours: String(h).padStart(2, '0'),
        minutes: String(m).padStart(2, '0'),
        seconds: String(s).padStart(2, '0')
      }
    }
  },
  beforeDestroy() {
    if (this.isRunning) {
      this.$store.dispatch('timer/stopTimer')
    }
  },
  methods: {
    parseTime,
    startTimer() {
      this.$store.dispatch('timer/startTimer', this.currentMode)
    },
    pauseTimer() {
      this.$store.dispatch('timer/pauseTimer')
    },
    stopTimer() {
      this.$store.dispatch('timer/pauseTimer')
      this.saveForm.behaviorType = this.mode
      this.saveForm.startTime = parseTime(this.startTime)
      this.saveForm.endTime = new Date()
      this.saveForm.feedingType = undefined
      this.saveForm.amount = undefined
      this.saveForm.activity = undefined
      this.saveForm.remark = undefined
      this.saveDialogVisible = true
    },
    resetTimer() {
      this.$store.dispatch('timer/resetTimer')
    },
    submitSave() {
      this.$refs['saveForm'].validate(valid => {
        if (valid) {
          const data = {
            behaviorType: this.saveForm.behaviorType,
            startTime: this.startTime ? parseTime(this.startTime, '{y}-{m}-{d} {h}:{i}:{s}') : undefined,
            endTime: parseTime(this.saveForm.endTime, '{y}-{m}-{d} {h}:{i}:{s}'),
            duration: this.elapsed,
            feedMethod: this.saveForm.feedingType,
            feedAmount: this.saveForm.amount,
            activity: this.saveForm.activity,
            remark: this.saveForm.remark
          }
          addBehavior(data).then(() => {
            this.$modal.msgSuccess('保存成功')
            this.saveDialogVisible = false
            this.$store.dispatch('timer/resetTimer')
          })
        }
      })
    },
    formatDuration(seconds) {
      if (!seconds) return '0秒'
      const h = Math.floor(seconds / 3600)
      const m = Math.floor((seconds % 3600) / 60)
      const s = seconds % 60
      if (h > 0) return h + '时' + m + '分' + s + '秒'
      if (m > 0) return m + '分' + s + '秒'
      return s + '秒'
    }
  }
}
</script>

<style scoped>
.timer-card {
  max-width: 600px;
  margin: 0 auto;
  text-align: center;
}

.timer-display {
  font-family: 'Courier New', monospace;
  margin: 40px 0 30px 0;
}

.timer-display span {
  font-size: 72px;
  font-weight: bold;
  color: #303133;
}

.timer-separator {
  margin: 0 4px;
}

.timer-mode {
  margin-bottom: 30px;
}

.timer-actions {
  margin-bottom: 30px;
}

.timer-actions .el-button {
  margin: 0 10px;
  min-width: 100px;
}

@media (max-width: 550px) {
  .timer-display span {
    font-size: 42px;
  }

  .timer-actions .el-button {
    margin: 5px;
  }
}
</style>