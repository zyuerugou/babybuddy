<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="行为类型" prop="behaviorType">
        <el-select v-model="queryParams.behaviorType" placeholder="请选择行为类型" clearable style="width: 240px">
          <el-option label="喂奶" value="feeding" />
          <el-option label="换尿布" value="diaper" />
          <el-option label="玩耍" value="play" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['baby:behavior:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['baby:behavior:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="behaviorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="行为类型" align="center" prop="behaviorType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.behaviorType === 'feeding'" type="success" effect="dark">
            <i class="el-icon-milk-tea"></i> 喂奶
          </el-tag>
          <el-tag v-else-if="scope.row.behaviorType === 'diaper'" type="warning" effect="dark">
            <i class="el-icon-tickets"></i> 换尿布
          </el-tag>
          <el-tag v-else-if="scope.row.behaviorType === 'play'" type="primary" effect="dark">
            <i class="el-icon-soccer"></i> 玩耍
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="详情" align="center" prop="detail" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span v-if="scope.row.behaviorType === 'feeding'">
            {{ scope.row.feedMethod === 'breast' ? '母乳' : '配方奶' }} {{ scope.row.feedAmount ? scope.row.feedAmount + 'ml' : '' }}
          </span>
          <span v-else-if="scope.row.behaviorType === 'diaper'">
            {{ scope.row.diaperType === 'wet' ? '湿' : scope.row.diaperType === 'dry' ? '干' : '都有' }}
          </span>
          <span v-else>
            {{ scope.row.activity || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime ? parseTime(scope.row.endTime) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时长" align="center" prop="duration" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.duration ? formatDuration(scope.row.duration) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['baby:behavior:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['baby:behavior:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="行为类型" prop="behaviorType">
          <el-select v-model="form.behaviorType" placeholder="请选择行为类型" style="width: 100%" @change="handleTypeChange">
            <el-option label="喂奶" value="feeding" />
            <el-option label="换尿布" value="diaper" />
            <el-option label="玩耍" value="play" />
          </el-select>
        </el-form-item>

        <template v-if="form.behaviorType === 'feeding'">
          <el-form-item label="喂养方式" prop="feedMethod">
            <el-select v-model="form.feedMethod" placeholder="请选择喂养方式" style="width: 100%">
              <el-option label="母乳" value="breast" />
              <el-option label="配方奶" value="formula" />
            </el-select>
          </el-form-item>
          <el-form-item label="喂养量(ml)" prop="feedAmount">
            <el-input-number v-model="form.feedAmount" :min="0" :max="500" placeholder="请输入喂养量" style="width: 100%" />
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择开始时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker v-model="form.endTime" type="datetime" placeholder="请选择结束时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
          </el-form-item>
        </template>

        <template v-if="form.behaviorType === 'diaper'">
          <el-form-item label="尿布类型" prop="diaperType">
            <el-select v-model="form.diaperType" placeholder="请选择尿布类型" style="width: 100%">
              <el-option label="湿" value="wet" />
              <el-option label="干" value="dry" />
              <el-option label="都有" value="both" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间" prop="startTime">
            <el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
          </el-form-item>
        </template>

        <template v-if="form.behaviorType === 'play'">
          <el-form-item label="活动描述" prop="activity">
            <el-input v-model="form.activity" placeholder="请输入活动描述" maxlength="200" />
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择开始时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker v-model="form.endTime" type="datetime" placeholder="请选择结束时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
          </el-form-item>
        </template>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBehavior, getBehavior, addBehavior, updateBehavior, delBehavior } from '@/api/baby/behavior'

export default {
  name: 'Behavior',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      behaviorList: [],
      title: '',
      open: false,
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        behaviorType: undefined
      },
      form: {},
      rules: {
        behaviorType: [
          { required: true, message: '请选择行为类型', trigger: 'change' }
        ],
        startTime: [
          { required: true, message: '请选择时间', trigger: 'change' }
        ],
        feedMethod: [
          { required: true, message: '请选择喂养方式', trigger: 'change' }
        ],
        diaperType: [
          { required: true, message: '请选择尿布类型', trigger: 'change' }
        ],
        activity: [
          { required: true, message: '请输入活动描述', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    if (this.$route.query.openAdd) {
      this.$nextTick(() => {
        this.handleAdd()
        this.form.behaviorType = this.$route.query.openAdd
      })
    }
  },
  methods: {
    getList() {
      this.loading = true
      listBehavior(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.behaviorList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        behaviorId: undefined,
        behaviorType: '',
        feedMethod: undefined,
        feedAmount: undefined,
        diaperType: undefined,
        activity: undefined,
        startTime: undefined,
        endTime: undefined,
        duration: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.behaviorId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleTypeChange() {
      this.form.feedMethod = undefined
      this.form.feedAmount = undefined
      this.form.diaperType = undefined
      this.form.activity = undefined
      this.form.startTime = undefined
      this.form.endTime = undefined
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增行为记录'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.behaviorId
      getBehavior(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改行为记录'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.behaviorId) {
            updateBehavior(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addBehavior(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.behaviorId
      this.$modal.confirm('是否确认删除选中的行为记录？').then(() => {
        return delBehavior(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    formatDuration(seconds) {
      if (!seconds) return '0分'
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