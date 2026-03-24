import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import PlayerBar from './PlayerBar'

export default function Layout() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Link to="/" className="brand">MusicBox Pro</Link>
        <nav className="nav-links">
          <NavLink to="/" end>Scopri</NavLink>
          <NavLink to="/library">Libreria</NavLink>
        </nav>
        <div className="user-card">
          <strong>{user?.name}</strong>
          <span>{user?.email}</span>
          <button onClick={handleLogout}>Esci</button>
        </div>
      </aside>
      <main className="content-area">
        <Outlet />
      </main>
      <PlayerBar />
    </div>
  )
}
