import { useEffect, useState } from 'react'
import { Routes, Route, Link, useNavigate, useParams } from 'react-router-dom'
import { api } from '../../services/api'
import Balatro from '../../components/Balatro'

// ─── Estilos base ────────────────────────────────────────────────
const card = {
  background: 'rgba(0,0,0,.93)',
  border: '1px solid rgba(255,255,255,.12)',
  borderRadius: 14,
  boxShadow: '0 4px 24px rgba(0,0,0,.7)',
}
const solidBtn = {
  background: '#308bcd',
  color: 'white',
  border: 'none',
  borderRadius: 10,
  padding: '10px 22px',
  fontSize: '0.875rem',
  fontWeight: 700,
  cursor: 'pointer',
  transition: 'opacity .2s',
  textDecoration: 'none',
  display: 'inline-block',
}
const outlineBtn = {
  background: 'transparent',
  border: '1px solid rgba(255,255,255,.2)',
  borderRadius: 10,
  padding: '10px 20px',
  color: 'rgba(255,255,255,.8)',
  fontSize: '0.875rem',
  fontWeight: 500,
  cursor: 'pointer',
  textDecoration: 'none',
  display: 'inline-block',
}
const rowItem = {
  display: 'flex', justifyContent: 'space-between', alignItems: 'center',
  background: 'rgba(255,255,255,.08)', borderRadius: 10, padding: '10px 14px',
  border: '1px solid rgba(255,255,255,.1)',
}
const inputStyle = {
  width: '100%', padding: '9px 12px', borderRadius: 8,
  background: 'rgba(255,255,255,.08)', border: '1px solid rgba(255,255,255,.15)',
  color: 'white', fontSize: '0.9rem', outline: 'none', boxSizing: 'border-box',
}
const labelStyle = {
  display: 'block', fontSize: '0.875rem', fontWeight: 500,
  color: 'rgba(255,255,255,.65)', marginBottom: 6,
}

// ─── Dashboard (inicio) ──────────────────────────────────────────
function Dashboard() {
  const [info, setInfo] = useState(null)
  useEffect(() => { api.get('/empresa/dashboard').then(setInfo).catch(() => {}) }, [])

  const cards = [
    { to: '/empresa/puestos',        emoji: '📋', label: 'Mis puestos',       sub: 'Gestiona tus publicaciones'     },
    { to: '/empresa/puestos/nuevo',  emoji: '➕', label: 'Publicar puesto',   sub: 'Crea una nueva oferta laboral'  },
    { to: '/empresa/buscar-puestos', emoji: '🔍', label: 'Buscar candidatos', sub: 'Encuentra el perfil ideal'      },
  ]

  return (
    <div style={{ textAlign: 'center', paddingTop: 48, paddingBottom: 48 }}>
      <div style={{ marginBottom: 4, fontSize: '0.85rem', color: 'rgba(255,255,255,.35)', letterSpacing: 2, textTransform: 'uppercase', fontWeight: 600 }}>
        Portal Empresa
      </div>
      <h1 style={{ color: 'white', fontSize: '2.6rem', marginBottom: 6, lineHeight: 1.15 }}>
        {info ? `Hola, ${info.nombre}` : 'Bienvenido'}
      </h1>
      {info && (
        <p style={{ color: 'rgba(255,255,255,.4)', fontSize: '0.95rem', marginBottom: 52, marginTop: 0 }}>
          {info.correo}{info.ubicacion ? ` · ${info.ubicacion}` : ''}
        </p>
      )}

      <div style={{ display: 'flex', gap: 24, justifyContent: 'center', flexWrap: 'wrap' }}>
        {cards.map(c => (
          <Link key={c.to} to={c.to} style={{ textDecoration: 'none' }}>
            <div style={{
              ...card, width: 210, padding: '36px 24px', textAlign: 'center',
              cursor: 'pointer', transition: 'transform .25s, background .2s',
            }}
              onMouseEnter={e => { e.currentTarget.style.transform = 'translateY(-5px)'; e.currentTarget.style.background = 'rgba(0,0,0,.97)' }}
              onMouseLeave={e => { e.currentTarget.style.transform = 'translateY(0)';    e.currentTarget.style.background = 'rgba(0,0,0,.93)' }}
            >
              <div style={{
                width: 56, height: 56, borderRadius: '50%', margin: '0 auto 16px',
                background: 'rgba(48,139,205,.15)', border: '1.5px solid rgba(48,139,205,.35)',
                display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1.6rem',
              }}>
                {c.emoji}
              </div>
              <div style={{ color: 'white', fontWeight: 700, fontSize: '1.05rem', marginBottom: 6 }}>{c.label}</div>
              <div style={{ color: 'rgba(255,255,255,.4)', fontSize: '0.8rem', lineHeight: 1.5 }}>{c.sub}</div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  )
}

// ─── Mis puestos ─────────────────────────────────────────────────
function MisPuestos() {
  const [puestos, setPuestos] = useState([])
  useEffect(() => { api.get('/empresa/puestos').then(setPuestos).catch(() => {}) }, [])

  async function action(path) {
    await api.post(path)
    api.get('/empresa/puestos').then(setPuestos).catch(() => {})
  }

  return (
    <div style={{ maxWidth: 780, margin: '0 auto', paddingTop: 32 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 24 }}>
        <h2 style={{ color: 'white', margin: 0 }}>Mis puestos</h2>
        <Link to="/empresa/puestos/nuevo" style={{ ...solidBtn, padding: '9px 20px', fontSize: '0.85rem' }}>+ Nuevo puesto</Link>
      </div>

      {puestos.length === 0 ? (
        <div style={{ ...card, padding: 40, textAlign: 'center' }}>
          <p style={{ color: 'rgba(255,255,255,.4)' }}>No hay puestos publicados aún.</p>
        </div>
      ) : (
        <div className="grid">
          {puestos.map(p => (
            <div key={p.id} style={{ ...card, padding: 20 }}>
              <div style={{ fontWeight: 700, fontSize: '1rem', color: 'white', marginBottom: 10 }}>{p.descripcion}</div>
              <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 14 }}>
                <span style={{ padding: '3px 10px', borderRadius: 999, fontSize: '0.75rem', fontWeight: 600, background: 'rgba(34,197,94,.2)', color: '#4ade80', border: '1px solid rgba(34,197,94,.3)' }}>
                  ₡{p.salario?.toLocaleString()}
                </span>
                <span style={{ padding: '3px 10px', borderRadius: 999, fontSize: '0.75rem', fontWeight: 600, background: p.activo ? 'rgba(48,139,205,.2)' : 'rgba(255,255,255,.08)', color: p.activo ? '#7dd3fc' : 'rgba(255,255,255,.4)', border: `1px solid ${p.activo ? 'rgba(48,139,205,.35)' : 'rgba(255,255,255,.12)'}` }}>
                  {p.activo ? 'Activo' : 'Inactivo'}
                </span>
                <span style={{ padding: '3px 10px', borderRadius: 999, fontSize: '0.75rem', fontWeight: 600, background: p.acceso ? 'rgba(34,197,94,.15)' : 'rgba(255,255,255,.08)', color: p.acceso ? '#86efac' : 'rgba(255,255,255,.4)', border: `1px solid ${p.acceso ? 'rgba(34,197,94,.25)' : 'rgba(255,255,255,.12)'}` }}>
                  {p.acceso ? 'Público' : 'Privado'}
                </span>
              </div>
              <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                {p.activo
                  ? <button style={{ ...outlineBtn, padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/desactivar`)}>Desactivar</button>
                  : <button style={{ ...solidBtn, padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/activar`)}>Activar</button>}
                {p.acceso
                  ? <button style={{ ...outlineBtn, padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/acceso-privado`)}>Hacer privado</button>
                  : <button style={{ ...outlineBtn, padding: '6px 14px', fontSize: '0.8rem' }} onClick={() => action(`/empresa/puestos/${p.id}/acceso-publico`)}>Hacer público</button>}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

// ─── Nuevo puesto ────────────────────────────────────────────────
function NuevoPuesto() {
  const navigate = useNavigate()
  const [caracteristicas, setCaracteristicas] = useState([])
  const [form, setForm] = useState({ descripcion: '', salario: '' })
  const [seleccionadas, setSeleccionadas] = useState([])
  const [error, setError] = useState('')

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
    <div style={{ maxWidth: 560, margin: '0 auto', paddingTop: 32 }}>
      <h2 style={{ color: 'white', marginBottom: 28 }}>Publicar puesto</h2>
      <div style={{ ...card, padding: 32 }}>
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: 18 }}>
            <label style={labelStyle}>Descripción del puesto</label>
            <input style={inputStyle} value={form.descripcion}
              onChange={e => setForm({ ...form, descripcion: e.target.value })} required />
          </div>
          <div style={{ marginBottom: 18 }}>
            <label style={labelStyle}>Salario (₡)</label>
            <input style={inputStyle} type="number" value={form.salario}
              onChange={e => setForm({ ...form, salario: e.target.value })} required />
          </div>
          {caracteristicas.length > 0 && (
            <div style={{ marginBottom: 24 }}>
              <label style={labelStyle}>Características requeridas</label>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8, marginTop: 8 }}>
                {caracteristicas.map(c => (
                  <label key={c.id} style={{ display: 'flex', gap: 8, alignItems: 'center', fontSize: '0.875rem', color: seleccionadas.includes(c.id) ? '#7dd3fc' : 'rgba(255,255,255,.6)', cursor: 'pointer', fontWeight: seleccionadas.includes(c.id) ? 600 : 400 }}>
                    <input type="checkbox" checked={seleccionadas.includes(c.id)} onChange={() => toggleCar(c.id)}
                      style={{ width: 15, height: 15, accentColor: '#308bcd' }} />
                    {c.nombre}
                  </label>
                ))}
              </div>
            </div>
          )}
          {error && (
            <div style={{ marginBottom: 16, padding: '10px 14px', borderRadius: 8, background: 'rgba(220,38,38,.15)', color: '#f87171', border: '1px solid rgba(220,38,38,.3)', fontSize: '0.875rem' }}>
              {error}
            </div>
          )}
          <div style={{ display: 'flex', gap: 10 }}>
            <button type="submit" style={{ ...solidBtn, padding: '10px 24px' }}>Publicar</button>
            <Link to="/empresa/puestos" style={{ ...outlineBtn, padding: '10px 20px' }}>Cancelar</Link>
          </div>
        </form>
      </div>
    </div>
  )
}

// ─── Buscar candidatos ───────────────────────────────────────────
function BuscarCandidatos() {
  const [puestos, setPuestos] = useState([])
  useEffect(() => { api.get('/empresa/buscar-puestos').then(setPuestos).catch(() => {}) }, [])

  return (
    <div style={{ maxWidth: 780, margin: '0 auto', paddingTop: 32 }}>
      <h2 style={{ color: 'white', marginBottom: 24 }}>Puestos abiertos a candidatos</h2>
      {puestos.length === 0 ? (
        <div style={{ ...card, padding: 40, textAlign: 'center' }}>
          <p style={{ color: 'rgba(255,255,255,.4)' }}>No hay puestos abiertos sin candidato asignado.</p>
        </div>
      ) : (
        <div className="grid">
          {puestos.map(p => (
            <div key={p.id} style={{ ...card, padding: 20 }}>
              <div style={{ fontWeight: 700, fontSize: '1rem', color: 'white', marginBottom: 14 }}>{p.descripcion}</div>
              <Link to={`/empresa/puestos/${p.id}/candidatos`} style={{ ...solidBtn, padding: '7px 16px', fontSize: '0.8rem' }}>
                Ver candidatos
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

// ─── Candidatos ──────────────────────────────────────────────────
function Candidatos() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [candidatos, setCandidatos] = useState([])
  const [error, setError] = useState('')

  useEffect(() => { api.get(`/empresa/puestos/${id}/candidatos`).then(setCandidatos).catch(() => {}) }, [id])

  async function asignar(idOferente) {
    try {
      await api.post(`/empresa/puestos/${id}/asignar-candidato`, { idOferente })
      navigate('/empresa/buscar-puestos')
    } catch (err) { setError(err.message) }
  }

  return (
    <div style={{ maxWidth: 780, margin: '0 auto', paddingTop: 32 }}>
      <h2 style={{ color: 'white', marginBottom: 24 }}>Candidatos compatibles</h2>
      {error && (
        <div style={{ marginBottom: 16, padding: '10px 14px', borderRadius: 8, background: 'rgba(220,38,38,.15)', color: '#f87171', border: '1px solid rgba(220,38,38,.3)', fontSize: '0.875rem' }}>
          {error}
        </div>
      )}
      {candidatos.length === 0 ? (
        <div style={{ ...card, padding: 40, textAlign: 'center' }}>
          <p style={{ color: 'rgba(255,255,255,.4)' }}>No hay candidatos disponibles.</p>
        </div>
      ) : (
        <div className="grid">
          {candidatos.map(c => (
            <div key={c.idOferente} style={{ ...card, padding: 20 }}>
              <div style={{ fontWeight: 700, fontSize: '1rem', color: 'white', marginBottom: 4 }}>{c.nombre} {c.apellido}</div>
              <p style={{ color: 'rgba(255,255,255,.45)', fontSize: '0.875rem', margin: '0 0 10px' }}>{c.correo}</p>
              <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 14 }}>
                <span style={{ padding: '3px 10px', borderRadius: 999, fontSize: '0.75rem', fontWeight: 600, background: 'rgba(48,139,205,.2)', color: '#7dd3fc', border: '1px solid rgba(48,139,205,.35)' }}>
                  Coincidencia: {c.porcentajeCoincidencia}%
                </span>
                <span style={{ padding: '3px 10px', borderRadius: 999, fontSize: '0.75rem', fontWeight: 600, background: 'rgba(255,255,255,.08)', color: 'rgba(255,255,255,.5)', border: '1px solid rgba(255,255,255,.12)' }}>
                  {c.coincidencias}/{c.totalRequeridas} requisitos
                </span>
              </div>
              <button style={{ ...solidBtn, padding: '7px 16px', fontSize: '0.8rem' }} onClick={() => asignar(c.idOferente)}>
                Asignar candidato
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

// ─── Shell ───────────────────────────────────────────────────────
export default function EmpresaDashboard() {
  const navigate = useNavigate()
  function logout() { localStorage.clear(); navigate('/') }

  return (
    <div style={{ minHeight: '100vh', background: '#020d10', position: 'relative' }}>
      <div style={{ position: 'fixed', inset: 0, zIndex: 0, pointerEvents: 'none' }}>
        <Balatro
          isRotate={false}
          mouseInteraction={false}
          pixelFilter={2000}
          color1="#308bcd"
          color2="#ffffff"
          color3="#00363e"
        />
        <div style={{ position: 'absolute', inset: 0, background: 'rgba(0,0,0,.2)' }} />
      </div>

      <nav style={{
        display: 'flex', justifyContent: 'space-between', alignItems: 'center',
        padding: '0 28px', height: 64,
        background: 'rgba(0,0,0,.95)',
        borderBottom: '1px solid rgba(255,255,255,.08)',
        position: 'relative', zIndex: 10,
      }}>
        <Link to="/empresa" style={{ display: 'flex', alignItems: 'center', gap: 10, textDecoration: 'none' }}>
          <img src="/logo.png" alt="Job Connect" style={{ height: 34, width: 'auto' }} />
          <span style={{ fontWeight: 800, fontSize: '1.1rem', color: 'white' }}>Empresa</span>
        </Link>
        <div style={{ display: 'flex', gap: 4, alignItems: 'center' }}>
          {[
            ['/empresa',              'Inicio'],
            ['/empresa/puestos',      'Mis puestos'],
            ['/empresa/buscar-puestos','Candidatos'],
          ].map(([to, label]) => (
            <Link key={to} to={to} style={{
              color: 'rgba(255,255,255,.65)', padding: '6px 14px', borderRadius: 8,
              fontSize: '0.875rem', fontWeight: 500, textDecoration: 'none', transition: 'color .15s',
            }}
              onMouseEnter={e => { e.currentTarget.style.color = 'white'; e.currentTarget.style.background = 'rgba(255,255,255,.08)' }}
              onMouseLeave={e => { e.currentTarget.style.color = 'rgba(255,255,255,.65)'; e.currentTarget.style.background = 'transparent' }}
            >
              {label}
            </Link>
          ))}
          <button onClick={logout} style={{ ...outlineBtn, padding: '6px 16px', fontSize: '0.8rem', marginLeft: 8 }}>
            Salir
          </button>
        </div>
      </nav>

      <div className="page" style={{ position: 'relative', zIndex: 1 }}>
        <Routes>
          <Route path="/"                       element={<Dashboard />} />
          <Route path="puestos"                 element={<MisPuestos />} />
          <Route path="puestos/nuevo"           element={<NuevoPuesto />} />
          <Route path="puestos/:id/candidatos"  element={<Candidatos />} />
          <Route path="buscar-puestos"          element={<BuscarCandidatos />} />
        </Routes>
      </div>
    </div>
  )
}
