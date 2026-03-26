import http from './http'
import type { Identity, User } from '../types'

export function login(sid: string, password: string): Promise<string> {
  return http.post('/users/login', { sid, password })
}

export function register(
  sid: string,
  password: string,
  identity: Identity
): Promise<boolean> {
  return http.post('/users/register', { sid, password, identity })
}

export function getMeBySid(): Promise<User> {
  return http.get('/users/sid')
}

export function bindDormitory(dormitory: string): Promise<void> {
  return http.post('/users/dormitory', { dormitory })
}

export function updateDormitory(dormitory: string): Promise<void> {
  return http.patch('/users/dormitory', { dormitory })
}

export function updatePassword(
  oldPassword: string,
  newPassword: string,
  confirmPassword: string
): Promise<void> {
  return http.patch('/users/password', {
    oldPassword,
    newPassword,
    confirmPassword
  })
}

