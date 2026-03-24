import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      await register(form)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Registrazione fallita')
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Crea il tuo account</h1>
        <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder="Nome" />
        <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="Email" />
        <input type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="Password" />
        {error && <p className="error-text">{error}</p>}
        <button>Registrati</button>
        <p>Hai già un account? <Link to="/login">Accedi</Link></p>
      </form>
    </div>
  )
}
