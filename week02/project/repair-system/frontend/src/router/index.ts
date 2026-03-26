import { createRouter, createWebHashHistory } from 'vue-router'
import { clearIdentity, clearToken, getToken, type Identity } from '../state/auth'
import Login from '../pages/Login.vue'
import Register from '../pages/Register.vue'
import StudentHome from '../pages/StudentHome.vue'
import StudentRepairs from '../pages/StudentRepairs.vue'
import AdminList from '../pages/AdminList.vue'
import AdminDetail from '../pages/AdminDetail.vue'
import { getMeBySid } from '../api/users'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    {
      path: '/student',
      component: StudentHome,
      meta: { requiresAuth: true, role: 'STUDENT' as Identity }
    },
    {
      path: '/student/repairs',
      component: StudentRepairs,
      meta: { requiresAuth: true, role: 'STUDENT' as Identity }
    },
    {
      path: '/admin',
      component: AdminList,
      meta: { requiresAuth: true, role: 'ADMIN' as Identity }
    },
    {
      path: '/admin/:id',
      component: AdminDetail,
      meta: { requiresAuth: true, role: 'ADMIN' as Identity }
    }
  ]
})

async function resolveCurrentIdentity(): Promise<Identity | null> {
  try {
    const me = await getMeBySid()
    return me.identity as Identity
  } catch {
    // token 无效时，http 拦截器已清理；这里兜底清理
    clearToken()
    clearIdentity()
    return null
  }
}

router.beforeEach(async (to) => {
  const requiresAuth = Boolean((to.meta as any)?.requiresAuth)
  const role = (to.meta as any)?.role as Identity | undefined

  if (!requiresAuth) return true

  if (!getToken()) return '/login'

  // 没指定角色就只做登录校验
  if (!role) return true

  const currentRole = await resolveCurrentIdentity()
  if (!currentRole) return '/login'
  if (currentRole === role) return true

  // 身份不匹配就跳到自己的入口
  if (currentRole === 'ADMIN') return '/admin'
  return '/student'
})

export default router

