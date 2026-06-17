<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams" @submit.prevent>
        <el-form-item label="姓名">
          <el-input v-model="queryParams.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.phone" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 140px">
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
        <h3>用户列表</h3>
        <button class="btn-primary" @click="handleAdd">新增用户</button>
      </div>

      <el-table :data="tableData" v-loading="loading" class="clean-table">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="roleName" label="角色" width="120" />
        <el-table-column prop="gender" label="性别" width="80" align="center">
          <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <span class="status-tag" :class="row.status === 1 ? 'active' : 'inactive'">
              {{ row.status === 1 ? '正常' : '停用' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" align="center" fixed="right">
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
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="580px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" :prop="isEdit ? '' : 'password'">
              <el-input v-model="form.password" type="password" placeholder="留空则不修改" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="部门">
              <el-select v-model="form.deptId" placeholder="请选择" clearable style="width:100%">
                <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.roleId" placeholder="请选择" clearable style="width:100%">
                <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :value="1">正常</el-radio>
                <el-radio :value="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="头像">
          <el-upload :show-file-list="false" :before-upload="beforeAvatarUpload" :http-request="handleAvatarUpload">
            <div class="avatar-uploader">
              <el-avatar v-if="form.image" :src="form.image" :size="56" shape="square" />
              <span v-else class="avatar-placeholder">上传</span>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入" :rows="2" />
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
import { getUserPage, addUser, updateUser, deleteUser, uploadAvatar } from '../../../api/user'
import { getDeptPage } from '../../../api/dept'
import { getRoleList } from '../../../api/role'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const tableData = ref([])
const total = ref(0)
const formRef = ref(null)
const deptList = ref([])
const roleList = ref([])

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', phone: '', status: '' })
const form = reactive({ id: null, username: '', password: '', name: '', phone: '', email: '', gender: 1, status: 1, deptId: '', roleId: '', image: '', remark: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try { const res = await getUserPage(queryParams); tableData.value = res.data.rows; total.value = res.data.total } finally { loading.value = false }
}
const loadDepts = async () => { const res = await getDeptPage({ pageNum: 1, pageSize: 100 }); deptList.value = res.data.rows }
const loadRoleList = async () => { const res = await getRoleList(); roleList.value = res.data || [] }
const resetQuery = () => { Object.assign(queryParams, { name: '', phone: '', status: '', pageNum: 1 }); loadData() }
const handleAdd = () => { isEdit.value = false; Object.assign(form, { id: null, username: '', password: '', name: '', phone: '', email: '', gender: 1, status: 1, deptId: '', roleId: '', image: '', remark: '' }); dialogVisible.value = true }
const handleEdit = (row) => { isEdit.value = true; Object.assign(form, { ...row, password: '' }); dialogVisible.value = true }
const handleDelete = (row) => { ElMessageBox.confirm(`确定删除用户「${row.name}」？`, '提示', { type: 'warning' }).then(async () => { await deleteUser(row.id); ElMessage.success('删除成功'); loadData() }).catch(() => {}) }
const beforeAvatarUpload = (file) => { const ok = file.type.startsWith('image/') && file.size / 1024 / 1024 < 5; if (!ok) ElMessage.error('仅支持5MB以内图片'); return ok }
const handleAvatarUpload = async (options) => { try { const res = await uploadAvatar(options.file); form.image = res.data; ElMessage.success('上传成功') } catch (e) { ElMessage.error('上传失败：' + (e.message || '未知错误')); console.error('上传失败', e) } }
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try { if (isEdit.value) { await updateUser(form); ElMessage.success('修改成功') } else { await addUser(form); ElMessage.success('新增成功') }; dialogVisible.value = false; loadData() } finally { submitLoading.value = false }
  })
}
onMounted(() => { loadData(); loadDepts(); loadRoleList() })
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

.status-tag { display: inline-block; padding: 2px 10px; border-radius: 999px; font-size: 11px; font-weight: 600; letter-spacing: 0.03em; }
.status-tag.active { background: #EDF3EC; color: #346538; }
.status-tag.inactive { background: #FDEBEC; color: #9F2F2D; }

.clean-table :deep(.el-table__header th) { background: #FAFAF9; color: #787774; font-size: 12px; font-weight: 600; }
.clean-table :deep(.el-table__row td) { font-size: 13px; color: #2F3437; }

.avatar-uploader { width: 56px; height: 56px; border: 1px dashed #EAEAEA; border-radius: 8px; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: border-color 0.2s; }
.avatar-uploader:hover { border-color: #999; }
.avatar-placeholder { font-size: 12px; color: #999; }

.pagination { margin-top: 20px; justify-content: flex-end; }
</style>
