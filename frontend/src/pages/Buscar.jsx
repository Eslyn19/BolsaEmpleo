import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'
import FloatingLines from '../components/FloatingLines'
import './Buscar.css'

function AccordionGroup({ grupo, seleccionadas, onToggle }) {
  const [abierto, setAbierto] = useState(false)
  const activas = grupo.hijos.filter(h => seleccionadas.includes(h.id)).length

  return (
    <div className="bs-accordion">
      <button onClick={() => setAbierto(a => !a)} className="bs-accordion-trigger">
        <span>{grupo.padre.nombre}</span>
        <span className="bs-accordion-right">
          {activas > 0 && <span className="bs-accordion-count">{activas}</span>}
          <span className={`bs-chevron${abierto ? ' bs-chevron--open' : ''}`}>▾</span>
        </span>
      </button>

      {abierto && (
        <div className="bs-accordion-body">
          {grupo.hijos.map(h => (
            <label key={h.id} className={`bs-filter-label${seleccionadas.includes(h.id) ? ' bs-filter-label--checked' : ''}`}>
              <input type="checkbox" checked={seleccionadas.includes(h.id)} onChange={() => onToggle(h.id)}
                className="bs-filter-checkbox" />
              {h.nombre}
            </label>
          ))}
        </div>
      )}
    </div>
  )
}

export default function Buscar() {
  const [data, setData]               = useState(null)
  const [seleccionadas, setSeleccionadas] = useState([])

  function cargar(ids) {
    const query = ids.length ? '?' + ids.map(id => `caracteristicas=${id}`).join('&') : ''
    api.get(`/buscar${query}`).then(setData).catch(() => {})
  }

  useEffect(() => { cargar([]) }, [])

  function toggleCar(id) {
    const next = seleccionadas.includes(id) ? seleccionadas.filter(x => x !== id) : [...seleccionadas, id]
    setSeleccionadas(next)
    cargar(next)
  }

  function limpiar() { setSeleccionadas([]); cargar([]) }

  return (
    <div className="dark-page">
      <div className="bs-bg">
        <FloatingLines
          enabledWaves={['top', 'middle', 'bottom']}
          lineCount={8} lineDistance={8} bendRadius={8} bendStrength={-2}
          interactive parallax animationSpeed={1}
          linesGradient={['#e945f5', '#7c3aed', '#2563eb', '#f97316', '#22c55e']}
        />
      </div>
      <div className="bs-overlay" />

      <div className="bs-content">
        <nav className="navbar bs-nav">
          <Link to="/" className="navbar-brand bs-nav-brand">
            <img src="/logo.png" alt="Job Connect" className="bs-nav-logo" />
            <span>Job Connect</span>
          </Link>
          <div className="navbar-links">
            <Link to="/">Inicio</Link>
            <Link to="/login">Ingresar</Link>
          </div>
        </nav>

        <div className="page">
          <h2 className="bs-title">Buscar puestos</h2>

          {data ? (
            <div className="bs-layout">
              <aside className="bs-sidebar">
                <div className="bs-sidebar-header">
                  <h3 className="bs-sidebar-label">Filtrar</h3>
                  {seleccionadas.length > 0 && (
                    <button onClick={limpiar} className="bs-clear-btn">
                      Limpiar ({seleccionadas.length})
                    </button>
                  )}
                </div>
                {data.arbol.map(grupo => (
                  <AccordionGroup key={grupo.padre.id} grupo={grupo} seleccionadas={seleccionadas} onToggle={toggleCar} />
                ))}
              </aside>

              <main className="bs-main">
                <p className="bs-results-count">
                  {data.puestos.length} resultado{data.puestos.length !== 1 ? 's' : ''}
                  {seleccionadas.length > 0 && ` para ${seleccionadas.length} filtro${seleccionadas.length !== 1 ? 's' : ''}`}
                </p>

                {data.puestos.length === 0 ? (
                  <div className="bs-empty">
                    <p className="bs-empty-text">No hay puestos que coincidan con los filtros seleccionados.</p>
                    <button onClick={limpiar} className="bs-empty-btn">Ver todos</button>
                  </div>
                ) : (
                  <div className="grid">
                    {data.puestos.map(p => (
                      <div key={p.id} className="bs-card">
                        <div className="bs-card-title">{p.descripcion}</div>
                        <p className="bs-card-empresa">
                          <strong>Empresa:</strong> {p.idUsuario?.nombre}
                        </p>
                        <div className="bs-card-badges">
                          <span className="bs-badge-salary">₡{p.salario?.toLocaleString()}</span>
                          {p.caracteristicas?.map(c => (
                            <span key={c.id} className="bs-badge-char">{c.nombre}</span>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </main>
            </div>
          ) : (
            <p className="bs-loading">Cargando...</p>
          )}
        </div>
      </div>
    </div>
  )
}
