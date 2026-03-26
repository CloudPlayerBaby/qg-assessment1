export type Identity = 'STUDENT' | 'ADMIN'

export interface User {
  id: number
  sid: string
  identity: Identity
  dormitory: string | null
}

export interface RepairForm {
  id: number
  userId: number
  type: string
  problem: string
  status: number // 0 / 1
  updateTime: string
  imageUrl: string | null
}

export interface Result<T> {
  code: number
  message: string
  data: T
}

