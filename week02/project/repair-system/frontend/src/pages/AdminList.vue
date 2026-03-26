<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { RepairForm } from '../types'
import { getAll, getByStatus } from '../api/repairForms'
import { useRouter } from 'vue-router'
import { clearIdentity, clearToken } from '../state/auth'
import { updatePassword } from '../api/users'

const forms = ref<RepairForm[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
const filter = ref<'all' | 0 | 1>('all')
const router = useRouter()

const statusText = (s: number) => {
  if (s === 0) return '处理中'
  if (s === 1) return '已取消'
  return `未知(${s})`
}

const refresh = async () => {
  loading.value = true
  error.value = null
  try {
    if (filter.value === 'all') {
      forms.value = await getAll()
    } else {
      forms.value = await getByStatus(filter.value)
    }
  } catch (e: any) {
    error.value = e?.message ?? '加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refresh()
})

const onFilterChange = () => {
  refresh()
}

const onLogout = () => {
  clearToken()
  clearIdentity()
  router.push('/login')
}

const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const passwordLoading = ref(false)
const passwordError = ref<string | null>(null)
const passwordSuccess = ref<string | null>(null)

const onUpdatePassword = async () => {
  passwordError.value = null
  passwordSuccess.value = null

  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    passwordError.value = '请完整填写旧密码/新密码/确认密码'
    return
  }
  if (newPassword.value !== confirmPassword.value) {
    passwordError.value = '两次新密码不一致'
    return
  }

  passwordLoading.value = true
  try {
    await updatePassword(oldPassword.value, newPassword.value, confirmPassword.value)
    oldPassword.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
    passwordSuccess.value = '密码已更新'
  } catch (e: any) {
    passwordError.value = e?.message ?? '更新失败'
  } finally {
    passwordLoading.value = false
  }
}
</script>

<template>
  <div class="container">
    <div class="nav">
<!--      <a class="nav-link" href="#/student">学生端</a>-->
      <button class="btn-danger" @click="onLogout">返回首页（退出登录）</button>
    </div>

    <h2>报修单列表（管理员）</h2>

    <div class="row">
      <label class="label">状态筛选</label>
      <select class="input" v-model="filter" @change="onFilterChange">
        <option value="all">全部</option>
        <option value="0">0：处理中</option>
        <option value="1">1：已取消</option>
      </select>
    </div>

    <div class="error" v-if="error">{{ error }}</div>
    <div v-if="loading">加载中...</div>

    <table class="table" v-if="!loading && forms.length">
      <thead>
        <tr>
          <th>单号</th>
          <th>类型</th>
          <th>问题</th>
          <th>状态</th>
          <th>更新时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="f in forms" :key="f.id">
          <td>{{ f.id }}</td>
          <td>{{ f.type }}</td>
          <td class="wrap">{{ f.problem }}</td>
          <td>{{ statusText(f.status) }}</td>
          <td>{{ f.updateTime }}</td>
          <td>
            <a class="btn-small-link" :href="`#/admin/${f.id}`">查看详情</a>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="!loading && !forms.length" class="muted">暂无数据</div>

    <div class="card">
      <h3>更新密码</h3>
      <div class="row">
        <label class="label">旧密码</label>
        <input v-model="oldPassword" class="input" type="password" placeholder="请输入旧密码" />
      </div>
      <div class="row">
        <label class="label">新密码</label>
        <input v-model="newPassword" class="input" type="password" placeholder="请输入新密码" />
      </div>
      <div class="row">
        <label class="label">确认密码</label>
        <input v-model="confirmPassword" class="input" type="password" placeholder="请再次输入新密码" />
      </div>

      <div class="error" v-if="passwordError">{{ passwordError }}</div>
      <div class="success" v-if="passwordSuccess">{{ passwordSuccess }}</div>

      <button class="btn" :disabled="passwordLoading" @click="onUpdatePassword">
        {{ passwordLoading ? '更新中...' : '更新密码' }}
      </button>
    </div>
  </div>
</template>

