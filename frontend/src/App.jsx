import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import SearchPage from './pages/SearchPage'
import LibraryPage from './pages/LibraryPage'
import { useAuth } from './context/AuthContext'

function ProtectedRoute({ children }) {
  const { token } = useAuth()
  return token ? children : <Navigate to="/login" replace />
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }
      >
        <Route index element={<SearchPage />} />
        <Route path="library" element={<LibraryPage />} />
      </Route>
    </Routes>
  )
}
