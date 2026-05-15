import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../services/api'
import FloatingLines from '../components/FloatingLines'

function AccordionGroup({ grupo, seleccionadas, onToggle }) {
  const [abierto, setAbierto] = useState(false)
  const activas = grupo.hijos.filter(h => seleccionadas.includes(h.id)).length

  return (
    <div style={{ marginBottom: 4 }}>
      <button
        onClick={() => setAbierto(a => !a)}
        style={{
          width: '100%', textAlign: 'left', background: 'none', border: 'none',
          padding: '8px 4px', cursor: 'pointer', display: 'flex',
          justifyContent: 'space-between', alignItems: 'center',
          fontWeight: 600, fontSize: '0.875rem', color: 'rgba(255,255,255,.8)',
          borderBottom: '1px solid rgba(255,255,255,.1)',
        }}
      >
        <span>{grupo.padre.nombre}</span>
        <span style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          {activas > 0 && (
            <span style={{
              padding: '1px 8px', borderRadius: 999, fontSize: '0.7rem', fontWeight: 700,
              background: 'rgba(37,99,235,.35)', color: '#93c5fd', border: '1px solid rgba(37,99,235,.4)',
            }}>
              {activas}
            </span>
          )}
          <span style={{
            fontSize: '0.75rem', color: 'rgba(255,255,255,.4)', transition: 'transform .2s',
            transform: abierto ? 'rotate(180deg)' : 'rotate(0deg)', display: 'inline-block',
          }}>
            ▾
          </span>
        </span>
      </button>

      {abierto && (
        <div style={{ paddingTop: 4, paddingBottom: 8 }}>
          {grupo.hijos.map(h => (
            <label key={h.id} style={{
              display: 'flex', alignItems: 'center', gap: 8,
              padding: '5px 4px', fontSize: '0.875rem', cursor: 'pointer',
              color: seleccionadas.includes(h.id) ? '#93c5fd' : 'rgba(255,255,255,.55)',
              fontWeight: seleccionadas.includes(h.id) ? 600 : 400,
            }}>
              <input
                type="checkbox"
                checked={seleccionadas.includes(h.id)}
                onChange={() => onToggle(h.id)}
                style={{ accentColor: '#2563eb', width: 15, height: 15 }}
              />
              {h.nombre}
            </label>
          ))}
        </div>
      )}
    </div>
  )
}

export default function Buscar() {
  const [data, setData] = useState(null)
  const [seleccionadas, setSeleccionadas] = useState([])

  function cargar(ids) {
    const query = ids.length ? '?' + ids.map(id => `caracteristicas=${id}`).join('&') : ''
    api.get(`/buscar${query}`).then(setData).catch(() => {})
  }

  useEffect(() => { cargar([]) }, [])

  function toggleCar(id) {
    const next = seleccionadas.includes(id)
      ? seleccionadas.filter(x => x !== id)
      : [...seleccionadas, id]
    setSeleccionadas(next)
    cargar(next)
  }

  function limpiar() {
    setSeleccionadas([])
    cargar([])
  }

  return (
    <div className="dark-page">
      {/* FloatingLines — mismo fondo que Inicio */}
      <div style={{ position: 'fixed', inset: 0, zIndex: 0 }}>
        <FloatingLines
          enabledWaves={['top', 'middle', 'bottom']}
          lineCount={8}
          lineDistance={8}
          bendRadius={8}
          bendStrength={-2}
          interactive
          parallax={true}
          animationSpeed={1}
          linesGradient={['#e945f5', '#7c3aed', '#2563eb', '#f97316', '#22c55e']}
        />
      </div>

      {/* Overlay */}
      <div style={{ position: 'fixed', inset: 0, zIndex: 1, background: 'rgba(8,11,26,.45)', pointerEvents: 'none' }} />

      {/* Contenido */}
      <div style={{ position: 'relative', zIndex: 2 }}>

        <nav className="navbar" style={{ background: 'rgba(17,29,74,.75)', backdropFilter: 'blur(16px)', WebkitBackdropFilter: 'blur(16px)' }}>
          <Link to="/" className="navbar-brand" style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <img src="/logo.png" alt="Job Connect" style={{ height: 34, width: 'auto' }} />
            <span>Job Connect</span>
          </Link>
          <div className="navbar-links">
            <Link to="/">Inicio</Link>
            <Link to="/login">Ingresar</Link>
          </div>
        </nav>

        <div className="page">
          <h2 style={{ color: 'white', marginBottom: 24 }}>Buscar puestos</h2>

          {data ? (
            <div style={{ display: 'flex', gap: 24, alignItems: 'flex-start' }}>

              {/* Sidebar de filtros */}
              <aside style={{
                minWidth: 220,
                background: 'rgba(0,0,0,.6)',
                border: '1px solid rgba(255,255,255,.1)',
                borderRadius: 10,
                padding: 20,
                boxShadow: '0 4px 20px rgba(0,0,0,.4)',
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
                  <h3 style={{ margin: 0, color: 'rgba(255,255,255,.5)', fontSize: '0.75rem', letterSpacing: 2, textTransform: 'uppercase' }}>
                    Filtrar
                  </h3>
                  {seleccionadas.length > 0 && (
                    <button onClick={limpiar} style={{
                      background: 'none', border: 'none', color: '#93c5fd',
                      fontSize: '0.78rem', cursor: 'pointer', padding: 0, fontWeight: 600,
                    }}>
                      Limpiar ({seleccionadas.length})
                    </button>
                  )}
                </div>

                {data.arbol.map(grupo => (
                  <AccordionGroup
                    key={grupo.padre.id}
                    grupo={grupo}
                    seleccionadas={seleccionadas}
                    onToggle={toggleCar}
                  />
                ))}
              </aside>

              {/* Resultados */}
              <main style={{ flex: 1 }}>
                <p style={{ marginBottom: 16, color: 'rgba(255,255,255,.45)', fontSize: '0.875rem' }}>
                  {data.puestos.length} resultado{data.puestos.length !== 1 ? 's' : ''}
                  {seleccionadas.length > 0 && ` para ${seleccionadas.length} filtro${seleccionadas.length !== 1 ? 's' : ''}`}
                </p>

                {data.puestos.length === 0 ? (
                  <div style={{
                    background: 'rgba(0,0,0,.6)', border: '1px solid rgba(255,255,255,.1)',
                    borderRadius: 10, padding: 40, textAlign: 'center',
                  }}>
                    <p style={{ fontSize: '1rem', color: 'rgba(255,255,255,.5)' }}>
                      No hay puestos que coincidan con los filtros seleccionados.
                    </p>
                    <button onClick={limpiar} style={{
                      marginTop: 12, background: 'transparent', border: '1px solid rgba(255,255,255,.2)',
                      borderRadius: 8, padding: '8px 20px', color: 'rgba(255,255,255,.75)',
                      fontSize: '0.875rem', cursor: 'pointer',
                    }}>
                      Ver todos
                    </button>
                  </div>
                ) : (
                  <div className="grid">
                    {data.puestos.map(p => (
                      <div key={p.id} style={{
                        background: 'rgba(0,0,0,.6)', border: '1px solid rgba(255,255,255,.1)',
                        borderRadius: 10, padding: 20,
                        boxShadow: '0 2px 12px rgba(0,0,0,.35)',
                        transition: 'background .2s, border-color .2s',
                      }}
                        onMouseEnter={e => { e.currentTarget.style.background = 'rgba(0,0,0,.78)'; e.currentTarget.style.borderColor = 'rgba(255,255,255,.18)' }}
                        onMouseLeave={e => { e.currentTarget.style.background = 'rgba(0,0,0,.6)';   e.currentTarget.style.borderColor = 'rgba(255,255,255,.1)' }}
                      >
                        <div style={{ fontWeight: 700, fontSize: '1rem', color: 'white', marginBottom: 6 }}>
                          {p.descripcion}
                        </div>
                        <p style={{ color: 'rgba(255,255,255,.5)', fontSize: '0.875rem', margin: '0 0 10px' }}>
                          <strong style={{ color: 'rgba(255,255,255,.65)' }}>Empresa:</strong> {p.idUsuario?.nombre}
                        </p>
                        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', alignItems: 'center' }}>
                          <span style={{
                            padding: '3px 10px', borderRadius: 999, fontSize: '0.78rem', fontWeight: 600,
                            background: 'rgba(34,197,94,.2)', color: '#4ade80', border: '1px solid rgba(34,197,94,.3)',
                          }}>
                            ₡{p.salario?.toLocaleString()}
                          </span>
                          {p.caracteristicas?.map(c => (
                            <span key={c.id} style={{
                              padding: '3px 10px', borderRadius: 999, fontSize: '0.78rem', fontWeight: 500,
                              background: 'rgba(37,99,235,.2)', color: '#93c5fd', border: '1px solid rgba(37,99,235,.3)',
                            }}>
                              {c.nombre}
                            </span>
                          ))}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </main>
            </div>
          ) : (
            <p style={{ color: 'rgba(255,255,255,.5)' }}>Cargando...</p>
          )}
        </div>
      </div>
    </div>
  )
}
