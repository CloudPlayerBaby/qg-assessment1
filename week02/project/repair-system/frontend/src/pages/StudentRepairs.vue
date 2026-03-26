<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { RepairForm } from '../types'
import { getMine, updateStatus } from '../api/repairForms'

const forms = ref<RepairForm[]>([])
const loading = ref(false)
const error = ref<string | null>(null)

const statusText = (s: number) => {
  if (s === 0) return '处理中'
  if (s === 1) return '已取消'
  return `未知(${s})`
}

const refresh = async () => {
  loading.value = true
  error.value = null
  try {
    forms.value = await getMine()
  } catch (e: any) {
    error.value = e?.message ?? '加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refresh()
})

const onCancel = async (id: number) => {
  if (!confirm('确认取消该报修单吗？（取消=把状态从 0 改为 1）')) return
  loading.value = true
  error.value = null
  try {
    await updateStatus(id, 1)
    await refresh()
  } catch (e: any) {
    error.value = e?.message ?? '取消失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="container">
    <div class="nav">
      <a class="nav-link" href="#/student">返回首页</a>
      <a class="nav-link" href="#/admin" v-if="false">管理员端</a>
    </div>

    <h2>报修列表</h2>

    <div class="error" v-if="error">{{ error }}</div>
    <div v-if="loading">加载中...</div>

    <table class="table" v-if="!loading && forms.length">
      <thead>
        <tr>
          <th>单号</th>
          <th>类型</th>
          <th>问题</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="f in forms" :key="f.id">
          <td>{{ f.id }}</td>
          <td>{{ f.type }}</td>
          <td class="wrap">{{ f.problem }}</td>
          <td>{{ statusText(f.status) }}</td>
          <td>
            <button class="btn-small" v-if="f.status === 0" @click="onCancel(f.id)" :disabled="loading">
              取消报修
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="!loading && !forms.length" class="muted">暂无报修记录</div>
  </div>
</template>

