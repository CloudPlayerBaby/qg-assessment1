<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/users'
import type { Identity } from '../state/auth'

const router = useRouter()

const sid = ref('')
const password = ref('')
const identity = ref<Identity>('STUDENT')
const loading = ref(false)
const error = ref<string | null>(null)
const successMsg = ref<string | null>(null)

const onSubmit = async () => {
  error.value = null
  successMsg.value = null
  if (!sid.value || !password.value) {
    error.value = '工号和密码不能为空'
    return
  }

  loading.value = true
  try {
    const ok = await register(sid.value, password.value, identity.value)
    if (!ok) {
      error.value = '注册失败'
      return
    }
    successMsg.value = '注册成功，请登录'
    setTimeout(() => {
      router.push('/login')
    }, 700)
  } catch (e: any) {
    error.value = e?.message ?? '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="container">
    <h2>注册</h2>
    <form class="form" @submit.prevent="onSubmit">
      <label class="label">工号</label>
      <input v-model="sid" class="input" placeholder="学生: 3125xxxxxx / 管理员: 0025xxxxxx" />

      <label class="label">密码</label>
      <input v-model="password" class="input" type="password" placeholder="4-16位字母数字" />

      <label class="label">身份</label>
      <select v-model="identity" class="input">
        <option value="STUDENT">学生</option>
        <option value="ADMIN">管理员</option>
      </select>

      <div class="error" v-if="error">{{ error }}</div>
      <div class="success" v-if="successMsg">{{ successMsg }}</div>

      <button class="btn" type="submit" :disabled="loading">
        {{ loading ? '注册中...' : '注册' }}
      </button>

      <div class="link">
        已有账号？
        <a href="#/login">去登录</a>
      </div>
    </form>
  </div>
</template>

