<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams" @submit.prevent>
        <el-form-item label="渠道">
          <el-select v-model="queryParams.channel" placeholder="请选择" clearable style="width: 180px">
            <el-option label="线上活动" :value="1" />
            <el-option label="推广介绍" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="queryParams.type" placeholder="请选择" clearable style="width: 180px">
            <el-option label="课程折扣" :value="1" />
            <el-option label="代金券" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <button class="btn-primary" @click="loadData">查询</button>
          <button class="btn-ghost" @click="resetQuery">重置</button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区 -->
    <div class="table-section">
      <div class="section-header">
        <h3>活动列表</h3>
        <button class="btn-primary" @click="handleAdd">新增活动</button>
      </div>

      <el-table :data="tableData" v-loading="loading" class="clean-table">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="name" label="活动名称" align="center" />
        <el-table-column prop="channel" label="渠道" align="center">
          <template #default="{ row }">{{ row.channel === 1 ? '线上活动' : '推广介绍' }}</template>
        </el-table-column>
        <el-table-column prop="type" label="活动类型" align="center">
          <template #default="{ row }">{{ row.type === 1 ? '课程折扣' : '代金券' }}</template>
        </el-table-column>
        <el-table-column label="优惠内容" align="center">
          <template #default="{ row }">
            <span v-if="row.type === 1">{{ row.discount }}折</span>
            <span v-else>{{ row.voucher }}元</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="170" align="center" />
        <el-table-column prop="endTime" label="结束时间" width="170" align="center" />
        <el-table-column label="状态" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.startTime > now" type="info">未开始</el-tag>
            <el-tag v-else-if="row.endTime < now" type="danger">已结束</el-tag>
            <el-tag v-else type="success">进行中</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="{ row }">
            <button class="btn-link" @click="handleEdit(row)">编辑</button>
            <button class="btn-link danger" @click="handleDelete(row.id)">删除</button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
      />
    </div>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="渠道" prop="channel">
              <el-select v-model="form.channel" placeholder="请选择" style="width:100%">
                <el-option label="线上活动" :value="1" />
                <el-option label="推广介绍" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择" style="width:100%">
                <el-option label="课程折扣" :value="1" />
                <el-option label="代金券" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="form.endTime" type="datetime" placeholder="请选择" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="折扣" prop="discount" v-if="form.type === 1">
              <el-input-number v-model="form.discount" :min="0" :max="10" :precision="1" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="代金券(元)" prop="voucher" v-if="form.type === 2">
              <el-input-number v-model="form.voucher" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动简介" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn-ghost" @click="dialogVisible = false">取消</button>
        <button class="btn-primary" @click="handleSubmit" :disabled="submitLoading">{{ submitLoading ? '提交中...' : '确定' }}</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getActivityPage, addActivity, updateActivity, deleteActivity } from '../../../api/activity'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const tableData = ref([])
const total = ref(0)
const now = computed(() => new Date())

const queryParams = reactive({ pageNum: 1, pageSize: 10, channel: null, type: null })
const form = reactive({ id: null, name: '', channel: null, type: null, startTime: '', endTime: '', discount: null, voucher: null, description: '' })
const rules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  channel: [{ required: true, message: '请选择渠道', trigger: 'change' }],
  type: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  discount: [{ required: true, message: '请输入折扣', trigger: 'blur' }],
  voucher: [{ required: true, message: '请输入代金券金额', trigger: 'blur' }],
  description: [{ required: true, message: '请输入活动简介', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getActivityPage(queryParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryParams.channel = null
  queryParams.type = null
  queryParams.pageNum = 1
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增活动'
  Object.assign(form, { id: null, name: '', channel: null, type: null, startTime: '', endTime: '', discount: null, voucher: null, description: '' })
  dialogVisible.value = true
  formRef.value?.clearValidate()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑活动'
  Object.assign(form, row)
  dialogVisible.value = true
  formRef.value?.clearValidate()
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (form.id) {
        await updateActivity(form)
        ElMessage.success('修改成功')
      } else {
        await addActivity(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      loadData()
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteActivity(id)
      ElMessage.success('删除成功')
      loadData()
    })
    .catch(() => {})
}

onMounted(loadData)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap');

.page-container { font-family: 'Plus Jakarta Sans', -apple-system, sans-serif; display: flex; flex-direction: column; gap: 16px; }
.search-bar { background: #FFFFFF; border: 1px solid #EAEAEA; border-radius: 12px; padding: 20px 24px 4px; }
.table-section { background: #FFFFFF; border: 1px solid #EAEAEA; border-radius: 12px; padding: 24px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.section-header h3 { font-size: 15px; font-weight: 700; color: #111; margin: 0; }

.btn-primary { height: 36px; padding: 0 18px; border: none; border-radius: 6px; background: #111; color: #fff; font-size: 13px; font-weight: 600; cursor: pointer; transition: all 0.2s; }
.btn-primary:hover { background: #333; }
.btn-primary:active { transform: scale(0.98); }
.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }
.btn-ghost { height: 36px; padding: 0 18px; border: 1px solid #EAEAEA; border-radius: 6px; background: #fff; color: #2F3437; font-size: 13px; font-weight: 500; cursor: pointer; transition: all 0.2s; margin-left: 8px; }
.btn-ghost:hover { border-color: #D0D0CC; background: #F7F7F5; }
.btn-link { background: none; border: none; color: #111; font-size: 13px; font-weight: 500; cursor: pointer; padding: 0; margin: 0 8px; text-decoration: underline; text-underline-offset: 3px; transition: color 0.2s; }
.btn-link:hover { color: #555; }
.btn-link.danger { color: #D14D44; }
.btn-link.danger:hover { color: #B33B39; }

.clean-table :deep(.el-table__header th) { background: #FAFAF9; color: #787774; font-size: 12px; font-weight: 600; }
.clean-table :deep(.el-table__row td) { font-size: 13px; color: #2F3437; }

.pagination { margin-top: 20px; justify-content: flex-end; }
</style>