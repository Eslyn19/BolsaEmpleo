import { useEffect, useState } from 'react'
import { Routes, Route, Link, useNavigate } from 'react-router-dom'
import { api } from '../../services/api'

function Dashboard() {
  const [stats, setStats] = useState(null)
  useEffect(() => { api.get('/admin/dashboard').then(setStats).catch(() => {}) }, [])
  return (
    <div>
      <h2>Resumen</h2>
      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-number">{stats.empresasPendientes}</div>
            <div className="stat-label">Empresas pendientes</div>
          </div>
          <div className="stat-card">
            <div className="stat-number">{stats.oferentesPendientes}</div>
            <div className="stat-label">Oferentes pendientes</div>
          </div>
        </div>
      )}
      <div className="dash-nav">
        <Link to="/admin/empresas">Aprobar empresas</Link>
        <Link to="/admin/oferentes">Aprobar oferentes</Link>
        <Link to="/admin/caracteristicas">Características</Link>
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
    <div>
      <h2>Empresas pendientes</h2>
      {lista.length === 0 ? (
        <div className="card"><p>No hay empresas pendientes.</p></div>
      ) : lista.map(e => (
        <div key={e.idUsuario} className="card">
          <div className="card-title">{e.nombre}</div>
          <p>{e.correo} · {e.ubicacion}</p>
          <p>{e.descripcion}</p>
          <div className="card-actions">
            <select value={tipo[e.idUsuario] || ''}
              onChange={ev => setTipo({ ...tipo, [e.idUsuario]: ev.target.value })}
              style={{ padding: '6px 10px', borderRadius: 6, border: '1px solid var(--border)', fontSize: '0.875rem' }}>
              <option value="">Tipo de empresa…</option>
              <option value="Pública">Pública</option>
              <option value="Privada">Privada</option>
            </select>
            <button className="btn btn-primary btn-sm" onClick={() => aprobar(e.idUsuario)}>Aprobar</button>
            <button className="btn btn-danger btn-sm" onClick={() => rechazar(e.idUsuario)}>Rechazar</button>
          </div>
        </div>
      ))}
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
    <div>
      <h2>Oferentes pendientes</h2>
      {lista.length === 0 ? (
        <div className="card"><p>No hay oferentes pendientes.</p></div>
      ) : lista.map(o => (
        <div key={o.idUsuario} className="card">
          <div className="card-title">{o.nombre} {o.apellido}</div>
          <p>{o.correo} · {o.nacionalidad}</p>
          <div className="card-actions">
            <button className="btn btn-primary btn-sm" onClick={() => aprobar(o.idUsuario)}>Aprobar</button>
            <button className="btn btn-danger btn-sm" onClick={() => rechazar(o.idUsuario)}>Rechazar</button>
          </div>
        </div>
      ))}
    </div>
  )
}

function Caracteristicas() {
  const [data, setData] = useState(null)
  const [parentId, setParentId] = useState(null)
  const [nombre, setNombre] = useState('')
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

  if (!data) return <p>Cargando...</p>

  return (
    <div>
      <h2>Características</h2>
      {parentId != null && (
        <button className="btn btn-outline btn-sm" onClick={() => cargar(null)} style={{ marginBottom: 16 }}>
          ← Volver a raíces
        </button>
      )}
      <div className="grid" style={{ marginBottom: 24 }}>
        {data.items.map(c => (
          <div key={c.id} className="card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <div>
              <span style={{ fontWeight: 600 }}>{c.nombre}</span>
              <span className={`badge ${c.activo ? 'badge-green' : 'badge-gray'}`} style={{ marginLeft: 10 }}>
                {c.activo ? 'Activa' : 'Inactiva'}
              </span>
            </div>
            <div className="card-actions">
              <button className="btn btn-outline btn-sm" onClick={() => cargar(c.id)}>Ver hijos</button>
              <button className="btn btn-outline btn-sm" onClick={() => toggle(c.id)}>Toggle</button>
            </div>
          </div>
        ))}
      </div>

      <div className="card">
        <h3>Agregar característica</h3>
        <form onSubmit={crear} style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
          <input placeholder="Nombre" value={nombre} onChange={e => setNombre(e.target.value)} required
            style={{ flex: 1, minWidth: 160, padding: '8px 12px', border: '1px solid var(--border)', borderRadius: 6 }} />
          <select value={padreSelect} onChange={e => setPadreSelect(e.target.value)} required
            style={{ padding: '8px 12px', border: '1px solid var(--border)', borderRadius: 6 }}>
            <option value="">Padre…</option>
            {data.padresSelect.map(p => <option key={p.id} value={p.id}>{p.nombre}</option>)}
          </select>
          <button type="submit" className="btn btn-primary">Agregar</button>
        </form>
      </div>
    </div>
  )
}

export default function AdminDashboard() {
  const navigate = useNavigate()
  function logout() { localStorage.clear(); navigate('/') }

  return (
    <>
      <nav className="navbar">
        <Link to="/admin" className="navbar-brand" style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <img src="/logo.png" alt="Job Connect" style={{ height: 34, width: 'auto' }} />
          <span>Administrador</span>
        </Link>
        <div className="navbar-links">
          <Link to="/admin">Dashboard</Link>
          <Link to="/admin/empresas">Empresas</Link>
          <Link to="/admin/oferentes">Oferentes</Link>
          <Link to="/admin/caracteristicas">Características</Link>
          <button className="btn btn-glass btn-sm" onClick={logout}>Salir</button>
        </div>
      </nav>
      <div className="page">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="empresas" element={<Empresas />} />
          <Route path="oferentes" element={<Oferentes />} />
          <Route path="caracteristicas" element={<Caracteristicas />} />
        </Routes>
      </div>
    </>
  )
}
