import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api } from '../services/api'
import MagicRings from '../components/MagicRings'
import './Login.css'

export default function Login() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError]   = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const data = await api.login(form.username, form.password)
      localStorage.setItem('token',    data.token)
      localStorage.setItem('role',     data.role)
      localStorage.setItem('username', data.username)
      if (data.role === 'ROLE_ADMIN')        navigate('/admin')
      else if (data.role === 'ROLE_EMPRESA') navigate('/empresa')
      else                                   navigate('/oferente')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="lg-shell">
      <div className="lg-bg">
        <MagicRings
          color="#e945f5" colorTwo="#2563eb"
          ringCount={6} speed={0.9} attenuation={8}
          lineThickness={2.5} baseRadius={0.28} radiusStep={0.12}
          scaleRate={0.12} opacity={1} noiseAmount={0.08}
          ringGap={1.6} fadeIn={0.7} fadeOut={0.5}
          followMouse mouseInfluence={0.15}
          hoverScale={1.1} parallax={0.04} clickBurst
        />
      </div>

      <div className="lg-center">
        <div className="lg-card">
          <img src="/logo.png" alt="Job Connect" className="lg-logo" />
          <h2 className="lg-title">Iniciar sesión</h2>

          <form onSubmit={handleSubmit}>
            <div className="lg-field">
              <label className="lg-label">Usuario</label>
              <input type="text" value={form.username} placeholder="Correo o identificación" required
                className="lg-input"
                onChange={e => setForm({ ...form, username: e.target.value })} />
            </div>

            <div className="lg-field--last">
              <label className="lg-label">Contraseña</label>
              <input type="password" value={form.password} placeholder="••••••••" required
                className="lg-input"
                onChange={e => setForm({ ...form, password: e.target.value })} />
            </div>

            {error && <div className="lg-error">{error}</div>}

            <button type="submit" disabled={loading} className="lg-btn-submit">
              {loading ? 'Ingresando...' : 'Ingresar'}
            </button>
          </form>

          <div className="lg-footer">
            <p className="lg-footer-text">¿No tienes cuenta?</p>
            <div className="lg-register-links">
              <Link to="/registro/empresa" className="lg-register-btn">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 3H8a2 2 0 0 0-2 2v2h12V5a2 2 0 0 0-2-2z"/>
                </svg>
                Empresa
              </Link>
              <Link to="/registro/oferente" className="lg-register-btn">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                </svg>
                Oferente
              </Link>
            </div>
          </div>

          <div className="lg-home-wrap">
            <Link to="/" title="Volver al inicio" className="lg-home-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M3 9.5L12 3l9 6.5V20a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
            </Link>
          </div>
        </div>
      </div>
    </div>
  )
}
