import { useEffect, useState } from 'react'
import { Routes, Route, Link, useNavigate } from 'react-router-dom'
import { api } from '../../services/api'
import Dither from '../../components/Dither'
import './AdminDashboard.css'

function Dashboard() {
  const [stats, setStats] = useState(null)
  useEffect(() => { api.get('/admin/dashboard').then(setStats).catch(() => {}) }, [])

  const navCards = [
    { to: '/admin/empresas', emoji: '🏢', label: 'Aprobar empresas', sub: 'Revisa solicitudes pendientes' },
    { to: '/admin/oferentes', emoji: '👤', label: 'Aprobar oferentes', sub: 'Gestiona nuevos registros' },
    { to: '/admin/caracteristicas', emoji: '🏷️', label: 'Características', sub: 'Administra el árbol de skills' },
  ]

  return (
    <div className="ad-dashboard">
      <div className="ad-eyebrow">Panel de Administración</div>
      <h1 className="ad-hero-title">Resumen</h1>

      {stats && (
        <div className="ad-stats">
          {[
            { n: stats.empresasPendientes,  label: 'Empresas pendientes'  },
            { n: stats.oferentesPendientes, label: 'Oferentes pendientes' },
          ].map(s => (
            <div key={s.label} className="ad-stat-card">
              <div className="ad-stat-num">{s.n}</div>
              <div className="ad-stat-label">{s.label}</div>
            </div>
          ))}
        </div>
      )}

      <div className="ad-nav-cards">
        {navCards.map(c => (
          <Link key={c.to} to={c.to} className="ad-nav-card">
            <div className="ad-nav-card-emoji">{c.emoji}</div>
            <div className="ad-nav-card-label">{c.label}</div>
            <div className="ad-nav-card-sub">{c.sub}</div>
          </Link>
        ))}
      </div>
    </div>
  )
}

function Empresas() {
  const [lista, setLista] = useState([])
  const [tipo, setTipo] = useState({})
  useEffect(() => { api.get('/admin/empresas').then(setLista).catch(() => {}) }, [])

  async function aprobar(id) {
    await api.post(`/admin/empresas/${id}/aprobar`, { tipo: tipo[id] || 'Privada' })
    setLista(l => l.filter(e => e.idUsuario !== id))
  }
  async function rechazar(id) {
    await api.post(`/admin/empresas/${id}/rechazar`)
    setLista(l => l.filter(e => e.idUsuario !== id))
  }

  return (
    <div className="ad-approval-page">
      <h2 className="ad-page-title">Empresas pendientes</h2>
      {lista.length === 0 ? (
        <div className="ad-card ad-empty-state"><p>No hay empresas pendientes.</p></div>
      ) : (
        <div className="grid">
          {lista.map(e => (
            <div key={e.idUsuario} className="ad-card" style={{ padding: 20 }}>
              <div className="ad-empresa-name">{e.nombre}</div>
              <p className="ad-empresa-meta">{e.correo} · {e.ubicacion}</p>
              <p className="ad-empresa-desc">{e.descripcion}</p>
              <div className="ad-empresa-actions">
                <select value={tipo[e.idUsuario] || ''}
                  onChange={ev => setTipo({ ...tipo, [e.idUsuario]: ev.target.value })}
                  className="ad-select">
                  <option value="">Tipo de empresa…</option>
                  <option value="Pública">Pública</option>
                  <option value="Privada">Privada</option>
                </select>
                <button className="ad-btn-solid" onClick={() => aprobar(e.idUsuario)}>Aprobar</button>
                <button className="ad-btn-danger" onClick={() => rechazar(e.idUsuario)}>Rechazar</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

function Oferentes() {
  const [lista, setLista] = useState([])
  useEffect(() => { api.get('/admin/oferentes').then(setLista).catch(() => {}) }, [])

  async function aprobar(id) {
    await api.post(`/admin/oferentes/${id}/aprobar`)
    setLista(l => l.filter(o => o.idUsuario !== id))
  }
  async function rechazar(id) {
    await api.post(`/admin/oferentes/${id}/rechazar`)
    setLista(l => l.filter(o => o.idUsuario !== id))
  }

  return (
    <div className="ad-approval-page">
      <h2 className="ad-page-title">Oferentes pendientes</h2>
      {lista.length === 0 ? (
        <div className="ad-card ad-empty-state"><p>No hay oferentes pendientes.</p></div>
      ) : (
        <div className="grid">
          {lista.map(o => (
            <div key={o.idUsuario} className="ad-card" style={{ padding: 20 }}>
              <div className="ad-oferente-name">{o.nombre} {o.apellido}</div>
              <p className="ad-oferente-meta">{o.correo} · {o.nacionalidad}</p>
              <div className="ad-oferente-actions">
                <button className="ad-btn-solid" onClick={() => aprobar(o.idUsuario)}>Aprobar</button>
                <button className="ad-btn-danger" onClick={() => rechazar(o.idUsuario)}>Rechazar</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

function Caracteristicas() {
  const [data, setData]               = useState(null)
  const [parentId, setParentId]       = useState(null)
  const [nombre, setNombre]           = useState('')
  const [padreSelect, setPadreSelect] = useState('')

  function cargar(pid) {
    const query = pid != null ? `?parentId=${pid}` : ''
    api.get(`/admin/caracteristicas${query}`).then(d => { setData(d); setParentId(pid) }).catch(() => {})
  }
  useEffect(() => { cargar(null) }, [])

  async function crear(e) {
    e.preventDefault()
    await api.post('/admin/caracteristicas', { nombre, idPadre: parseInt(padreSelect) })
    setNombre('')
    cargar(parentId)
  }
  async function toggle(id) {
    await api.post(`/admin/caracteristicas/${id}/toggle-activo`)
    cargar(parentId)
  }

  if (!data) return <div style={{ textAlign: 'center', paddingTop: 80, color: 'rgba(255,255,255,.4)' }}>Cargando...</div>

  return (
    <div className="ad-approval-page">
      <h2 className="ad-page-title">Características</h2>

      {parentId != null && (
        <button className="ad-btn-outline" style={{ marginBottom: 20 }} onClick={() => cargar(null)}>
          ← Volver a raíces
        </button>
      )}

      <div className="grid" style={{ marginBottom: 24 }}>
        {data.items.map(c => (
          <div key={c.id} className="ad-card ad-char-item">
            <div className="ad-char-info">
              <span className="ad-char-name">{c.nombre}</span>
              <span className={`ad-badge ${c.activo ? 'ad-badge--active' : 'ad-badge--inactive'}`}>
                {c.activo ? 'Activa' : 'Inactiva'}
              </span>
            </div>
            <div className="ad-char-actions">
              <button className="ad-btn-outline" style={{ padding: '5px 12px', fontSize: '0.8rem' }} onClick={() => cargar(c.id)}>Ver hijos</button>
              <button className="ad-btn-outline" style={{ padding: '5px 12px', fontSize: '0.8rem' }} onClick={() => toggle(c.id)}>Toggle</button>
            </div>
          </div>
        ))}
      </div>

      <div className="ad-card">
        <div className="ad-section-label">Agregar característica</div>
        <form onSubmit={crear} className="ad-char-form">
          <input placeholder="Nombre" value={nombre} onChange={e => setNombre(e.target.value)} required
            className="ad-input" style={{ flex: 1, minWidth: 160 }} />
          <select value={padreSelect} onChange={e => setPadreSelect(e.target.value)} required className="ad-select">
            <option value="">Padre…</option>
            {data.padresSelect.map(p => <option key={p.id} value={p.id}>{p.nombre}</option>)}
          </select>
          <button type="submit" className="ad-btn-solid">Agregar</button>
        </form>
      </div>
    </div>
  )
}

// Agrupar todos los componentes
export default function AdminDashboard() {
  const navigate = useNavigate()
  function logout() { localStorage.clear(); navigate('/') }

  return (
    <div className="ad-shell">
      <div className="ad-bg">
        <Dither
          waveColor={[0.5, 0.5, 0.5]}
          disableAnimation
          enableMouseInteraction
          mouseRadius={0.3}
          colorNum={13.7}
          waveAmplitude={0.24}
          waveFrequency={10}
          waveSpeed={0.02}
        />
        <div className="ad-bg-overlay" />
      </div>

      <nav className="ad-nav">
        <Link to="/admin" className="ad-nav-brand">
          <img src="/logo.png" alt="Job Connect" className="ad-nav-logo" />
          <span className="ad-nav-title">Administrador</span>
        </Link>
        <div className="ad-nav-links">
          {[
            ['/admin', 'Dashboard'],
            ['/admin/empresas', 'Empresas'],
            ['/admin/oferentes', 'Oferentes'],
            ['/admin/caracteristicas','Características'],
          ].map(([to, label]) => (
            <Link key={to} to={to} className="ad-nav-link">{label}</Link>
          ))}
          <button onClick={logout} className="ad-nav-logout">Salir</button>
        </div>
      </nav>

      <div className="page ad-page-content">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="empresas" element={<Empresas />} />
          <Route path="oferentes" element={<Oferentes />} />
          <Route path="caracteristicas" element={<Caracteristicas />} />
        </Routes>
      </div>
    </div>
  )
}
