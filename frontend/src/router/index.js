import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue')
  },
  {
    path: '/',
    component: () => import('../views/layout/index.vue'),
    redirect: '/dept',
    children: [
      {
        path: 'dept',
        name: 'Dept',
        component: () => import('../views/system/dept/index.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/system/user/index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('../views/system/role/index.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'clue',
        name: 'Clue',
        component: () => import('../views/clue/index.vue'),
        meta: { title: '线索管理' }
      },
      {
        path: 'course',
        name: 'Course',
        component: () => import('../views/resource/course/index.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: 'activity',
        name: 'Activity',
        component: () => import('../views/resource/activity/index.vue'),
        meta: { title: '活动管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const publicPages = ['/login']
  if (!publicPages.includes(to.path) && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
