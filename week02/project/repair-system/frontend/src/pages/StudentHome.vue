<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { bindDormitory, getMeBySid, updateDormitory, updatePassword } from '../api/users'
import { createForm } from '../api/repairForms'
import { uploadImage } from '../api/upload'
import { clearIdentity, clearToken } from '../state/auth'
import type { User } from '../types'

const router = useRouter()

const me = ref<User | null>(null)
const dormitoryInput = ref('')
const dormitoryLoading = ref(false)
const dormitoryError = ref<string | null>(null)

const typeInput = ref('')
const problemInput = ref('')
const imageUrl = ref<string | null>(null)
const uploadLoading = ref(false)
const createLoading = ref(false)
const formError = ref<string | null>(null)
const successMsg = ref<string | null>(null)

const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const passwordLoading = ref(false)
const passwordError = ref<string | null>(null)
const passwordSuccess = ref<string | null>(null)

const refreshMe = async () => {
  me.value = await getMeBySid()
  dormitoryInput.value = me.value?.dormitory ?? ''
}

onMounted(async () => {
  try {
    await refreshMe()
  } catch (e) {
    // 已在路由守卫拦截未登录情况；这里保留占位
  }
})

const onBindOrUpdateDormitory = async () => {
  dormitoryError.value = null
  successMsg.value = null
  if (!dormitoryInput.value) {
    dormitoryError.value = '宿舍不能为空'
    return
  }

  dormitoryLoading.value = true
  try {
    if (me.value?.dormitory) {
      await updateDormitory(dormitoryInput.value)
    } else {
      await bindDormitory(dormitoryInput.value)
    }
    await refreshMe()
    successMsg.value = '宿舍信息已更新'
  } catch (e: any) {
    dormitoryError.value = e?.message ?? '操作失败'
  } finally {
    dormitoryLoading.value = false
  }
}

const onFileChange = async (file: File | null) => {
  formError.value = null
  successMsg.value = null
  imageUrl.value = null
  if (!file) return

  uploadLoading.value = true
  try {
    const url = await uploadImage(file)
    imageUrl.value = url
  } catch (e: any) {
    formError.value = e?.message ?? '图片上传失败'
  } finally {
    uploadLoading.value = false
  }
}

const onCreateForm = async () => {
  formError.value = null
  successMsg.value = null
  if (!typeInput.value || !problemInput.value) {
    formError.value = '类型和问题不能为空'
    return
  }
  if (!imageUrl.value) {
    formError.value = '请先上传图片'
    return
  }

  createLoading.value = true
  try {
    await createForm(typeInput.value, problemInput.value, imageUrl.value)
    typeInput.value = ''
    problemInput.value = ''
    imageUrl.value = null
    successMsg.value = '报修单已创建'
    // 创建成功后跳到列表，便于查看记录
    router.push('/student/repairs')
  } catch (e: any) {
    formError.value = e?.message ?? '创建失败'
  } finally {
    createLoading.value = false
  }
}

const onLogout = () => {
  clearToken()
  clearIdentity()
  router.push('/login')
}

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
      <a class="nav-link" href="#/student/repairs">我的报修</a>
      <a class="nav-link" href="#/admin" v-if="false">管理员</a>
      <button class="btn-danger" @click="onLogout">返回首页（退出登录）</button>
    </div>

    <h2>学生端：首页</h2>

    <div class="card">
      <h3>绑定宿舍</h3>
      <div class="muted" v-if="me">当前用户：{{ me.sid }}（{{ me.identity }}）</div>
      <div class="row">
        <label class="label">宿舍</label>
        <input v-model="dormitoryInput" class="input" placeholder="例如 西一101，东一101" />
      </div>
      <div class="error" v-if="dormitoryError">{{ dormitoryError }}</div>
      <button class="btn" :disabled="dormitoryLoading" @click="onBindOrUpdateDormitory">
        {{ dormitoryLoading ? '处理中...' : me?.dormitory ? '更新宿舍' : '绑定宿舍' }}
      </button>
    </div>

    <div class="card">
      <h3>创建报修单</h3>
      <div class="row">
        <label class="label">类型</label>
        <input v-model="typeInput" class="input" placeholder="例如 电器/网线/水电" />
      </div>
      <div class="row">
        <label class="label">问题描述</label>
        <textarea v-model="problemInput" class="textarea" placeholder="请描述具体问题" />
      </div>

      <div class="row">
        <label class="label">图片</label>
        <input class="input" type="file" accept="image/*" @change="(e) => onFileChange((e.target as HTMLInputElement).files?.[0] ?? null)" />
      </div>
      <div class="muted" v-if="imageUrl">
        上传成功：<a :href="imageUrl" target="_blank" rel="noreferrer">查看</a>
      </div>
      <div class="error" v-if="formError">{{ formError }}</div>

      <button class="btn" :disabled="createLoading || uploadLoading" @click="onCreateForm">
        {{ createLoading ? '创建中...' : '创建报修单' }}
      </button>
    </div>

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

    <div class="success" v-if="successMsg">{{ successMsg }}</div>
  </div>
</template>

