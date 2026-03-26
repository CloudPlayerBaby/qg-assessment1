import http from './http'

export function uploadImage(file: File): Promise<string> {
  const form = new FormData()
  form.append('file', file)
  // 后端接收 MultipartFile
  return http.post('/upload', form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

