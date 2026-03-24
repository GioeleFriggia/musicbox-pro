import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: 'admin@musicbox.local', password: 'Admin123!' })
  const [error, setError] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      await login(form)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Login fallito')
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Accedi a MusicBox Pro</h1>
        <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="Email" />
        <input type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="Password" />
        {error && <p className="error-text">{error}</p>}
        <button>Accedi</button>
        <p>Non hai un account? <Link to="/register">Registrati</Link></p>
      </form>
    </div>
  )
}
