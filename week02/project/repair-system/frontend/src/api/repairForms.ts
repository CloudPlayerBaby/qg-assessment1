import http from './http'
import type { RepairForm } from '../types'

export function createForm(
  type: string,
  problem: string,
  imageUrl: string
): Promise<void> {
  return http.post('/repairForms/create', null, {
    params: { type, problem, imageUrl }
  })
}

export function getMine(): Promise<RepairForm[]> {
  return http.get('/repairForms/mine')
}

export function getAll(): Promise<RepairForm[]> {
  return http.get('/repairForms/all')
}

export function getByStatus(status: number): Promise<RepairForm[]> {
  return http.get('/repairForms/status', { params: { status } })
}

export function getById(id: number): Promise<RepairForm> {
  return http.get(`/repairForms/${id}`)
}

export function updateStatus(id: number, status: number): Promise<boolean> {
  return http.patch(`/repairForms/${id}/status`, { status })
}

export function deleteById(id: number): Promise<boolean> {
  return http.delete(`/repairForms/${id}`)
}

