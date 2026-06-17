<template>
  <div class="register-wrap">
    <!-- 顶部品牌导航 -->
    <div class="top-bar">
      <router-link to="/login" class="back-link">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M19 12H5m7-7l-7 7 7 7"/></svg>
        <span>返回登录</span>
      </router-link>
      <div class="top-logo">
        <svg viewBox="0 0 48 48" fill="none" width="28" height="28">
          <rect width="48" height="48" rx="12" fill="#111"/>
          <path d="M14 34V18l10-8 10 8v16H28v-8h-8v8H14z" fill="white" fill-opacity="0.9"/>
        </svg>
        <span>轻客管家</span>
      </div>
    </div>

    <!-- 注册表单主体 -->
    <div class="register-main">
      <div class="register-content">
        <div class="form-header">
          <div class="eyebrow">CREATE ACCOUNT</div>
          <h1>创建新账户</h1>
          <p>填写以下信息，加入轻客管家开始管理您的客户资源</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" class="register-form" @keyup.enter="handleRegister">
          <div class="form-row">
            <el-form-item prop="username" class="form-col">
              <label class="field-label">用户名 <span class="req">*</span></label>
              <el-input v-model="form.username" placeholder="请输入用户名" size="large" :prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="name" class="form-col">
              <label class="field-label">姓名 <span class="req">*</span></label>
              <el-input v-model="form.name" placeholder="请输入真实姓名" size="large" :prefix-icon="UserFilled" />
            </el-form-item>
          </div>

          <div class="form-row">
            <el-form-item prop="phone" class="form-col">
              <label class="field-label">手机号 <span class="req">*</span></label>
              <el-input v-model="form.phone" placeholder="请输入手机号" size="large" :prefix-icon="Phone" />
            </el-form-item>
            <el-form-item prop="email" class="form-col">
              <label class="field-label">邮箱 <span class="req">*</span></label>
              <el-input v-model="form.email" placeholder="请输入邮箱地址" size="large" :prefix-icon="Message" />
            </el-form-item>
          </div>

          <div class="form-row">
            <el-form-item prop="password" class="form-col">
              <label class="field-label">密码 <span class="req">*</span></label>
              <el-input v-model="form.password" type="password" placeholder="至少6位" size="large" show-password :prefix-icon="Lock" />
            </el-form-item>
            <el-form-item prop="confirmPassword" class="form-col">
              <label class="field-label">确认密码 <span class="req">*</span></label>
              <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" size="large" show-password :prefix-icon="Lock" />
            </el-form-item>
          </div>

          <el-form-item>
            <button class="register-btn" :class="{ 'is-loading': loading }" @click.prevent="handleRegister" type="button">
              <span v-if="!loading">创建账户</span>
              <span v-else>注册中...</span>
            </button>
          </el-form-item>
        </el-form>

        <div class="form-footer">
          <span>已有账户？</span>
          <router-link to="/login" class="link">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, UserFilled, Phone, Message, Lock } from '@element-plus/icons-vue'
import { register } from '../../api/login'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '', name: '', phone: '', email: '', password: '', confirmPassword: ''
})

const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) callback(new Error('两次输入密码不一致'))
  else callback()
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请设置密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      await register({ username: form.username, name: form.name, phone: form.phone, email: form.email, password: form.password })
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } catch (e) { /* */ } finally { loading.value = false }
  })
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Instrument+Serif&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap');

.register-wrap {
  min-height: 100vh;
  background: #FBFBFA;
  font-family: 'Plus Jakarta Sans', -apple-system, sans-serif;
  color: #2F3437;
}

/* 顶部导航 */
.top-bar {
  display: flex; align-items: center; justify-content: space-between;
  padding: 20px 48px;
  border-bottom: 1px solid #EAEAEA;
}
.back-link {
  display: flex; align-items: center; gap: 8px;
  color: #787774; text-decoration: none; font-size: 13px; font-weight: 500;
  transition: color 0.2s;
}
.back-link svg { width: 16px; height: 16px; }
.back-link:hover { color: #2F3437; }

.top-logo {
  display: flex; align-items: center; gap: 10px;
  font-size: 15px; font-weight: 700; color: #111; letter-spacing: -0.01em;
}

/* 主体 */
.register-main {
  display: flex; justify-content: center;
  padding: 48px 24px 80px;
}
.register-content { width: 100%; max-width: 680px; }

.form-header { margin-bottom: 40px; }
.eyebrow {
  display: inline-block; padding: 5px 12px;
  border-radius: 4px; margin-bottom: 16px;
  background: #F2F2F0; color: #787774;
  font-size: 10px; font-weight: 600;
  letter-spacing: 0.15em; text-transform: uppercase;
}
.form-header h1 {
  font-family: 'Instrument Serif', 'Georgia', serif;
  font-size: 36px; font-weight: 400;
  color: #111; margin: 0 0 10px;
  letter-spacing: -0.03em; line-height: 1.15;
}
.form-header p {
  font-size: 15px; color: #787774; line-height: 1.6; margin: 0;
  max-width: 480px;
}

/* 表单 */
.form-row {
  display: flex; gap: 16px;
}
.form-col { flex: 1; }

.field-label {
  display: block; font-size: 12px; font-weight: 600;
  color: #2F3437; margin-bottom: 6px; letter-spacing: 0.02em;
}
.req { color: #D14D44; }

.register-form :deep(.el-input__wrapper) {
  background: #FFFFFF;
  border: 1px solid #EAEAEA;
  border-radius: 8px; box-shadow: none;
  padding: 4px 14px; height: 46px;
  transition: all 0.2s;
}
.register-form :deep(.el-input__wrapper:hover) {
  border-color: #D0D0CC;
}
.register-form :deep(.el-input__wrapper.is-focus) {
  border-color: #111;
  box-shadow: 0 0 0 3px rgba(17,17,17,0.06);
}
.register-form :deep(.el-input__inner) { color: #2F3437; font-size: 14px; }
.register-form :deep(.el-input__inner::placeholder) { color: #B0B0AC; }
.register-form :deep(.el-input__prefix .el-icon) { color: #999; font-size: 16px; }
.register-form :deep(.el-form-item) { margin-bottom: 20px; }
.register-form :deep(.el-form-item__error) { font-size: 12px; }

/* 按钮 */
.register-btn {
  width: 100%; height: 48px; margin-top: 8px;
  border: none; border-radius: 6px;
  background: #111; color: #fff;
  font-size: 14px; font-weight: 600;
  font-family: 'Plus Jakarta Sans', sans-serif;
  letter-spacing: 0.05em; cursor: pointer;
  transition: all 0.2s;
}
.register-btn:hover { background: #333; }
.register-btn:active { transform: scale(0.98); }
.register-btn.is-loading { pointer-events: none; opacity: 0.7; }

.form-footer {
  text-align: center; margin-top: 24px;
  font-size: 13px; color: #787774;
}
.link {
  color: #111; text-decoration: underline; text-underline-offset: 3px;
  margin-left: 6px; font-weight: 600; transition: color 0.2s;
}
.link:hover { color: #555; }

/* 响应式 */
@media (max-width: 768px) {
  .top-bar { padding: 16px 20px; }
  .register-main { padding: 32px 20px 60px; }
  .form-header h1 { font-size: 28px; }
  .form-row { flex-direction: column; gap: 0; }
}
</style>
