<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="婴儿姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入婴儿姓名"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="queryParams.gender" placeholder="请选择性别" clearable style="width: 120px">
          <el-option label="男" value="0" />
          <el-option label="女" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['baby:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['baby:info:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="babyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="婴儿姓名" align="center" prop="name" width="150" />
      <el-table-column label="性别" align="center" prop="gender" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.gender === '0' ? '男' : scope.row.gender === '1' ? '女' : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出生日期" align="center" prop="birthDate" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.birthDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['baby:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['baby:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="婴儿姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入婴儿姓名" maxlength="30" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="0" />
            <el-option label="女" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
            v-model="form.birthDate"
            type="date"
            placeholder="请选择出生日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
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
import { listBaby, getBaby, addBaby, updateBaby, delBaby } from '@/api/baby/baby'

export default {
  name: 'BabyInfo',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      babyList: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        gender: null
      },
      form: {},
      rules: {
        name: [
          { required: true, message: '婴儿姓名不能为空', trigger: 'blur' },
          { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        birthDate: [
          { required: true, message: '请选择出生日期', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listBaby(this.queryParams).then(response => {
        this.babyList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        name: null,
        gender: null
      }
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.babyId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加婴儿'
    },
    handleUpdate(row) {
      this.reset()
      const babyId = row.babyId || this.ids[0]
      getBaby(babyId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改婴儿'
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.babyId) {
            updateBaby(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addBaby(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        babyId: null,
        name: null,
        gender: null,
        birthDate: null,
        remark: null
      }
      this.resetForm('form')
    },
    handleDelete(row) {
      const babyIds = row.babyId || this.ids
      this.$modal.confirm('是否确认删除选中的婴儿信息？').then(() => {
        return delBaby(babyIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
