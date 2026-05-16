import { useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'
import MagicRings from '../components/MagicRings'
import './RegistroEmpresa.css'

const ringsProps = {
  color: '#10b981', colorTwo: '#06b6d4',
  ringCount: 6, speed: 0.9, attenuation: 8,
  lineThickness: 2.5, baseRadius: 0.28, radiusStep: 0.12,
  scaleRate: 0.12, opacity: 1, noiseAmount: 0.08,
  ringGap: 1.6, fadeIn: 0.7, fadeOut: 0.5,
  followMouse: true, mouseInfluence: 0.15,
  hoverScale: 1.1, parallax: 0.04, clickBurst: true,
}

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
    <div className="re-input-wrap">
      <input type={show ? 'text' : 'password'} value={value} onChange={onChange}
        placeholder={placeholder} required className="re-input re-input--pr" />
      <button type="button" onClick={() => setShow(s => !s)} className="re-eye-btn">
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
    <div className="re-shell">
      <div className="re-bg"><MagicRings {...ringsProps} /></div>
      <div className="re-center">
        <div className="re-card">
          <div className="re-success">
            <div className="re-success-icon">✅</div>
            <p className="re-success-text">Registro exitoso. Espere aprobación del administrador.</p>
            <Link to="/login" className="re-back-btn">Ir al login</Link>
          </div>
        </div>
      </div>
    </div>
  )

  return (
    <div className="re-shell">
      <div className="re-bg"><MagicRings {...ringsProps} /></div>

      <div className="re-center">
        <div className="re-card">
          <div className="re-header">
            <Link to="/login" title="Volver al login" className="re-icon-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
              </svg>
            </Link>
            <div className="re-header-brand">
              <img src="/logo.png" alt="logo" className="re-logo" />
              <span className="re-title">Registro de Empresa</span>
            </div>
            <div className="re-header-spacer" />
          </div>

          <form onSubmit={handleSubmit}>
            <div className="re-row">
              <div>
                <label className="re-label">Correo electrónico</label>
                <input type="email" value={form.correo} onChange={set('correo')} placeholder="empresa@correo.com" required className="re-input" />
              </div>
              <div>
                <label className="re-label">Nombre de la empresa</label>
                <input type="text" value={form.nombre} onChange={set('nombre')} placeholder="Mi Empresa S.A." required className="re-input" />
              </div>
            </div>

            <div className="re-row">
              <div>
                <label className="re-label">Ubicación</label>
                <input type="text" value={form.ubicacion} onChange={set('ubicacion')} placeholder="Provincia, País" required className="re-input" />
              </div>
              <div>
                <label className="re-label">Teléfono</label>
                <input type="text" value={form.telefono} onChange={set('telefono')} placeholder="8888-8888" required className="re-input" />
              </div>
            </div>

            <div className="re-field">
              <label className="re-label">Descripción</label>
              <input type="text" value={form.descripcion} onChange={set('descripcion')} placeholder="Descripción breve de la empresa" className="re-input" />
            </div>

            <div className="re-row">
              <div>
                <label className="re-label">Contraseña</label>
                <PasswordInput value={form.clave} onChange={set('clave')} placeholder="••••••••" />
              </div>
              <div>
                <label className="re-label">Confirmar contraseña</label>
                <PasswordInput value={form.claveConfirm} onChange={set('claveConfirm')} placeholder="••••••••" />
              </div>
            </div>

            {error && <div className="re-error">{error}</div>}

            <button type="submit" className="re-btn-submit">Registrar empresa</button>
          </form>
        </div>
      </div>
    </div>
  )
}
