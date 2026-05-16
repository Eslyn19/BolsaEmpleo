import { useEffect, useState, useRef } from 'react'
import { Routes, Route, Link, useNavigate } from 'react-router-dom'
import { api } from '../../services/api'
import Balatro from '../../components/Balatro'

// ─── Estilos base ────────────────────────────────────────────────
const card = {
  background: 'rgba(0,0,0,.93)',
  border: '1px solid rgba(255,255,255,.14)',
  borderRadius: 14,
  boxShadow: '0 4px 24px rgba(0,0,0,.8)',
}

const solidBtn = {
  background: '#35b389',
  color: 'white',
  border: 'none',
  borderRadius: 10,
  padding: '11px 28px',
  fontSize: '0.9rem',
  fontWeight: 700,
  cursor: 'pointer',
  transition: 'opacity .2s',
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
}

const rowItem = {
  display: 'flex', justifyContent: 'space-between', alignItems: 'center',
  background: 'rgba(255,255,255,.08)', borderRadius: 10, padding: '10px 14px',
  border: '1px solid rgba(255,255,255,.1)',
}

function Dashboard() {
  const [info, setInfo] = useState(null)
  useEffect(() => { api.get('/oferente/dashboard').then(setInfo).catch(() => {}) }, [])

  const cards = [
    { to: '/oferente/habilidades', emoji: '⚡', label: 'Mis habilidades', sub: 'Gestiona tus habilidades técnicas' },
    { to: '/oferente/cv', emoji: '📄', label: 'Mi CV', sub: 'Sube y actualiza tu currículum'   },
  ]

  return (
    <div style={{ textAlign: 'center', paddingTop: 48, paddingBottom: 48 }}>
      <div style={{ marginBottom: 4, fontSize: '0.85rem', color: 'rgba(255,255,255,.35)', letterSpacing: 2, textTransform: 'uppercase', fontWeight: 600 }}>
        Portal Oferente
      </div>
      <h1 style={{ color: 'white', fontSize: '2.6rem', marginBottom: 6, lineHeight: 1.15 }}>
        {info ? `Hola, ${info.nombre} ${info.apellido}` : 'Bienvenido'}
      </h1>
      {info && (
        <p style={{ color: 'rgba(255,255,255,.4)', fontSize: '0.95rem', marginBottom: 52, marginTop: 0 }}>
          {info.correo}
        </p>
      )}

      <div style={{ display: 'flex', gap: 24, justifyContent: 'center', flexWrap: 'wrap' }}>
        {cards.map(c => (
          <Link key={c.to} to={c.to} style={{ textDecoration: 'none' }}>
            <div style={{
              ...card,
              width: 210, padding: '36px 24px', textAlign: 'center',
              cursor: 'pointer', transition: 'transform .25s, box-shadow .25s',
            }}
              onMouseEnter={e => { e.currentTarget.style.transform = 'translateY(-5px)'; e.currentTarget.style.background = 'rgba(0,0,0,.97)' }}
              onMouseLeave={e => { e.currentTarget.style.transform = 'translateY(0)';    e.currentTarget.style.background = 'rgba(0,0,0,.93)'  }}
            >
              <div style={{
                width: 56, height: 56, borderRadius: '50%', margin: '0 auto 16px',
                background: 'rgba(53,179,137,.15)', border: '1.5px solid rgba(53,179,137,.35)',
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

function Habilidades() {
  const [data, setData]     = useState(null)
  const [actual, setActual] = useState(null)
  const [nivel, setNivel]   = useState(3)

  function cargar(id) {
    const q = id != null ? `?actual=${id}` : ''
    api.get(`/oferente/habilidades${q}`).then(d => { setData(d); setActual(id) }).catch(() => {})
  }
  useEffect(() => { cargar(null) }, [])

  async function agregar(habilidadId) {
    await api.post('/oferente/habilidades', { habilidadId, nivel })
    cargar(actual)
  }

  if (!data) return (
    <div style={{ textAlign: 'center', paddingTop: 80, color: 'rgba(255,255,255,.5)' }}>Cargando...</div>
  )

  return (
    <div style={{ maxWidth: 780, margin: '0 auto', paddingTop: 32 }}>
      <h2 style={{ color: 'white', fontSize: '2rem', marginBottom: 6, textAlign: 'center' }}>Mis habilidades</h2>
      <p style={{ color: 'rgba(255,255,255,.38)', textAlign: 'center', marginBottom: 36, marginTop: 0 }}>
        Navega por categorías y agrega las tuyas
      </p>

      {data.rutaActual.length > 0 && (
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 24, flexWrap: 'wrap' }}>
          <button onClick={() => cargar(null)}
            style={{ background: 'none', border: 'none', color: '#35b389', cursor: 'pointer', fontWeight: 600, fontSize: '0.875rem', padding: '4px 0' }}>
            Inicio
          </button>
          {data.rutaActual.map(r => (
            <span key={r.id} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <span style={{ color: 'rgba(255,255,255,.25)' }}>›</span>
              <button onClick={() => cargar(r.id)}
                style={{ background: 'none', border: 'none', color: 'rgba(255,255,255,.7)', cursor: 'pointer', fontWeight: 600, fontSize: '0.875rem', padding: '4px 0' }}>
                {r.nombre}
              </button>
            </span>
          ))}
        </div>
      )}

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 28 }}>
        <div>
          {data.subHabilidades.filter(c => !data.habilidadesFiltradas.find(h => h.id === c.id)).length > 0 && (
            <div style={{ ...card, padding: 24, marginBottom: 20 }}>
              <div style={{ fontSize: '0.75rem', fontWeight: 700, letterSpacing: 2, textTransform: 'uppercase', color: 'rgba(255,255,255,.35)', marginBottom: 14 }}>
                Categorías
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {data.subHabilidades.filter(c => !data.habilidadesFiltradas.find(h => h.id === c.id)).map(c => (
                  <button key={c.id} onClick={() => cargar(c.id)} style={{
                    ...outlineBtn,
                    textAlign: 'left', display: 'flex',
                    justifyContent: 'space-between', alignItems: 'center', padding: '10px 16px',
                  }}>
                    <span>{c.nombre}</span>
                    <span style={{ color: 'rgba(255,255,255,.4)', fontSize: '1.1rem' }}>›</span>
                  </button>
                ))}
              </div>
            </div>
          )}

          {data.habilidadesFiltradas.length > 0 && (
            <div style={{ ...card, padding: 24 }}>
              <div style={{ fontSize: '0.75rem', fontWeight: 700, letterSpacing: 2, textTransform: 'uppercase', color: 'rgba(255,255,255,.35)', marginBottom: 14 }}>
                Agregar habilidad
              </div>
              <div style={{ marginBottom: 16 }}>
                <label style={{ display: 'block', fontSize: '0.8rem', color: 'rgba(255,255,255,.5)', marginBottom: 8 }}>
                  Nivel de dominio (1–5)
                </label>
                <div style={{ display: 'flex', gap: 8 }}>
                  {[1,2,3,4,5].map(n => (
                    <button key={n} onClick={() => setNivel(n)} style={{
                      width: 36, height: 36, borderRadius: 8, border: 'none', cursor: 'pointer',
                      fontWeight: 700, fontSize: '0.85rem', transition: 'background .2s',
                      background: nivel === n ? '#35b389' : 'rgba(255,255,255,.1)',
                      color: nivel === n ? 'white' : 'rgba(255,255,255,.45)',
                    }}>
                      {n}
                    </button>
                  ))}
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {data.habilidadesFiltradas.map(h => (
                  <div key={h.id} style={rowItem}>
                    <span style={{ color: 'rgba(255,255,255,.85)', fontSize: '0.875rem' }}>{h.nombre}</span>
                    <button onClick={() => agregar(h.id)} style={{ ...solidBtn, padding: '6px 14px', fontSize: '0.8rem' }}>
                      + Agregar
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        <div style={{ ...card, padding: 24, alignSelf: 'start' }}>
          <div style={{ fontSize: '0.75rem', fontWeight: 700, letterSpacing: 2, textTransform: 'uppercase', color: 'rgba(255,255,255,.35)', marginBottom: 14 }}>
            Mis habilidades actuales
          </div>
          {data.misHabilidades.length === 0 ? (
            <p style={{ color: 'rgba(255,255,255,.3)', fontSize: '0.875rem', textAlign: 'center', padding: '24px 0', margin: 0 }}>
              Sin habilidades registradas aún
            </p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              {data.misHabilidades.map(h => (
                <div key={h.id?.idCaracteristica} style={rowItem}>
                  <span style={{ color: 'rgba(255,255,255,.85)', fontSize: '0.875rem' }}>{h.idCaracteristica?.nombre}</span>
                  <span style={{
                    padding: '3px 12px', borderRadius: 999, fontSize: '0.75rem', fontWeight: 700,
                    background: 'rgba(53,179,137,.2)', color: '#35b389', border: '1px solid rgba(53,179,137,.35)',
                  }}>
                    Nv. {h.nivel}
                  </span>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

function CV() {
  const [rutaCV, setRutaCV] = useState('')
  const [msg, setMsg] = useState('')
  const [error, setError] = useState('')
  const [dragging, setDragging] = useState(false)
  const [fileName, setFileName] = useState('')
  const inputRef = useRef(null)

  useEffect(() => { api.get('/oferente/cv').then(d => setRutaCV(d.rutaCV)).catch(() => {}) }, [])

  async function handleUpload(e) {
    e.preventDefault()
    const file = inputRef.current?.files[0]
    if (!file) return
    const form = new FormData()
    form.append('archivo', file)
    try {
      const d = await api.postForm('/oferente/cv', form)
      setRutaCV(d.rutaCV)
      setMsg('CV actualizado correctamente.')
      setError('')
      setFileName('')
    } catch (err) { setError(err.message) }
  }

  const handleDrop = e => {
    e.preventDefault(); setDragging(false)
    const file = e.dataTransfer.files[0]
    if (file && inputRef.current) {
      const dt = new DataTransfer(); dt.items.add(file)
      inputRef.current.files = dt.files
      setFileName(file.name)
    }
  }

  return (
    <div style={{ maxWidth: 520, margin: '0 auto', paddingTop: 32, paddingBottom: 48 }}>
      <h2 style={{ color: 'white', fontSize: '2rem', marginBottom: 6, textAlign: 'center' }}>Mi CV</h2>
      <p style={{ color: 'rgba(255,255,255,.38)', textAlign: 'center', marginBottom: 36, marginTop: 0 }}>
        Sube tu currículum en formato PDF
      </p>

      {rutaCV && (
        <div style={{ ...card, padding: 20, marginBottom: 20, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <div>
            <div style={{ fontSize: '0.75rem', fontWeight: 700, letterSpacing: 2, textTransform: 'uppercase', color: 'rgba(255,255,255,.35)', marginBottom: 4 }}>
              CV actual
            </div>
            <div style={{ color: 'rgba(255,255,255,.7)', fontSize: '0.875rem' }}>curriculum.pdf</div>
          </div>
          <a href={rutaCV} target="_blank" rel="noreferrer" style={{
            ...outlineBtn, textDecoration: 'none', display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: '0.8rem',
          }}>
            📥 Ver PDF
          </a>
        </div>
      )}

      <div style={{ ...card, padding: 32 }}>
        <form onSubmit={handleUpload}>
          <div
            onDragOver={e => { e.preventDefault(); setDragging(true) }}
            onDragLeave={() => setDragging(false)}
            onDrop={handleDrop}
            onClick={() => inputRef.current?.click()}
            style={{
              border: `2px dashed ${dragging ? '#35b389' : 'rgba(255,255,255,.18)'}`,
              borderRadius: 12, padding: '40px 24px', textAlign: 'center', cursor: 'pointer',
              background: dragging ? 'rgba(53,179,137,.06)' : 'transparent',
              transition: 'border-color .2s, background .2s', marginBottom: 24,
            }}
          >
            <div style={{ fontSize: '2.6rem', marginBottom: 10 }}>📂</div>
            {fileName ? (
              <>
                <div style={{ color: '#35b389', fontWeight: 600, fontSize: '0.95rem', marginBottom: 4 }}>{fileName}</div>
                <div style={{ color: 'rgba(255,255,255,.35)', fontSize: '0.8rem' }}>Listo para subir</div>
              </>
            ) : (
              <>
                <div style={{ color: 'rgba(255,255,255,.7)', fontWeight: 600, fontSize: '0.95rem', marginBottom: 4 }}>
                  Arrastra tu CV aquí
                </div>
                <div style={{ color: 'rgba(255,255,255,.35)', fontSize: '0.8rem' }}>o haz clic para buscar · Solo PDF</div>
              </>
            )}
            <input ref={inputRef} type="file" name="archivo" accept=".pdf" required
              onChange={e => setFileName(e.target.files[0]?.name || '')}
              style={{ display: 'none' }} />
          </div>

          <button type="submit" style={{ ...solidBtn, width: '100%', fontSize: '1rem', padding: '13px 0' }}>
            Subir CV
          </button>
        </form>

        {msg && (
          <div style={{ marginTop: 16, padding: '12px 16px', borderRadius: 10, fontSize: '0.875rem', background: 'rgba(53,179,137,.15)', color: '#35b389', border: '1px solid rgba(53,179,137,.3)' }}>
            ✓ {msg}
          </div>
        )}
        {error && (
          <div style={{ marginTop: 16, padding: '12px 16px', borderRadius: 10, fontSize: '0.875rem', background: 'rgba(220,38,38,.15)', color: '#f87171', border: '1px solid rgba(220,38,38,.3)' }}>
            ✕ {error}
          </div>
        )}
      </div>
    </div>
  )
}

export default function OferenteDashboard() {
  const navigate = useNavigate()
  function logout() { localStorage.clear(); navigate('/') }

  return (
    <div style={{ minHeight: '100vh', background: '#0d0a14', position: 'relative' }}>
      <div style={{ position: 'fixed', inset: 0, zIndex: 0, pointerEvents: 'none' }}>
        <Balatro
          isRotate={false}
          mouseInteraction={false}
          pixelFilter={2000}
          color1="#35b389"
          color2="#ececec"
          color3="#162325"
        />
        <div style={{ position: 'absolute', inset: 0, background: 'rgba(0,0,0,.2)' }} />
      </div>

      <nav style={{
        display: 'flex', justifyContent: 'space-between', alignItems: 'center',
        padding: '0 28px', height: 64,
        background: 'rgba(0,0,0,.95)',
        borderBottom: '1px solid rgba(255,255,255,.1)',
        position: 'relative', zIndex: 10,
      }}>
        <Link to="/oferente" style={{ display: 'flex', alignItems: 'center', gap: 10, textDecoration: 'none' }}>
          <img src="/logo.png" alt="Job Connect" style={{ height: 34, width: 'auto' }} />
          <span style={{ fontWeight: 800, fontSize: '1.1rem', color: 'white' }}>Oferente</span>
        </Link>
        <div style={{ display: 'flex', gap: 4, alignItems: 'center' }}>
          {[['/oferente', 'Inicio'], ['/oferente/habilidades', 'Habilidades'], ['/oferente/cv', 'CV']].map(([to, label]) => (
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
          <Route path="/"           element={<Dashboard />} />
          <Route path="habilidades" element={<Habilidades />} />
          <Route path="cv"          element={<CV />} />
        </Routes>
      </div>
    </div>
  )
}
