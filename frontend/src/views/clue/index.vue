<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="queryParams" @submit.prevent>
        <el-form-item label="客户姓名">
          <el-input v-model="queryParams.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.phone" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 140px">
            <el-option label="待分配" :value="1" />
            <el-option label="待跟进" :value="2" />
            <el-option label="跟进中" :value="3" />
            <el-option label="伪线索" :value="4" />
            <el-option label="转商机" :value="5" />
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
        <h3>线索列表</h3>
        <button class="btn-primary" @click="handleAdd">新增线索</button>
      </div>

      <el-table :data="tableData" v-loading="loading" class="clean-table">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="name" label="客户姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="channel" label="渠道" width="100" align="center">
          <template #default="{ row }">
            <span class="status-tag" :class="row.channel === 1 ? 'blue' : 'orange'">
              {{ row.channel === 1 ? '线上活动' : '推广介绍' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <span class="status-tag" :class="statusClass(row.status)">{{ statusLabel(row.status) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="assignName" label="归属人" width="100" />
        <el-table-column prop="nextTime" label="下次跟进" width="170" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="{ row }">
            <button class="btn-link" @click="handleAssign(row)">分配</button>
            <button class="btn-link" style="color:#346538" @click="handleTrack(row)">跟进</button>
            <button class="btn-link" style="color:#956400" @click="handleConvert(row)" :disabled="row.status === 5">转商机</button>
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

    <!-- 新增线索弹窗 -->
    <el-dialog v-model="addVisible" title="新增线索" width="520px" destroy-on-close>
      <el-form ref="addFormRef" :model="addForm" :rules="addRules" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="客户姓名" prop="name">
              <el-input v-model="addForm.name" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="addForm.phone" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="渠道" prop="channel">
              <el-select v-model="addForm.channel" placeholder="请选择" style="width:100%">
                <el-option label="线上活动" :value="1" />
                <el-option label="推广介绍" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="addForm.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="年龄">
              <el-input-number v-model="addForm.age" :min="1" :max="120" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="微信号">
              <el-input v-model="addForm.wechat" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="意向等级">
          <el-select v-model="addForm.level" placeholder="请选择" style="width:100%">
            <el-option label="近期学习" :value="1" />
            <el-option label="考虑中" :value="2" />
            <el-option label="了解" :value="3" />
            <el-option label="打酱油" :value="4" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn-ghost" @click="addVisible = false">取消</button>
        <button class="btn-primary" @click="handleSubmitAdd" :disabled="submitLoading">{{ submitLoading ? '提交中...' : '确定' }}</button>
      </template>
    </el-dialog>

    <!-- 分配弹窗 -->
    <el-dialog v-model="assignVisible" title="分配线索" width="380px" destroy-on-close>
      <el-form label-width="70px">
        <el-form-item label="归属人">
          <el-select v-model="assignUserId" placeholder="请选择" style="width:100%">
            <el-option v-for="u in userList" :key="u.id" :label="u.name" :value="u.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn-ghost" @click="assignVisible = false">取消</button>
        <button class="btn-primary" @click="handleSubmitAssign">确定</button>
      </template>
    </el-dialog>

    <!-- 跟进弹窗 -->
    <el-dialog v-model="trackVisible" title="线索跟进" width="480px" destroy-on-close>
      <el-form ref="trackFormRef" :model="trackForm" :rules="trackRules" label-width="90px">
        <el-form-item label="跟进内容" prop="content">
          <el-input v-model="trackForm.content" type="textarea" :rows="4" placeholder="请输入跟进内容" />
        </el-form-item>
        <el-form-item label="下次跟进时间">
          <el-date-picker v-model="trackForm.nextTime" type="datetime" placeholder="选择" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="btn-ghost" @click="trackVisible = false">取消</button>
        <button class="btn-primary" @click="handleSubmitTrack">确定</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCluePage, addClue, assignClue, trackClue, convertClue } from '../../api/clue'
import { getUserPage } from '../../api/user'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const addFormRef = ref(null)
const trackFormRef = ref(null)

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', phone: '', status: '' })

const addVisible = ref(false)
const addForm = reactive({ name: '', phone: '', channel: '', gender: 1, age: '', wechat: '', level: '' })
const addRules = { name: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }], phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }], channel: [{ required: true, message: '请选择渠道', trigger: 'change' }] }

const assignVisible = ref(false)
const assignClueId = ref(null)
const assignUserId = ref(null)
const userList = ref([])

const trackVisible = ref(false)
const trackClueId = ref(null)
const trackForm = reactive({ content: '', nextTime: '' })
const trackRules = { content: [{ required: true, message: '请输入跟进内容', trigger: 'blur' }] }

const statusLabel = (s) => ({ 1: '待分配', 2: '待跟进', 3: '跟进中', 4: '伪线索', 5: '转商机' }[s] || '')
const statusClass = (s) => ({ 1: 'gray', 2: 'orange', 3: 'blue', 4: 'inactive', 5: 'active' }[s] || '')

const loadData = async () => { loading.value = true; try { const res = await getCluePage(queryParams); tableData.value = res.data.rows; total.value = res.data.total } finally { loading.value = false } }
const loadUsers = async () => { const res = await getUserPage({ pageNum: 1, pageSize: 100 }); userList.value = res.data.rows }
const resetQuery = () => { Object.assign(queryParams, { name: '', phone: '', status: '', pageNum: 1 }); loadData() }
const handleAdd = () => { Object.assign(addForm, { name: '', phone: '', channel: '', gender: 1, age: '', wechat: '', level: '' }); addVisible.value = true }
const handleSubmitAdd = async () => { if (!addFormRef.value) return; await addFormRef.value.validate(async (valid) => { if (!valid) return; submitLoading.value = true; try { await addClue(addForm); ElMessage.success('新增成功'); addVisible.value = false; loadData() } finally { submitLoading.value = false } }) }
const handleAssign = (row) => { assignClueId.value = row.id; assignUserId.value = row.userId || null; assignVisible.value = true }
const handleSubmitAssign = async () => { if (!assignUserId.value) { ElMessage.warning('请选择归属人'); return }; await assignClue(assignClueId.value, assignUserId.value); ElMessage.success('分配成功'); assignVisible.value = false; loadData() }
const handleTrack = (row) => { trackClueId.value = row.id; Object.assign(trackForm, { content: '', nextTime: '' }); trackVisible.value = true }
const handleSubmitTrack = async () => { if (!trackFormRef.value) return; await trackFormRef.value.validate(async (valid) => { if (!valid) return; await trackClue(trackClueId.value, trackForm); ElMessage.success('跟进成功'); trackVisible.value = false; loadData() }) }
const handleConvert = (row) => { ElMessageBox.confirm(`确定将线索「${row.name}」转为商机？`, '提示', { type: 'warning' }).then(async () => { await convertClue(row.id); ElMessage.success('转商机成功'); loadData() }).catch(() => {}) }

onMounted(() => { loadData(); loadUsers() })
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
.btn-link { background: none; border: none; color: #111; font-size: 13px; font-weight: 500; cursor: pointer; padding: 0; margin: 0 6px; text-decoration: underline; text-underline-offset: 3px; transition: color 0.2s; }
.btn-link:hover { color: #555; }
.btn-link:disabled { opacity: 0.4; cursor: not-allowed; text-decoration: none; }

.status-tag { display: inline-block; padding: 2px 10px; border-radius: 999px; font-size: 11px; font-weight: 600; letter-spacing: 0.03em; }
.status-tag.active { background: #EDF3EC; color: #346538; }
.status-tag.inactive { background: #FDEBEC; color: #9F2F2D; }
.status-tag.blue { background: #E1F3FE; color: #1F6C9F; }
.status-tag.orange { background: #FBF3DB; color: #956400; }
.status-tag.gray { background: #F2F2F0; color: #787774; }

.clean-table :deep(.el-table__header th) { background: #FAFAF9; color: #787774; font-size: 12px; font-weight: 600; }
.clean-table :deep(.el-table__row td) { font-size: 13px; color: #2F3437; }

.pagination { margin-top: 20px; justify-content: flex-end; }
</style>
