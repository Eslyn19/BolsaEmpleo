import { useEffect, useState } from 'react'
import { Routes, Route, Link, useNavigate, useParams } from 'react-router-dom'
import { api } from '../../services/api'
import Balatro from '../../components/Balatro'
import './EmpresaDashboard.css'

function Dashboard() {
  const [info, setInfo] = useState(null)
  useEffect(() => { api.get('/empresa/dashboard').then(setInfo).catch(() => {}) }, [])

  const cards = [
    { to: '/empresa/puestos', emoji: '📋', label: 'Mis puestos', sub: 'Gestiona tus publicaciones' },
    { to: '/empresa/puestos/nuevo', emoji: '➕', label: 'Publicar puesto', sub: 'Crea una nueva oferta laboral' },
    { to: '/empresa/buscar-puestos', emoji: '🔍', label: 'Buscar candidatos', sub: 'Encuentra el perfil ideal' },
  ]

  return (
    <div className="ed-dashboard">
      <div className="ed-eyebrow">Portal Empresa</div>
      <h1 className="ed-hero-title">{info ? `Hola, ${info.nombre}` : 'Bienvenido'}</h1>
      {info && (
        <p className="ed-hero-sub">
          {info.correo}{info.ubicacion ? ` · ${info.ubicacion}` : ''}
        </p>
      )}

      <div className="ed-nav-cards">
        {cards.map(c => (
          <Link key={c.to} to={c.to} className="ed-nav-card">
            <div className="ed-nav-card-icon">{c.emoji}</div>
            <div className="ed-nav-card-label">{c.label}</div>
            <div className="ed-nav-card-sub">{c.sub}</div>
          </Link>
        ))}
      </div>
    </div>
  )
}

function MisPuestos() {
  const [puestos, setPuestos] = useState([])
  useEffect(() => { api.get('/empresa/puestos').then(setPuestos).catch(() => {}) }, [])

  async function action(path) {
    await api.post(path)
    api.get('/empresa/puestos').then(setPuestos).catch(() => {})
  }

  return (
    <div className="ed-page">
      <div className="ed-page-header">
        <h2 className="ed-page-title">Mis puestos</h2>
        <Link to="/empresa/puestos/nuevo" className="ed-btn-solid" style={{ padding: '9px 20px', fontSize: '0.85rem' }}>+ Nuevo puesto</Link>
      </div>

      {puestos.length === 0 ? (
        <div className="ed-card ed-empty-state"><p>No hay puestos publicados aún.</p></div>
      ) : (
        <div className="grid">
          {puestos.map(p => (
            <div key={p.id} className="ed-card ed-puesto-card">
              <div className="ed-puesto-name">{p.descripcion}</div>
              <div className="ed-puesto-badges">
                <span className="ed-badge ed-badge--salary">₡{p.salario?.toLocaleString()}</span>
                <span className={`ed-badge ${p.activo ? 'ed-badge--blue' : 'ed-badge--dim'}`}>
                  {p.activo ? 'Activo' : 'Inactivo'}
                </span>
                <span className={`ed-badge ${p.acceso ? 'ed-badge--green' : 'ed-badge--dim'}`}>
                  {p.acceso ? 'Público' : 'Privado'}
                </span>
              </div>
              <div className="ed-puesto-actions">
                {p.activo
                  ? <button className="ed-btn-outline" style={{ padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/desactivar`)}>Desactivar</button>
                  : <button className="ed-btn-solid"  style={{ padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/activar`)}>Activar</button>}
                {p.acceso
                  ? <button className="ed-btn-outline" style={{ padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/acceso-privado`)}>Hacer privado</button>
                  : <button className="ed-btn-outline" style={{ padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/acceso-publico`)}>Hacer público</button>}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

function NuevoPuesto() {
  const navigate = useNavigate()
  const [caracteristicas, setCaracteristicas] = useState([])
  const [form, setForm]             = useState({ descripcion: '', salario: '' })
  const [seleccionadas, setSeleccionadas] = useState([])
  const [error, setError]           = useState('')

  useEffect(() => { api.get('/empresa/caracteristicas').then(setCaracteristicas).catch(() => {}) }, [])

  function toggleCar(id) {
    setSeleccionadas(s => s.includes(id) ? s.filter(x => x !== id) : [...s, id])
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    try {
      await api.post('/empresa/puestos', { descripcion: form.descripcion, salario: parseFloat(form.salario), idCaracteristica: seleccionadas })
      navigate('/empresa/puestos')
    } catch (err) { setError(err.message) }
  }

  return (
    <div className="ed-form-page">
      <h2 style={{ color: 'white', marginBottom: 28 }}>Publicar puesto</h2>
      <div className="ed-card ed-form-card">
        <form onSubmit={handleSubmit}>
          <div className="ed-form-field">
            <label className="ed-label">Descripción del puesto</label>
            <input className="ed-input" value={form.descripcion}
              onChange={e => setForm({ ...form, descripcion: e.target.value })} required />
          </div>
          <div className="ed-form-field">
            <label className="ed-label">Salario (₡)</label>
            <input className="ed-input" type="number" value={form.salario}
              onChange={e => setForm({ ...form, salario: e.target.value })} required />
          </div>
          {caracteristicas.length > 0 && (
            <div style={{ marginBottom: 24 }}>
              <label className="ed-label">Características requeridas</label>
              <div className="ed-char-grid">
                {caracteristicas.map(c => (
                  <label key={c.id} style={{
                    display: 'flex', gap: 8, alignItems: 'center', fontSize: '0.875rem', cursor: 'pointer',
                    color: seleccionadas.includes(c.id) ? '#7dd3fc' : 'rgba(255,255,255,.6)',
                    fontWeight: seleccionadas.includes(c.id) ? 600 : 400,
                  }}>
                    <input type="checkbox" checked={seleccionadas.includes(c.id)} onChange={() => toggleCar(c.id)}
                      style={{ width: 15, height: 15, accentColor: '#308bcd' }} />
                    {c.nombre}
                  </label>
                ))}
              </div>
            </div>
          )}
          {error && <div className="ed-error-msg">{error}</div>}
          <div className="ed-form-actions">
            <button type="submit" className="ed-btn-solid" style={{ padding: '10px 24px' }}>Publicar</button>
            <Link to="/empresa/puestos" className="ed-btn-outline" style={{ padding: '10px 20px' }}>Cancelar</Link>
          </div>
        </form>
      </div>
    </div>
  )
}

function BuscarCandidatos() {
  const [puestos, setPuestos] = useState([])
  useEffect(() => { api.get('/empresa/buscar-puestos').then(setPuestos).catch(() => {}) }, [])

  return (
    <div className="ed-page">
      <h2 style={{ color: 'white', marginBottom: 24 }}>Puestos abiertos a candidatos</h2>
      {puestos.length === 0 ? (
        <div className="ed-card ed-empty-state"><p>No hay puestos abiertos sin candidato asignado.</p></div>
      ) : (
        <div className="grid">
          {puestos.map(p => (
            <div key={p.id} className="ed-card" style={{ padding: 20 }}>
              <div className="ed-puesto-name">{p.descripcion}</div>
              <Link to={`/empresa/puestos/${p.id}/candidatos`} className="ed-btn-solid" style={{ padding: '7px 16px', fontSize: '0.8rem' }}>
                Ver candidatos
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

function Candidatos() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [candidatos, setCandidatos] = useState([])
  const [error, setError]           = useState('')

  useEffect(() => { api.get(`/empresa/puestos/${id}/candidatos`).then(setCandidatos).catch(() => {}) }, [id])

  async function asignar(idOferente) {
    try {
      await api.post(`/empresa/puestos/${id}/asignar-candidato`, { idOferente })
      navigate('/empresa/buscar-puestos')
    } catch (err) { setError(err.message) }
  }

  return (
    <div className="ed-page">
      <h2 style={{ color: 'white', marginBottom: 24 }}>Candidatos compatibles</h2>
      {error && <div className="ed-error-msg">{error}</div>}
      {candidatos.length === 0 ? (
        <div className="ed-card ed-empty-state"><p>No hay candidatos disponibles.</p></div>
      ) : (
        <div className="grid">
          {candidatos.map(c => (
            <div key={c.idOferente} className="ed-card ed-candidato-card">
              <div className="ed-candidato-name">{c.nombre} {c.apellido}</div>
              <p className="ed-candidato-meta">{c.correo}</p>
              <div className="ed-candidato-badges">
                <span className="ed-badge ed-badge--blue">Coincidencia: {c.porcentajeCoincidencia}%</span>
                <span className="ed-badge ed-badge--dim">{c.coincidencias}/{c.totalRequeridas} requisitos</span>
              </div>
              <button className="ed-btn-solid" style={{ padding: '7px 16px', fontSize: '0.8rem' }} onClick={() => asignar(c.idOferente)}>
                Asignar candidato
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default function EmpresaDashboard() {
  const navigate = useNavigate()
  function logout() { localStorage.clear(); navigate('/') }

  return (
    <div className="ed-shell">
      <div className="ed-bg">
        <Balatro
          isRotate={false}
          mouseInteraction={false}
          pixelFilter={2000}
          color1="#308bcd"
          color2="#ffffff"
          color3="#00363e"
        />
        <div className="ed-bg-overlay" />
      </div>

      <nav className="ed-nav">
        <Link to="/empresa" className="ed-nav-brand">
          <img src="/logo.png" alt="Job Connect" className="ed-nav-logo" />
          <span className="ed-nav-title">Empresa</span>
        </Link>
        <div className="ed-nav-links">
          {[
            ['/empresa', 'Inicio' ],
            ['/empresa/puestos', 'Mis puestos' ],
            ['/empresa/buscar-puestos','Candidatos' ],
          ].map(([to, label]) => (
            <Link key={to} to={to} className="ed-nav-link">{label}</Link>
          ))}
          <button onClick={logout} className="ed-nav-logout">Salir</button>
        </div>
      </nav>

      <div className="page ed-page-content">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="puestos" element={<MisPuestos />} />
          <Route path="puestos/nuevo" element={<NuevoPuesto />} />
          <Route path="puestos/:id/candidatos" element={<Candidatos />} />
          <Route path="buscar-puestos" element={<BuscarCandidatos />} />
        </Routes>
      </div>
    </div>
  )
}
