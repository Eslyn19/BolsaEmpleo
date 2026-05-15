import { useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'
import MagicRings from '../components/MagicRings'

function EyeIcon({ open }) {
  return open ? (
    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
    </svg>
  ) : (
    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/>
    </svg>
  )
}

function PasswordInput({ value, onChange, placeholder }) {
  const [show, setShow] = useState(false)
  return (
    <div style={{ position: 'relative' }}>
      <input
        type={show ? 'text' : 'password'}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required
        style={{ ...inputStyle, paddingRight: 40 }}
      />
      <button type="button" onClick={() => setShow(s => !s)}
        style={{ position: 'absolute', right: 10, top: '50%', transform: 'translateY(-50%)', background: 'none', border: 'none', color: 'rgba(255,255,255,.45)', cursor: 'pointer', padding: 2, display: 'flex' }}>
        <EyeIcon open={show} />
      </button>
    </div>
  )
}

export default function RegistroEmpresa() {
  const [form, setForm] = useState({ correo: '', nombre: '', ubicacion: '', telefono: '', descripcion: '', clave: '', claveConfirm: '' })
  const [error, setError] = useState('')
  const [ok, setOk] = useState('')

  const set = key => e => setForm(f => ({ ...f, [key]: e.target.value }))

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    try {
      const data = await api.post('/empresa/registro', form)
      setOk(data.mensaje)
    } catch (err) {
      setError(err.message)
    }
  }

  if (ok) return (
    <div style={pageStyle}>
      <div style={{ position: 'absolute', inset: 0, zIndex: 0 }}><MagicRings {...ringsProps} /></div>
      <div style={{ position: 'relative', zIndex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', padding: 24 }}>
        <div style={cardStyle}>
          <div style={{ textAlign: 'center', color: 'white', padding: '16px 0' }}>
            <div style={{ fontSize: '2rem', marginBottom: 12 }}>✅</div>
            <p style={{ color: 'rgba(255,255,255,.7)', marginBottom: 20 }}>Registro exitoso. Espere aprobación del administrador.</p>
            <Link to="/login" style={backBtnStyle}>Ir al login</Link>
          </div>
        </div>
      </div>
    </div>
  )

  return (
    <div style={pageStyle}>
      <div style={{ position: 'absolute', inset: 0, zIndex: 0 }}><MagicRings {...ringsProps} /></div>

      <div style={{ position: 'relative', zIndex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', padding: 24 }}>
        <div style={{ ...cardStyle, maxWidth: 640 }}>

          {/* Header */}
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
            <Link to="/login" title="Volver al login" style={iconBtnStyle}
              onMouseEnter={e => { e.currentTarget.style.background = 'rgba(16,185,129,.25)'; e.currentTarget.style.borderColor = 'rgba(16,185,129,.5)' }}
              onMouseLeave={e => { e.currentTarget.style.background = 'rgba(255,255,255,.07)'; e.currentTarget.style.borderColor = 'rgba(255,255,255,.14)' }}>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
              </svg>
            </Link>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <img src="/logo.png" alt="logo" style={{ height: 36, filter: 'drop-shadow(0 2px 10px rgba(16,185,129,.5))' }} />
              <span style={{ color: 'white', fontWeight: 700, fontSize: '1rem' }}>Registro de Empresa</span>
            </div>
            <div style={{ width: 38 }} />
          </div>

          <form onSubmit={handleSubmit}>
            {/* Fila 1 */}
            <div style={rowStyle}>
              <div style={colStyle}>
                <label style={labelStyle}>Correo electrónico</label>
                <input type="email" value={form.correo} onChange={set('correo')} placeholder="empresa@correo.com" required style={inputStyle} />
              </div>
              <div style={colStyle}>
                <label style={labelStyle}>Nombre de la empresa</label>
                <input type="text" value={form.nombre} onChange={set('nombre')} placeholder="Mi Empresa S.A." required style={inputStyle} />
              </div>
            </div>

            {/* Fila 2 */}
            <div style={rowStyle}>
              <div style={colStyle}>
                <label style={labelStyle}>Ubicación</label>
                <input type="text" value={form.ubicacion} onChange={set('ubicacion')} placeholder="Provincia, País" required style={inputStyle} />
              </div>
              <div style={colStyle}>
                <label style={labelStyle}>Teléfono</label>
                <input type="text" value={form.telefono} onChange={set('telefono')} placeholder="8888-8888" required style={inputStyle} />
              </div>
            </div>

            {/* Descripción */}
            <div style={{ marginBottom: 14 }}>
              <label style={labelStyle}>Descripción</label>
              <input type="text" value={form.descripcion} onChange={set('descripcion')} placeholder="Descripción breve de la empresa" style={inputStyle} />
            </div>

            {/* Fila contraseñas */}
            <div style={rowStyle}>
              <div style={colStyle}>
                <label style={labelStyle}>Contraseña</label>
                <PasswordInput value={form.clave} onChange={set('clave')} placeholder="••••••••" />
              </div>
              <div style={colStyle}>
                <label style={labelStyle}>Confirmar contraseña</label>
                <PasswordInput value={form.claveConfirm} onChange={set('claveConfirm')} placeholder="••••••••" />
              </div>
            </div>

            {error && <div style={errorStyle}>{error}</div>}

            <button type="submit" style={submitStyle}>
              Registrar empresa
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}

const ringsProps = {
  color: '#10b981', colorTwo: '#06b6d4',
  ringCount: 6, speed: 0.9, attenuation: 8,
  lineThickness: 2.5, baseRadius: 0.28, radiusStep: 0.12,
  scaleRate: 0.12, opacity: 1, noiseAmount: 0.08,
  ringGap: 1.6, fadeIn: 0.7, fadeOut: 0.5,
  followMouse: true, mouseInfluence: 0.15,
  hoverScale: 1.1, parallax: 0.04, clickBurst: true,
}

const pageStyle = { minHeight: '100vh', background: '#040f0a', position: 'relative', overflow: 'hidden' }
const cardStyle = {
  width: '100%',
  background: 'rgba(255,255,255,.06)',
  backdropFilter: 'blur(24px)', WebkitBackdropFilter: 'blur(24px)',
  border: '1px solid rgba(255,255,255,.12)',
  borderRadius: 16, padding: '36px 36px',
  boxShadow: '0 24px 64px rgba(0,0,0,.5)',
}
const rowStyle = { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 14 }
const colStyle = {}
const labelStyle = { display: 'block', color: 'rgba(255,255,255,.6)', fontSize: '0.8rem', marginBottom: 5 }
const inputStyle = {
  width: '100%', padding: '10px 12px', boxSizing: 'border-box',
  background: 'rgba(255,255,255,.07)', border: '1px solid rgba(255,255,255,.14)',
  borderRadius: 8, color: 'white', fontSize: '0.88rem', outline: 'none',
}
const submitStyle = {
  width: '100%', padding: '12px', marginTop: 8,
  background: 'linear-gradient(135deg, #10b981, #06b6d4)',
  border: 'none', borderRadius: 8, color: 'white',
  fontWeight: 700, fontSize: '0.95rem', cursor: 'pointer',
  boxShadow: '0 4px 20px rgba(16,185,129,.35)',
}
const errorStyle = {
  background: 'rgba(220,38,38,.15)', border: '1px solid rgba(220,38,38,.3)',
  color: '#fca5a5', borderRadius: 8, padding: '9px 12px',
  fontSize: '0.83rem', marginBottom: 14, textAlign: 'center',
}
const iconBtnStyle = {
  display: 'flex', alignItems: 'center', justifyContent: 'center',
  width: 38, height: 38, borderRadius: '50%',
  background: 'rgba(255,255,255,.07)', border: '1px solid rgba(255,255,255,.14)',
  color: 'rgba(255,255,255,.6)', textDecoration: 'none',
  transition: 'background .2s, border-color .2s',
}
const backBtnStyle = {
  display: 'inline-block', padding: '10px 28px',
  background: 'linear-gradient(135deg, #10b981, #06b6d4)',
  borderRadius: 8, color: 'white', fontWeight: 700,
  textDecoration: 'none', fontSize: '0.9rem',
}
