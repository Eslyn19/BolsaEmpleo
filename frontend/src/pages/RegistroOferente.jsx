import { useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'
import MagicRings from '../components/MagicRings'
import './RegistroOferente.css'

const ringsProps = {
  color: '#f97316', 
  colorTwo: '#eab308',
  ringCount: 8, 
  speed: 0.4, 
  attenuation: 8,
  lineThickness: 2.5, 
  baseRadius: 0.28, 
  radiusStep: 0.12,
  scaleRate: 0.12, 
  opacity: 0.4, 
  noiseAmount: 0.08,
  ringGap: 1.6, 
  fadeIn: 0.7, 
  fadeOut: 0.5,
  followMouse: true, 
  mouseInfluence: 0.15,
  hoverScale: 1.1, 
  parallax: 0.04, 
  clickBurst: true,
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
    <div className="ro-input-wrap">
      <input type={show ? 'text' : 'password'} value={value} onChange={onChange}
        placeholder={placeholder} required className="ro-input ro-input--pr" />
      <button type="button" onClick={() => setShow(s => !s)} className="ro-eye-btn">
        <EyeIcon open={show} />
      </button>
    </div>
  )
}

export default function RegistroOferente() {
  const [form, setForm] = useState({ identificacion: '', nombre: '', primerAp: '', nacionalidad: '', telefono: '', correo: '', lugarResidencia: '', clave: '', claveConfirm: '' })
  const [error, setError] = useState('')
  const [ok, setOk] = useState('')

  const set = key => e => setForm(f => ({ ...f, [key]: e.target.value }))

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    try {
      const data = await api.post('/oferente/registro', form)
      setOk(data.mensaje)
    } catch (err) {
      setError(err.message)
    }
  }

  if (ok) return (
    <div className="ro-shell">
      <div className="ro-bg"><MagicRings {...ringsProps} /></div>
      <div className="ro-center">
        <div className="ro-card">
          <div className="ro-success">
            <div className="ro-success-icon">✅</div>
            <p className="ro-success-text">Registro exitoso. Espere aprobación del administrador.</p>
            <Link to="/login" className="ro-back-btn">Ir al login</Link>
          </div>
        </div>
      </div>
    </div>
  )

  return (
    <div className="ro-shell">
      <div className="ro-bg"><MagicRings {...ringsProps} /></div>

      <div className="ro-center">
        <div className="ro-card">
          <div className="ro-header">
            <Link to="/login" title="Volver al login" className="ro-icon-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
              </svg>
            </Link>
            <div className="ro-header-brand">
              <img src="/logo.png" alt="logo" className="ro-logo" />
              <span className="ro-title">Registro de Oferente</span>
            </div>
            <div className="ro-header-spacer" />
          </div>

          <form onSubmit={handleSubmit}>
            <div className="ro-row">
              <div>
                <label className="ro-label">Identificación</label>
                <input type="text" value={form.identificacion} onChange={set('identificacion')} placeholder="123456789" required className="ro-input" />
              </div>
              <div>
                <label className="ro-label">Nombre</label>
                <input type="text" value={form.nombre} onChange={set('nombre')} placeholder="Juan" required className="ro-input" />
              </div>
            </div>

            <div className="ro-row">
              <div>
                <label className="ro-label">Primer apellido</label>
                <input type="text" value={form.primerAp} onChange={set('primerAp')} placeholder="Pérez" required className="ro-input" />
              </div>
              <div>
                <label className="ro-label">Nacionalidad</label>
                <input type="text" value={form.nacionalidad} onChange={set('nacionalidad')} placeholder="Costarricense" required className="ro-input" />
              </div>
            </div>

            <div className="ro-row">
              <div>
                <label className="ro-label">Teléfono</label>
                <input type="text" value={form.telefono} onChange={set('telefono')} placeholder="8888-8888" className="ro-input" />
              </div>
              <div>
                <label className="ro-label">Correo electrónico</label>
                <input type="email" value={form.correo} onChange={set('correo')} placeholder="correo@ejemplo.com" required className="ro-input" />
              </div>
            </div>

            <div className="ro-field">
              <label className="ro-label">Lugar de residencia</label>
              <input type="text" value={form.lugarResidencia} onChange={set('lugarResidencia')} placeholder="Provincia, País" required className="ro-input" />
            </div>

            <div className="ro-row">
              <div>
                <label className="ro-label">Contraseña</label>
                <PasswordInput value={form.clave} onChange={set('clave')} placeholder="••••••••" />
              </div>
              <div>
                <label className="ro-label">Confirmar contraseña</label>
                <PasswordInput value={form.claveConfirm} onChange={set('claveConfirm')} placeholder="••••••••" />
              </div>
            </div>

            {error && <div className="ro-error">{error}</div>}

            <button type="submit" className="ro-btn-submit">Registrar oferente</button>
          </form>
        </div>
      </div>
    </div>
  )
}
