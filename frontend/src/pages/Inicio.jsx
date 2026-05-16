import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../services/api'
import FloatingLines from '../components/FloatingLines'
import './Inicio.css'

export default function Inicio() {
  const navigate = useNavigate()
  const [puestos, setPuestos] = useState([])
  const role = localStorage.getItem('role')
  const username = localStorage.getItem('username')

  useEffect(() => { api.get('/inicio').then(setPuestos).catch(() => {}) }, [])

  function logout() { localStorage.clear(); navigate('/') }

  function dashboardLink() {
    if (role === 'ROLE_ADMIN')   return '/admin'
    if (role === 'ROLE_EMPRESA') return '/empresa'
    return '/oferente'
  }

  return (
    <div className="dark-page">
      <div className="in-bg">
        <FloatingLines
          enabledWaves={['top', 'middle', 'bottom']}
          lineCount={8} lineDistance={8} bendRadius={8} bendStrength={-2}
          interactive parallax animationSpeed={1}
          linesGradient={['#e945f5', '#7c3aed', '#2563eb', '#f97316', '#22c55e']}
        />
      </div>
      <div className="in-overlay" />

      <div className="in-content">
        <nav className="navbar in-nav">
          <Link to="/" className="navbar-brand in-nav-brand">
            <img src="/logo.png" alt="Job Connect" className="in-nav-logo" />
            <span>Job Connect</span>
          </Link>
          <div className="navbar-links">
            <Link to="/buscar">Buscar puestos</Link>
            {username ? (
              <>
                <Link to={dashboardLink()}>Mi panel</Link>
                <button className="btn btn-glass btn-sm" onClick={logout}>Salir</button>
              </>
            ) : (
              <>
                <Link to="/login">Ingresar</Link>
                <Link to="/registro/empresa">Empresa</Link>
                <Link to="/registro/oferente">Oferente</Link>
              </>
            )}
          </div>
        </nav>

        <div className="in-hero">
          <h1 className="in-hero-title">Encuentra tu próxima oportunidad</h1>
          <p className="in-hero-sub">Conectamos empresas con talento. Puestos actualizados diariamente.</p>
          <div className="in-hero-actions">
            <Link to="/buscar" className="btn btn-glass-primary in-btn-primary">Ver todos los puestos</Link>
            {!username && (
              <Link to="/registro/empresa" className="btn btn-glass in-btn-register">Registrar empresa</Link>
            )}
          </div>
        </div>

        <div className="page in-section">
          <h2 className="in-section-title">Puestos destacados</h2>
          {puestos.length === 0 ? (
            <p>No hay puestos disponibles por el momento.</p>
          ) : (
            <div className="grid in-grid">
              {puestos.map(p => (
                <div key={p.id} className="card-glass">
                  <div className="card-title">{p.descripcion}</div>
                  <p><strong>Empresa:</strong> {p.idUsuario?.nombre}</p>
                  <div className="card-meta">
                    <span className="badge badge-green">₡{p.salario?.toLocaleString()}</span>
                    <span className="badge badge-blue">Público</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
