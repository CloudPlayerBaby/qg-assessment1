import axios from 'axios'
import { getToken } from '../state/auth'

const http = axios.create({
  baseURL: '',
  timeout: 15000
})

function safeString(v: unknown): string {
  return v === null || v === undefined ? '' : String(v)
}

function normalizeMessage(message: unknown): string | null {
  if (message === null || message === undefined) return null
  const text = safeString(message).trim()
  if (!text) return null

  // 去掉后端封装的技术前缀
  let cleaned = text.replace(/^系统错误：/g, '').trim()

  // 形如：400 BAD_REQUEST "xxx" 或 400 BAD_REQUEST 'xxx'
  cleaned = cleaned.replace(/^\d+\s+[A-Z_]+\s*[:\s]*/i, '').trim()

  // 注册重复 sid：屏蔽 SQL 细节
  if (cleaned.includes('Duplicate entry') && cleaned.includes('user.sid')) {
    return '该工号已注册，请直接登录'
  }

  // 登录/密码相关
  if (cleaned.includes('用户名或密码错误') || cleaned.includes('密码错误')) {
    return cleaned
  }

  // 对于其他 Spring/Servlet 常见格式：400 BAD_REQUEST '学生工号应以0025开头'
  // 这里再提取引号内业务文案，避免截断 Duplicate entry 判断逻辑。
  if (
    (cleaned.startsWith('"') && cleaned.endsWith('"')) ||
    (cleaned.startsWith("'") && cleaned.endsWith("'"))
  ) {
    cleaned = cleaned.slice(1, -1).trim()
  }
  const quotedMatch = cleaned.match(/['"]([^'"]+)['"]/)
  if (quotedMatch?.[1]) cleaned = quotedMatch[1].trim()

  // 其他：尽量保留后端业务文案，但不要泄露太多技术细节
  if (cleaned.length > 120) return cleaned.slice(0, 120) + '...'
  return cleaned
}

function extractDataMessage(data: any): string | null {
  if (!data) return null
  if (typeof data === 'string') return normalizeMessage(data)
  if (typeof data === 'object') {
    // Spring 常见：{ message: 'xxx' } 或 error 页面结构
    if (typeof data.message === 'string') return normalizeMessage(data.message)
    if (typeof data.error === 'string') return normalizeMessage(data.error)
  }
  return null
}

function getFriendlyErrorMessage(error: any): string {
  const status = error?.response?.status
  // 没登录或 token 失效
  if (status === 401) return '登录已失效，请重新登录'
  // 鉴权失败
  if (status === 403) return '没有权限'

  const data = error?.response?.data
  const msgFromData = extractDataMessage(data)
  const msgFromAxios = normalizeMessage(error?.message)

  // 400/404/500：优先返回后端给的业务提示；拿不到再给通用文案
  if (msgFromData) return msgFromData
  if (msgFromAxios) return msgFromAxios

  if (status === 400) return '请求参数不合法，请检查输入'
  if (status === 404) return '资源不存在'
  if (status === 500) return '系统错误，请稍后重试'
  return '请求失败，请稍后重试'
}

http.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = token
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    // 后端统一返回 Result<T>
    const body = response.data
    if (body && typeof body.code === 'number') {
      if (body.code === 200) return body.data
      throw new Error(normalizeMessage(body.message) ?? '操作失败')
    }
    return body
  },
  (error) => {
    const status = error?.response?.status
    if (status === 401) {
      // 未登录/token失效时，清理本地状态
      localStorage.removeItem('token')
      localStorage.removeItem('identity')
    }
    throw new Error(getFriendlyErrorMessage(error))
  }
)

export default http

