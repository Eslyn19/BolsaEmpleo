import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../services/api'
import FloatingLines from '../components/FloatingLines'

export default function Inicio() {
  const navigate = useNavigate()
  const [puestos, setPuestos] = useState([])
  const role = localStorage.getItem('role')
  const username = localStorage.getItem('username')

  useEffect(() => { api.get('/inicio').then(setPuestos).catch(() => {}) }, [])

  function logout() { localStorage.clear(); navigate('/') }

  function dashboardLink() {
    if (role === 'ROLE_ADMIN') return '/admin'
    if (role === 'ROLE_EMPRESA') return '/empresa'
    return '/oferente'
  }

  return (
    <div className="dark-page">
      {/* FloatingLines fijo cubriendo toda la página */}
      <div style={{ position: 'fixed', inset: 0, zIndex: 0 }}>
        <FloatingLines
          enabledWaves={['top', 'middle', 'bottom']}
          lineCount={8}
          lineDistance={8}
          bendRadius={8}
          bendStrength={-2}
          interactive
          parallax={true}
          animationSpeed={1}
          linesGradient={['#e945f5', '#7c3aed', '#2563eb', '#f97316', '#22c55e']}
        />
      </div>

      {/* Overlay suave para que el contenido sea más legible */}
      <div style={{ position: 'fixed', inset: 0, zIndex: 1, background: 'rgba(8,11,26,.45)', pointerEvents: 'none' }} />

      {/* Todo el contenido encima */}
      <div style={{ position: 'relative', zIndex: 2 }}>

        {/* Navbar */}
        <nav className="navbar" style={{ background: 'rgba(17,29,74,.75)', backdropFilter: 'blur(16px)', WebkitBackdropFilter: 'blur(16px)' }}>
          <Link to="/" className="navbar-brand" style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <img src="/logo.png" alt="Job Connect" style={{ height: 36, width: 'auto' }} />
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

        {/* Hero */}
        <div style={{
          display: 'flex', flexDirection: 'column',
          alignItems: 'center', justifyContent: 'center',
          textAlign: 'center', padding: '100px 24px 80px',
        }}>
          <h1 style={{
            fontSize: '3rem', fontWeight: 800, letterSpacing: '-1.5px',
            marginBottom: 16, color: 'white',
            textShadow: '0 2px 24px rgba(0,0,0,.6)',
          }}>
            Encuentra tu próxima oportunidad
          </h1>
          <p style={{ fontSize: '1.15rem', color: 'rgba(255,255,255,.7)', marginBottom: 36, maxWidth: 520 }}>
            Conectamos empresas con talento. Puestos actualizados diariamente.
          </p>
          <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', justifyContent: 'center' }}>
            <Link to="/buscar" className="btn btn-glass-primary"
              style={{ padding: '13px 36px', fontSize: '1rem' }}>
              Ver todos los puestos
            </Link>
            {!username && (
              <Link to="/registro/empresa" className="btn btn-glass"
                style={{ padding: '13px 28px', fontSize: '1rem' }}>
                Registrar empresa
              </Link>
            )}
          </div>
        </div>

        {/* Puestos destacados */}
        <div className="page" style={{ paddingTop: 0 }}>
          <h2 style={{ marginBottom: 20 }}>Puestos destacados</h2>
          {puestos.length === 0 ? (
            <p>No hay puestos disponibles por el momento.</p>
          ) : (
            <div className="grid" style={{ paddingBottom: 60 }}>
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
