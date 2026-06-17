<template>
  <AuroraBackground>
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <div class="logo">
            <svg viewBox="0 0 48 48" fill="none" width="32" height="32">
              <rect width="48" height="48" rx="12" fill="#111"/>
              <path d="M14 34V18l10-8 10 8v16H28v-8h-8v8H14z" fill="white" fill-opacity="0.9"/>
            </svg>
            <span class="logo-text">轻客管家</span>
          </div>
        </div>

        <div class="auth-tabs">
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'login' }"
            @click="activeTab = 'login'"
          >
            登录
          </button>
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'register' }"
            @click="activeTab = 'register'"
          >
            注册
          </button>
        </div>

        <div class="auth-content">
          <!-- 登录表单 -->
          <el-form v-if="activeTab === 'login'" ref="loginFormRef" :model="loginForm" :rules="loginRules" @keyup.enter="handleLogin">
            <div class="form-title">
              <div class="eyebrow">WELCOME BACK</div>
              <h2>欢迎回来</h2>
              <p>登录您的账户以继续管理客户资源</p>
            </div>
            
            <el-form-item prop="username">
              <label class="field-label">用户名 <span class="req">*</span></label>
              <el-input v-model="loginForm.username" placeholder="请输入用户名" size="large" :prefix-icon="User" />
            </el-form-item>
            
            <el-form-item prop="password">
              <label class="field-label">密码 <span class="req">*</span></label>
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" size="large" show-password :prefix-icon="Lock" />
            </el-form-item>
            
            <el-form-item>
              <button class="auth-btn" :class="{ 'is-loading': loginLoading }" @click.prevent="handleLogin" type="button">
                <span v-if="!loginLoading">登 录</span>
                <span v-else>登录中...</span>
              </button>
            </el-form-item>
          </el-form>

          <!-- 注册表单 -->
          <el-form v-if="activeTab === 'register'" ref="registerFormRef" :model="registerForm" :rules="registerRules" @keyup.enter="handleRegister">
            <div class="form-title">
              <div class="eyebrow">CREATE ACCOUNT</div>
              <h2>创建新账户</h2>
              <p>填写以下信息，加入轻客管家开始管理您的客户资源</p>
            </div>
            
            <div class="form-row">
              <el-form-item prop="username" class="form-col">
                <label class="field-label">用户名 <span class="req">*</span></label>
                <el-input v-model="registerForm.username" placeholder="请输入用户名" size="large" :prefix-icon="User" />
              </el-form-item>
              <el-form-item prop="name" class="form-col">
                <label class="field-label">姓名 <span class="req">*</span></label>
                <el-input v-model="registerForm.name" placeholder="请输入真实姓名" size="large" :prefix-icon="UserFilled" />
              </el-form-item>
            </div>
            
            <div class="form-row">
              <el-form-item prop="phone" class="form-col">
                <label class="field-label">手机号 <span class="req">*</span></label>
                <el-input v-model="registerForm.phone" placeholder="请输入手机号" size="large" :prefix-icon="Phone" />
              </el-form-item>
              <el-form-item prop="email" class="form-col">
                <label class="field-label">邮箱 <span class="req">*</span></label>
                <el-input v-model="registerForm.email" placeholder="请输入邮箱地址" size="large" :prefix-icon="Message" />
              </el-form-item>
            </div>
            
            <div class="form-row">
              <el-form-item prop="password" class="form-col">
                <label class="field-label">密码 <span class="req">*</span></label>
                <el-input v-model="registerForm.password" type="password" placeholder="至少6位" size="large" show-password :prefix-icon="Lock" />
              </el-form-item>
              <el-form-item prop="confirmPassword" class="form-col">
                <label class="field-label">确认密码 <span class="req">*</span></label>
                <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" size="large" show-password :prefix-icon="Lock" />
              </el-form-item>
            </div>
            
            <el-form-item>
              <button class="auth-btn" :class="{ 'is-loading': registerLoading }" @click.prevent="handleRegister" type="button">
                <span v-if="!registerLoading">创建账户</span>
                <span v-else>注册中...</span>
              </button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </AuroraBackground>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, UserFilled, Phone, Message, Lock } from '@element-plus/icons-vue'
import { login, register } from '../../api/login'
import AuroraBackground from '../../components/AuroraBackground.vue'

const router = useRouter()
const activeTab = ref('login')

// 登录表单
const loginFormRef = ref(null)
const loginLoading = ref(false)
const loginForm = reactive({ username: '', password: '' })
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 注册表单
const registerFormRef = ref(null)
const registerLoading = ref(false)
const registerForm = reactive({
  username: '', name: '', phone: '', email: '', password: '', confirmPassword: ''
})

const validateConfirm = (rule, value, callback) => {
  if (value !== registerForm.password) callback(new Error('两次输入密码不一致'))
  else callback()
}

const registerRules = {
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

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    loginLoading.value = true
    try {
      const res = await login(loginForm)
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data))
      ElMessage.success('登录成功')
      router.push('/')
    } catch (e) { /* */ } finally { loginLoading.value = false }
  })
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  await registerFormRef.value.validate(async (valid) => {
    if (!valid) return
    registerLoading.value = true
    try {
      await register({ 
        username: registerForm.username, 
        name: registerForm.name, 
        phone: registerForm.phone, 
        email: registerForm.email, 
        password: registerForm.password 
      })
      ElMessage.success('注册成功，请登录')
      activeTab.value = 'login'
      registerForm.username = ''
      registerForm.name = ''
      registerForm.phone = ''
      registerForm.email = ''
      registerForm.password = ''
      registerForm.confirmPassword = ''
    } catch (e) { /* */ } finally { registerLoading.value = false }
  })
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Instrument+Serif&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap');

.auth-container {
  position: relative;
  z-index: 10;
  padding: 40px 20px;
  width: 100%;
  max-width: 600px;
}

.auth-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 48px 40px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.dark .auth-card {
  background: rgba(15, 23, 42, 0.85);
  border-color: rgba(255, 255, 255, 0.1);
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo {
  display: inline-flex;
  align-items: center;
  gap: 12px;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #111;
  letter-spacing: -0.02em;
}

.dark .logo-text {
  color: #fff;
}

.auth-tabs {
  display: flex;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 10px;
  padding: 4px;
  margin-bottom: 32px;
}

.dark .auth-tabs {
  background: rgba(255, 255, 255, 0.1);
}

.tab-btn {
  flex: 1;
  height: 44px;
  border: none;
  border-radius: 8px;
  background: transparent;
  color: #787774;
  font-size: 14px;
  font-weight: 600;
  font-family: 'Plus Jakarta Sans', sans-serif;
  letter-spacing: 0.02em;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-btn.active {
  background: #111;
  color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.dark .tab-btn.active {
  background: #fff;
  color: #111;
}

.tab-btn:hover:not(.active) {
  color: #2F3437;
}

.dark .tab-btn:hover:not(.active) {
  color: #fff;
}

.form-title {
  text-align: center;
  margin-bottom: 32px;
}

.eyebrow {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 4px;
  margin-bottom: 16px;
  background: rgba(0, 0, 0, 0.05);
  color: #787774;
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.15em;
  text-transform: uppercase;
}

.dark .eyebrow {
  background: rgba(255, 255, 255, 0.1);
}

.form-title h2 {
  font-family: 'Instrument Serif', 'Georgia', serif;
  font-size: 32px;
  font-weight: 400;
  color: #111;
  margin: 0 0 10px;
  letter-spacing: -0.03em;
}

.dark .form-title h2 {
  color: #fff;
}

.form-title p {
  font-size: 14px;
  color: #787774;
  line-height: 1.6;
  margin: 0;
}

.dark .form-title p {
  color: #94a3b8;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-col {
  flex: 1;
}

.field-label {
  display: block;
  font-size: 12px;
  font-weight: 600;
  color: #2F3437;
  margin-bottom: 6px;
  letter-spacing: 0.02em;
}

.dark .field-label {
  color: #e2e8f0;
}

.req {
  color: #D14D44;
}

.auth-card :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 10px;
  box-shadow: none;
  padding: 6px 16px;
  height: 48px;
  transition: all 0.3s;
}

.dark .auth-card :deep(.el-input__wrapper) {
  background: rgba(30, 41, 59, 0.6);
  border-color: rgba(255, 255, 255, 0.1);
}

.auth-card :deep(.el-input__wrapper:hover) {
  border-color: rgba(0, 0, 0, 0.2);
}

.dark .auth-card :deep(.el-input__wrapper:hover) {
  border-color: rgba(255, 255, 255, 0.2);
}

.auth-card :deep(.el-input__wrapper.is-focus) {
  border-color: #111;
  box-shadow: 0 0 0 3px rgba(17, 17, 17, 0.08);
}

.dark .auth-card :deep(.el-input__wrapper.is-focus) {
  border-color: #fff;
  box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.1);
}

.auth-card :deep(.el-input__inner) {
  color: #2F3437;
  font-size: 14px;
}

.dark .auth-card :deep(.el-input__inner) {
  color: #e2e8f0;
}

.auth-card :deep(.el-input__inner::placeholder) {
  color: #B0B0AC;
}

.dark .auth-card :deep(.el-input__inner::placeholder) {
  color: #64748b;
}

.auth-card :deep(.el-input__prefix .el-icon) {
  color: #999;
  font-size: 16px;
}

.dark .auth-card :deep(.el-input__prefix .el-icon) {
  color: #94a3b8;
}

.auth-card :deep(.el-form-item) {
  margin-bottom: 20px;
}

.auth-btn {
  width: 100%;
  height: 50px;
  margin-top: 8px;
  border: none;
  border-radius: 10px;
  background: #111;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  font-family: 'Plus Jakarta Sans', sans-serif;
  letter-spacing: 0.05em;
  cursor: pointer;
  transition: all 0.3s;
}

.dark .auth-btn {
  background: #fff;
  color: #111;
}

.auth-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.auth-btn:active {
  transform: translateY(0);
}

.auth-btn.is-loading {
  pointer-events: none;
  opacity: 0.7;
}

@media (max-width: 640px) {
  .auth-card {
    padding: 32px 24px;
  }
  
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  
  .form-title h2 {
    font-size: 26px;
  }
}
</style>