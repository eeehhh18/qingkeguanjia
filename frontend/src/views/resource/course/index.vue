<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams" @submit.prevent>
        <el-form-item label="课程名称">
          <el-input v-model="queryParams.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="学科">
          <el-select v-model="queryParams.subject" placeholder="请选择" clearable style="width:200px">
            <el-option v-for="(label, key) in subjectMap" :key="key" :label="label" :value="Number(key)" />
          </el-select>
        </el-form-item>
        <el-form-item label="适用人群">
          <el-select v-model="queryParams.target" placeholder="请选择" clearable style="width: 160px">
            <el-option v-for="(label, key) in targetMap" :key="key" :label="label" :value="Number(key)" />
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
        <h3>课程列表</h3>
        <button class="btn-primary" @click="handleAdd">新增课程</button>
      </div>

      <el-table :data="tableData" v-loading="loading" class="clean-table">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="subject" label="学科" align="center">
          <template #default="{ row }">{{ subjectMap[row.subject] }}</template>
        </el-table-column>
        <el-table-column prop="name" label="课程名称" align="center" />
        <el-table-column prop="price" label="价格(元)" align="center" />
        <el-table-column prop="target" label="适用人群" align="center">
          <template #default="{ row }">{{ targetMap[row.target] }}</template>
        </el-table-column>
        <el-table-column prop="description" label="课程介绍" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="170" align="center" />
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="课程名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学科" prop="subject">
              <el-select v-model="form.subject" placeholder="请选择" style="width:100%">
                <el-option v-for="(label, key) in subjectMap" :key="key" :label="label" :value="Number(key)" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格(元)" prop="price">
              <el-input-number v-model="form.price" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用人群" prop="target">
              <el-select v-model="form.target" placeholder="请选择" style="width:100%">
                <el-option v-for="(label, key) in targetMap" :key="key" :label="label" :value="Number(key)" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="课程介绍" prop="description">
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCoursePage, addCourse, updateCourse, deleteCourse } from '../../../api/course'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const tableData = ref([])
const total = ref(0)

const subjectMap = {
  1: 'AI智能应用开发(Java)',
  2: 'AI大模型开发(Python)',
  3: 'AI鸿蒙开发',
  4: 'AI大数据',
  5: 'AI嵌入式',
  6: 'AI测试',
  7: 'AI运维'
}
const targetMap = { 1: '小白学员', 2: '初级程序员', 3: '中级程序员' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', subject: null, target: null })
const form = reactive({ id: null, name: '', subject: null, price: 0, target: null, description: '' })
const rules = {
  name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  target: [{ required: true, message: '请选择适用人群', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getCoursePage(queryParams)
    tableData.value = res.data.rows || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryParams.name = ''
  queryParams.subject = null
  queryParams.target = null
  queryParams.pageNum = 1
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增课程'
  Object.assign(form, { id: null, name: '', subject: null, price: 0, target: null, description: '' })
  dialogVisible.value = true
  formRef.value?.clearValidate()
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑课程'
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
        await updateCourse(form)
        ElMessage.success('修改成功')
      } else {
        await addCourse(form)
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
      await deleteCourse(id)
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