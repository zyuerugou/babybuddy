<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="测量类型" prop="measureType">
        <el-select v-model="queryParams.measureType" placeholder="请选择测量类型" clearable style="width: 240px">
          <el-option label="身高" value="height" />
          <el-option label="体重" value="weight" />
        </el-select>
      </el-form-item>
      <el-form-item label="日期范围">
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['baby:measurement:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['baby:measurement:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="measurementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="测量类型" align="center" prop="measureType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.measureType === 'height'" type="primary">身高</el-tag>
          <el-tag v-else-if="scope.row.measureType === 'weight'" type="success">体重</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="数值" align="center" prop="value" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.value }}{{ scope.row.measureType === 'height' ? ' cm' : ' kg' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="测量日期" align="center" prop="measureDate" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.measureDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['baby:measurement:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['baby:measurement:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="测量类型" prop="measureType">
          <el-select v-model="form.measureType" placeholder="请选择测量类型" style="width: 100%">
            <el-option label="身高" value="height" />
            <el-option label="体重" value="weight" />
          </el-select>
        </el-form-item>
        <el-form-item :label="form.measureType === 'height' ? '身高(cm)' : form.measureType === 'weight' ? '体重(kg)' : '数值'" prop="value">
          <el-input-number v-model="form.value" :min="0" :precision="2" :step="0.1" style="width: 100%" :placeholder="form.measureType === 'height' ? '请输入身高' : '请输入体重'" />
        </el-form-item>
        <el-form-item label="测量日期" prop="measureDate">
          <el-date-picker v-model="form.measureDate" type="date" placeholder="请选择测量日期" value-format="yyyy-MM-dd" style="width: 100%" />
        </el-form-item>
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
import { listMeasurement, getMeasurement, addMeasurement, updateMeasurement, delMeasurement } from '@/api/baby/measurement'

export default {
  name: 'Measurement',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      measurementList: [],
      title: '',
      open: false,
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        measureType: undefined
      },
      form: {},
      rules: {
        measureType: [
          { required: true, message: '请选择测量类型', trigger: 'change' }
        ],
        value: [
          { required: true, message: '请输入数值', trigger: 'blur' }
        ],
        measureDate: [
          { required: true, message: '请选择测量日期', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
    if (this.$route.query.openAdd) {
      this.$nextTick(() => {
        this.handleAdd()
        this.form.measureType = this.$route.query.openAdd
      })
    }
  },
  methods: {
    getList() {
      this.loading = true
      listMeasurement(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.measurementList = response.rows
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
        measureId: undefined,
        measureType: '',
        value: undefined,
        measureDate: undefined,
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
      this.ids = selection.map(item => item.measureId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增测量记录'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.measureId
      getMeasurement(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改测量记录'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.measureId != undefined) {
            updateMeasurement(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addMeasurement(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.measureId
      this.$modal.confirm('是否确认删除选中的测量记录？').then(() => {
        return delMeasurement(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>