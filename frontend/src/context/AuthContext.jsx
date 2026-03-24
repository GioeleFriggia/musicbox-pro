import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import client from '../api/client'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('musicbox_token'))
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('musicbox_user')
    return raw ? JSON.parse(raw) : null
  })

  useEffect(() => {
    if (token) localStorage.setItem('musicbox_token', token)
    else localStorage.removeItem('musicbox_token')
  }, [token])

  useEffect(() => {
    if (user) localStorage.setItem('musicbox_user', JSON.stringify(user))
    else localStorage.removeItem('musicbox_user')
  }, [user])

  const login = async (payload) => {
    const { data } = await client.post('/auth/login', payload)
    setToken(data.token)
    setUser(data)
  }

  const register = async (payload) => {
    const { data } = await client.post('/auth/register', payload)
    setToken(data.token)
    setUser(data)
  }

  const logout = () => {
    setToken(null)
    setUser(null)
  }

  const value = useMemo(() => ({ token, user, login, register, logout }), [token, user])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}
