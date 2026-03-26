export type Identity = 'STUDENT' | 'ADMIN'

const TOKEN_KEY = 'token'
const IDENTITY_KEY = 'identity'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

export function getIdentity(): Identity | null {
  const v = localStorage.getItem(IDENTITY_KEY)
  if (v === 'STUDENT' || v === 'ADMIN') return v
  return null
}

export function setIdentity(identity: Identity): void {
  localStorage.setItem(IDENTITY_KEY, identity)
}

export function clearIdentity(): void {
  localStorage.removeItem(IDENTITY_KEY)
}

