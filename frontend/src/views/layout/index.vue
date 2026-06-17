<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <div class="sidebar" :class="{ collapsed: isCollapsed }">
      <div class="logo" @click="router.push('/')">
        <svg viewBox="0 0 48 48" fill="none" width="26" height="26">
          <rect width="48" height="48" rx="12" fill="#111"/>
          <path d="M14 34V18l10-8 10 8v16H28v-8h-8v8H14z" fill="white" fill-opacity="0.9"/>
        </svg>
        <span v-if="!isCollapsed" class="logo-text">轻客管家</span>
      </div>

      <el-menu
        :default-active="route.path"
        router
        :collapse="isCollapsed"
        background-color="transparent"
        text-color="#787774"
        active-text-color="#111"
        class="sidebar-menu"
      >
        <el-menu-item index="/dept">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title>部门管理</template>
        </el-menu-item>
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          <template #title>用户管理</template>
        </el-menu-item>
        <el-menu-item index="/role">
          <el-icon><Key /></el-icon>
          <template #title>角色管理</template>
        </el-menu-item>
        <el-menu-item index="/clue">
          <el-icon><Phone /></el-icon>
          <template #title>线索管理</template>
        </el-menu-item>
        <el-menu-item index="/course">
          <el-icon><Reading /></el-icon>
          <template #title>课程管理</template>
        </el-menu-item>
        <el-menu-item index="/activity">
          <el-icon><Present /></el-icon>
          <template #title>活动管理</template>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-wrapper">
      <!-- 顶部导航 -->
      <div class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapsed = !isCollapsed">
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
          <span class="page-title">{{ route.meta.title || '首页' }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="30" :src="userInfo?.image" class="user-avatar">
                {{ userInfo?.name?.charAt(0) }}
              </el-avatar>
              <span class="user-name">{{ userInfo?.name || userInfo?.username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 页面内容 -->
      <div class="main-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { OfficeBuilding, User, Key, Phone, Fold, Expand, SwitchButton, Reading, Present } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const isCollapsed = ref(false)

const userInfo = computed(() => {
  try { return JSON.parse(localStorage.getItem('userInfo') || '{}') } catch { return {} }
})

const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap');

.layout-container {
  display: flex; min-height: 100vh;
  background: #FBFBFA;
  font-family: 'Plus Jakarta Sans', -apple-system, sans-serif;
  color: #2F3437;
}

.sidebar {
  width: 220px;
  background: #FFFFFF;
  border-right: 1px solid #EAEAEA;
  transition: width 0.3s;
  overflow: hidden; flex-shrink: 0;
}
.sidebar.collapsed { width: 64px; }

.logo {
  height: 56px; display: flex; align-items: center; justify-content: center; gap: 10px;
  cursor: pointer; border-bottom: 1px solid #EAEAEA;
}
.logo-text { font-size: 15px; font-weight: 700; color: #111; letter-spacing: -0.01em; white-space: nowrap; }

.sidebar-menu { border-right: none; padding: 12px 8px; }
.sidebar-menu :deep(.el-menu-item) {
  border-radius: 8px; margin-bottom: 2px; height: 42px;
  font-size: 13px; font-weight: 500;
  transition: all 0.2s;
}
.sidebar-menu :deep(.el-menu-item.is-active) {
  background: #F2F2F0 !important; color: #111 !important; font-weight: 600;
}
.sidebar-menu :deep(.el-menu-item:hover) { background: #F7F7F5 !important; }

.main-wrapper { flex: 1; display: flex; flex-direction: column; overflow: hidden; }

.header {
  height: 56px; background: #FFFFFF;
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid #EAEAEA; flex-shrink: 0;
}
.header-left { display: flex; align-items: center; gap: 14px; }
.collapse-btn {
  font-size: 18px; cursor: pointer; color: #999;
  transition: color 0.2s;
}
.collapse-btn:hover { color: #2F3437; }
.page-title { font-size: 14px; font-weight: 600; color: #2F3437; }

.user-info {
  display: flex; align-items: center; gap: 8px;
  cursor: pointer; padding: 4px 8px; border-radius: 6px;
  transition: background 0.2s;
}
.user-info:hover { background: #F7F7F5; }
.user-avatar { background: #111; color: #fff; font-size: 12px; }
.user-name { font-size: 13px; color: #2F3437; font-weight: 500; }

.main-content { flex: 1; padding: 20px; overflow-y: auto; }
</style>
