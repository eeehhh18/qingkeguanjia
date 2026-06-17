<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams" @submit.prevent>
        <el-form-item label="部门名称">
          <el-input v-model="queryParams.name" placeholder="请输入" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable @clear="loadData" style="width: 140px">
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="0" />
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
        <h3>部门列表</h3>
        <button class="btn-primary" @click="handleAdd">新增部门</button>
      </div>

      <el-table :data="tableData" v-loading="loading" class="clean-table">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <span class="status-tag" :class="row.status === 1 ? 'active' : 'inactive'">
              {{ row.status === 1 ? '正常' : '停用' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="160" align="center">
          <template #default="{ row }">
            <button class="btn-link" @click="handleEdit(row)">编辑</button>
            <button class="btn-link danger" @click="handleDelete(row)">删除</button>
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑部门' : '新增部门'" width="420px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="0">停用</el-radio>
          </el-radio-group>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeptPage, addDept, updateDept, deleteDept } from '../../../api/dept'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const tableData = ref([])
const total = ref(0)
const formRef = ref(null)

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', status: '' })
const form = reactive({ id: null, name: '', status: 1 })
const rules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDeptPage(queryParams)
    tableData.value = res.data.rows
    total.value = res.data.total
  } finally { loading.value = false }
}

const resetQuery = () => {
  queryParams.name = ''
  queryParams.status = ''
  queryParams.pageNum = 1
  loadData()
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, name: '', status: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { id: row.id, name: row.name, status: row.status })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除部门「${row.name}」？`, '提示', { type: 'warning' }).then(async () => {
    await deleteDept(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (isEdit.value) {
        await updateDept(form)
        ElMessage.success('修改成功')
      } else {
        await addDept(form)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      loadData()
    } finally { submitLoading.value = false }
  })
}

onMounted(loadData)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap');

.page-container {
  font-family: 'Plus Jakarta Sans', -apple-system, sans-serif;
  display: flex; flex-direction: column; gap: 16px;
}

.search-bar {
  background: #FFFFFF; border: 1px solid #EAEAEA; border-radius: 12px;
  padding: 20px 24px 4px;
}

.table-section {
  background: #FFFFFF; border: 1px solid #EAEAEA; border-radius: 12px;
  padding: 24px;
}
.section-header {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 20px;
}
.section-header h3 {
  font-size: 15px; font-weight: 700; color: #111; margin: 0;
}

/* 按钮 */
.btn-primary {
  height: 36px; padding: 0 18px;
  border: none; border-radius: 6px;
  background: #111; color: #fff;
  font-size: 13px; font-weight: 600; cursor: pointer;
  transition: all 0.2s;
}
.btn-primary:hover { background: #333; }
.btn-primary:active { transform: scale(0.98); }
.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }

.btn-ghost {
  height: 36px; padding: 0 18px;
  border: 1px solid #EAEAEA; border-radius: 6px;
  background: #fff; color: #2F3437;
  font-size: 13px; font-weight: 500; cursor: pointer;
  transition: all 0.2s; margin-left: 8px;
}
.btn-ghost:hover { border-color: #D0D0CC; background: #F7F7F5; }

.btn-link {
  background: none; border: none; color: #111;
  font-size: 13px; font-weight: 500; cursor: pointer;
  padding: 0; margin: 0 8px;
  text-decoration: underline; text-underline-offset: 3px;
  transition: color 0.2s;
}
.btn-link:hover { color: #555; }
.btn-link.danger { color: #D14D44; }
.btn-link.danger:hover { color: #B33B39; }

/* 状态标签 */
.status-tag {
  display: inline-block; padding: 2px 10px;
  border-radius: 999px; font-size: 11px; font-weight: 600;
  letter-spacing: 0.03em;
}
.status-tag.active { background: #EDF3EC; color: #346538; }
.status-tag.inactive { background: #FDEBEC; color: #9F2F2D; }

/* 表格样式覆盖 */
.clean-table :deep(.el-table__header th) {
  background: #FAFAF9; color: #787774; font-size: 12px;
  font-weight: 600; letter-spacing: 0.02em;
}
.clean-table :deep(.el-table__row td) {
  font-size: 13px; color: #2F3437;
}
.clean-table :deep(.el-table) { border-radius: 8px; }
.clean-table :deep(.el-table__border-left-patch) { display: none; }

.pagination { margin-top: 20px; justify-content: flex-end; }
</style>
