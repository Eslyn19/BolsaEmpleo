import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api } from '../services/api'
import MagicRings from '../components/MagicRings'

export default function Login() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const data = await api.login(form.username, form.password)
      localStorage.setItem('token', data.token)
      localStorage.setItem('role', data.role)
      localStorage.setItem('username', data.username)
      if (data.role === 'ROLE_ADMIN') navigate('/admin')
      else if (data.role === 'ROLE_EMPRESA') navigate('/empresa')
      else navigate('/oferente')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ minHeight: '100vh', background: '#060912', position: 'relative', overflow: 'hidden' }}>

      {/* MagicRings como fondo completo */}
      <div style={{ position: 'absolute', inset: 0, zIndex: 0 }}>
        <MagicRings
          color="#e945f5"
          colorTwo="#2563eb"
          ringCount={6}
          speed={0.9}
          attenuation={8}
          lineThickness={2.5}
          baseRadius={0.28}
          radiusStep={0.12}
          scaleRate={0.12}
          opacity={1}
          noiseAmount={0.08}
          ringGap={1.6}
          fadeIn={0.7}
          fadeOut={0.5}
          followMouse={true}
          mouseInfluence={0.15}
          hoverScale={1.1}
          parallax={0.04}
          clickBurst={true}
        />
      </div>

      {/* Formulario centrado */}
      <div style={{
        position: 'relative', zIndex: 1,
        minHeight: '100vh',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        padding: 24,
      }}>
        <div style={{
          width: '100%', maxWidth: 380,
          background: 'rgba(255,255,255,.06)',
          backdropFilter: 'blur(24px)',
          WebkitBackdropFilter: 'blur(24px)',
          border: '1px solid rgba(255,255,255,.12)',
          borderRadius: 16,
          padding: '40px 36px',
          boxShadow: '0 24px 64px rgba(0,0,0,.5)',
        }}>
          <img src="/logo.png" alt="Job Connect"
            style={{ height: 48, display: 'block', margin: '0 auto 20px', filter: 'drop-shadow(0 2px 12px rgba(233,69,245,.5))' }} />

          <h2 style={{ color: 'white', textAlign: 'center', fontWeight: 700, fontSize: '1.4rem', marginBottom: 28 }}>
            Iniciar sesión
          </h2>

          <form onSubmit={handleSubmit}>
            <div style={{ marginBottom: 14 }}>
              <label style={{ display: 'block', color: 'rgba(255,255,255,.65)', fontSize: '0.83rem', marginBottom: 5 }}>
                Usuario
              </label>
              <input type="text" value={form.username} placeholder="Correo o identificación" required
                onChange={e => setForm({ ...form, username: e.target.value })}
                style={{
                  width: '100%', padding: '10px 12px', boxSizing: 'border-box',
                  background: 'rgba(255,255,255,.07)', border: '1px solid rgba(255,255,255,.14)',
                  borderRadius: 8, color: 'white', fontSize: '0.9rem', outline: 'none',
                }} />
            </div>

            <div style={{ marginBottom: 20 }}>
              <label style={{ display: 'block', color: 'rgba(255,255,255,.65)', fontSize: '0.83rem', marginBottom: 5 }}>
                Contraseña
              </label>
              <input type="password" value={form.password} placeholder="••••••••" required
                onChange={e => setForm({ ...form, password: e.target.value })}
                style={{
                  width: '100%', padding: '10px 12px', boxSizing: 'border-box',
                  background: 'rgba(255,255,255,.07)', border: '1px solid rgba(255,255,255,.14)',
                  borderRadius: 8, color: 'white', fontSize: '0.9rem', outline: 'none',
                }} />
            </div>

            {error && (
              <div style={{
                background: 'rgba(220,38,38,.15)', border: '1px solid rgba(220,38,38,.3)',
                color: '#fca5a5', borderRadius: 8, padding: '9px 12px',
                fontSize: '0.83rem', marginBottom: 16, textAlign: 'center',
              }}>{error}</div>
            )}

            <button type="submit" disabled={loading} style={{
              width: '100%', padding: '11px',
              background: 'linear-gradient(135deg, #e945f5, #7c3aed, #2563eb)',
              border: 'none', borderRadius: 8, color: 'white',
              fontWeight: 700, fontSize: '0.95rem', cursor: 'pointer',
              boxShadow: '0 4px 20px rgba(233,69,245,.3)',
              opacity: loading ? .6 : 1,
            }}>
              {loading ? 'Ingresando...' : 'Ingresar'}
            </button>
          </form>

          <div style={{ marginTop: 24, borderTop: '1px solid rgba(255,255,255,.08)', paddingTop: 20 }}>
            <p style={{ color: 'rgba(255,255,255,.35)', fontSize: '0.78rem', textAlign: 'center', marginBottom: 12 }}>
              ¿No tienes cuenta?
            </p>
            <div style={{ display: 'flex', gap: 10 }}>
              <Link to="/registro/empresa" style={btnRegisterStyle}>
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 3H8a2 2 0 0 0-2 2v2h12V5a2 2 0 0 0-2-2z"/>
                </svg>
                Empresa
              </Link>
              <Link to="/registro/oferente" style={btnRegisterStyle}>
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                </svg>
                Oferente
              </Link>
            </div>
          </div>

          {/* Botón home */}
          <div style={{ marginTop: 20, display: 'flex', justifyContent: 'center' }}>
            <Link to="/" title="Volver al inicio" style={{
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              width: 40, height: 40, borderRadius: '50%',
              background: 'rgba(255,255,255,.06)',
              border: '1px solid rgba(255,255,255,.12)',
              color: 'rgba(255,255,255,.45)',
              transition: 'background .2s, color .2s, border-color .2s',
              textDecoration: 'none',
            }}
              onMouseEnter={e => { e.currentTarget.style.background = 'rgba(233,69,245,.2)'; e.currentTarget.style.color = 'white'; e.currentTarget.style.borderColor = 'rgba(233,69,245,.5)' }}
              onMouseLeave={e => { e.currentTarget.style.background = 'rgba(255,255,255,.06)'; e.currentTarget.style.color = 'rgba(255,255,255,.45)'; e.currentTarget.style.borderColor = 'rgba(255,255,255,.12)' }}
            >
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

const btnRegisterStyle = {
  flex: 1,
  display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 7,
  padding: '10px 12px',
  background: 'rgba(255,255,255,.07)',
  border: '1px solid rgba(255,255,255,.14)',
  borderRadius: 9,
  color: 'rgba(255,255,255,.75)',
  fontSize: '0.85rem',
  fontWeight: 600,
  textDecoration: 'none',
  transition: 'background .2s, border-color .2s, color .2s',
}
