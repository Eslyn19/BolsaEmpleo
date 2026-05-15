import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Inicio from './pages/Inicio'
import Buscar from './pages/Buscar'
import RegistroEmpresa from './pages/RegistroEmpresa'
import RegistroOferente from './pages/RegistroOferente'
import AdminDashboard from './pages/admin/AdminDashboard'
import EmpresaDashboard from './pages/empresa/EmpresaDashboard'
import OferenteDashboard from './pages/oferente/OferenteDashboard'

function PrivateRoute({ children, role }) {
  const token = localStorage.getItem('token')
  const userRole = localStorage.getItem('role')
  if (!token) return <Navigate to="/login" replace />
  if (role && userRole !== role) return <Navigate to="/login" replace />
  return children
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Inicio />} />
        <Route path="/login" element={<Login />} />
        <Route path="/buscar" element={<Buscar />} />
        <Route path="/registro/empresa" element={<RegistroEmpresa />} />
        <Route path="/registro/oferente" element={<RegistroOferente />} />
        <Route path="/admin/*" element={
          <PrivateRoute role="ROLE_ADMIN"><AdminDashboard /></PrivateRoute>
        } />
        <Route path="/empresa/*" element={
          <PrivateRoute role="ROLE_EMPRESA"><EmpresaDashboard /></PrivateRoute>
        } />
        <Route path="/oferente/*" element={
          <PrivateRoute role="ROLE_OFERENTE"><OferenteDashboard /></PrivateRoute>
        } />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
