<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { RepairForm } from '../types'
import { getById, updateStatus, deleteById } from '../api/repairForms'

const route = useRoute()
const router = useRouter()

const form = ref<RepairForm | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)
const statusValue = ref<number>(0)
const saving = ref(false)
const imgFailed = ref(false)

const statusText = (s: number) => {
  if (s === 0) return '处理中'
  if (s === 1) return '已取消'
  return `未知(${s})`
}

const refresh = async () => {
  loading.value = true
  error.value = null
  imgFailed.value = false
  try {
    const id = Number(route.params.id)
    form.value = await getById(id)
    statusValue.value = form.value.status
  } catch (e: any) {
    error.value = e?.message ?? '加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refresh()
})

const onSaveStatus = async () => {
  if (!form.value) return
  saving.value = true
  error.value = null
  try {
    await updateStatus(form.value.id, statusValue.value)
    await refresh()
    alert('状态已更新')
  } catch (e: any) {
    error.value = e?.message ?? '更新失败'
  } finally {
    saving.value = false
  }
}

const onDelete = async () => {
  if (!form.value) return
  if (!confirm('确认删除该报修单？')) return
  saving.value = true
  error.value = null
  try {
    await deleteById(form.value.id)
    router.push('/admin')
  } catch (e: any) {
    error.value = e?.message ?? '删除失败'
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div class="container">
    <div class="nav">
      <a class="nav-link" href="#/admin">返回列表</a>
    </div>

    <h2>报修单详情</h2>

    <div class="error" v-if="error">{{ error }}</div>
    <div v-if="loading">加载中...</div>

    <div v-if="!loading && form" class="card">
      <div class="row">
        <div class="muted">单号：{{ form.id }}</div>
      </div>
      <div class="row">
        <div class="muted">用户ID：{{ form.userId }}</div>
      </div>
      <div class="row">
        <div class="muted">类型：{{ form.type }}</div>
      </div>
      <div class="row">
        <div class="muted">问题：{{ form.problem }}</div>
      </div>
      <div class="row">
        <div class="muted">更新时间：{{ form.updateTime }}</div>
      </div>
      <div class="row">
        <div class="muted">当前状态：{{ statusText(form.status) }}</div>
      </div>

      <div class="row" v-if="form.imageUrl">
        <label class="label">图片</label>
        <div class="image-wrap">
          <img
            class="detail-image"
            :src="form.imageUrl"
            alt="报修图片"
            @error="imgFailed = true"
          />
          <div class="error" v-if="imgFailed">图片加载失败（可能是链接不可访问）</div>
<!--          <div class="muted" v-if="form.imageUrl">图片链接：{{ form.imageUrl }}</div>-->
          <a class="btn-small-link" :href="form.imageUrl" target="_blank" rel="noreferrer">查看</a>
        </div>
      </div>

      <div class="divider" />

      <div class="row">
        <label class="label">修改状态</label>
        <select class="input" v-model="statusValue" :disabled="saving">
          <option :value="0">0：处理中</option>
          <option :value="1">1：已取消</option>
        </select>
      </div>

      <div class="row actions">
        <button class="btn" :disabled="saving" @click="onSaveStatus">
          {{ saving ? '保存中...' : '保存状态' }}
        </button>
        <button class="btn-danger" :disabled="saving" @click="onDelete">
          {{ saving ? '处理中...' : '删除' }}
        </button>
      </div>
    </div>
  </div>
</template>

