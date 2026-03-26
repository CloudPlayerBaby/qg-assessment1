<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login, getMeBySid } from '../api/users'
import { setToken, setIdentity } from '../state/auth'
import type { Identity } from '../state/auth'

const router = useRouter()

const sid = ref('')
const password = ref('')
const loading = ref(false)
const error = ref<string | null>(null)

const onSubmit = async () => {
  error.value = null
  if (!sid.value || !password.value) {
    error.value = '工号和密码不能为空'
    return
  }

  loading.value = true
  try {
    const token = await login(sid.value, password.value)
    setToken(token)
    const me = await getMeBySid()
    setIdentity(me.identity as Identity)
    if (me.identity === 'ADMIN') {
      router.push('/admin')
    } else {
      router.push('/student')
    }
  } catch (e: any) {
    error.value = e?.message ?? '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="container">
    <h2>登录</h2>
    <form class="form" @submit.prevent="onSubmit">
      <label class="label">工号</label>
      <input v-model="sid" class="input" placeholder="例如 3125xxxxxx / 0025xxxxxx" />

      <label class="label">密码</label>
      <input v-model="password" class="input" type="password" placeholder="4-16位字母数字" />

      <div class="error" v-if="error">{{ error }}</div>
      <button class="btn" type="submit" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>

      <div class="link">
        没有账号？
        <a href="#/register">去注册</a>
      </div>
    </form>
  </div>
</template>

