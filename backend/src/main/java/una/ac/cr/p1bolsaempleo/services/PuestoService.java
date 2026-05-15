package una.ac.cr.p1bolsaempleo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.CaracteristicaRepository;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import una.ac.cr.p1bolsaempleo.data.OferenteHabilidadRepository;
import una.ac.cr.p1bolsaempleo.data.OferenteRepository;
import una.ac.cr.p1bolsaempleo.data.PuestoRepository;
import una.ac.cr.p1bolsaempleo.dto.CandidatoPuestoDto;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Empresa;
import una.ac.cr.p1bolsaempleo.models.Oferente;
import una.ac.cr.p1bolsaempleo.models.Oferentehabilidad;
import una.ac.cr.p1bolsaempleo.models.Puesto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PuestoService {

    private static final Logger log = LoggerFactory.getLogger(PuestoService.class);

    private final PuestoRepository puestoRepository;
    private final EmpresaRepository empresaRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final OferenteRepository oferenteRepository;
    private final OferenteHabilidadRepository oferenteHabilidadRepository;

    public PuestoService(PuestoRepository puestoRepository, EmpresaRepository empresaRepository,
                         CaracteristicaRepository caracteristicaRepository,
                         OferenteRepository oferenteRepository,
                         OferenteHabilidadRepository oferenteHabilidadRepository) {
        this.puestoRepository = puestoRepository;
        this.empresaRepository = empresaRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.oferenteRepository = oferenteRepository;
        this.oferenteHabilidadRepository = oferenteHabilidadRepository;
    }

    public static final byte ACCESO_PRIVADO = 0;
    public static final byte ACCESO_PUBLICO = 1;

    public List<Puesto> listarPorEmpresa(String idEmpresa) {
        return puestoRepository.findByIdUsuario_IdUsuarioOrderByIdDesc(idEmpresa);
    }

    /* Hasta 5 puestos públicos más relevantes para la página de inicio. */
    public List<Puesto> listarCincoPublicosParaInicio() {
        return puestoRepository.findPublicosParaInicio(PageRequest.of(0, 5));
    }

    /* Solo puestos abiertos a candidatos: activos y sin oferente asignado. */
    public List<Puesto> listarAbiertosParaBuscarCandidatos(String idEmpresa) {
        return puestoRepository.findAbiertosSinAsignarPorEmpresa(idEmpresa);
    }

    public Optional<Puesto> obtenerPuestoEmpresaParaCandidatos(Integer idPuesto, String idEmpresa) {
        return puestoRepository.findByIdAndEmpresaWithCaracteristicas(idPuesto, idEmpresa);
    }

    /*
      Todos los oferentes aprobados, con comparativa n requisitos del puesto vs habilidades del oferente.
      Orden: más coincidencias, luego mayor suma de nivel en requisitos cubiertos, luego más suma de nivel total.
     */
    public List<CandidatoPuestoDto> listarCandidatosCompatibles(Puesto puesto) {
        List<Caracteristica> listaReq = puesto.getCaracteristicas().stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toList());
        int totalRequeridas = listaReq.size();

        List<Oferente> aprobados = oferenteRepository.findAllAprobados();
        List<CandidatoPuestoDto> salida = new ArrayList<>();
        for (Oferente o : aprobados) {
            List<Oferentehabilidad> habs = oferenteHabilidadRepository.findByOferente(o.getIdUsuario());
            Map<Integer, Integer> nivelPorCaracteristica = new HashMap<>();
            int sumaNivelTodas = 0;
            for (Oferentehabilidad oh : habs) {
                Integer idCar = oh.getIdCaracteristica().getId();
                if (idCar != null) {
                    nivelPorCaracteristica.put(idCar, oh.getNivel());
                    sumaNivelTodas += oh.getNivel() != null ? oh.getNivel() : 0;
                }
            }

            List<String> cubiertos = new ArrayList<>();
            List<String> faltantes = new ArrayList<>();
            int coincidencias = 0;
            int sumaNivelEnCubiertos = 0;
            for (Caracteristica req : listaReq) {
                Integer id = req.getId();
                if (nivelPorCaracteristica.containsKey(id)) {
                    coincidencias++;
                    sumaNivelEnCubiertos += nivelPorCaracteristica.getOrDefault(id, 0);
                    cubiertos.add(req.getNombre());
                } else {
                    faltantes.add(req.getNombre());
                }
            }

            int porcentaje;
            if (totalRequeridas == 0) {
                porcentaje = 100;
            } else {
                porcentaje = (100 * coincidencias) / totalRequeridas;
            }
            boolean encajeTotal = totalRequeridas == 0 || coincidencias == totalRequeridas;

            List<String> etiquetas = habs.stream()
                    .sorted((a, b) -> a.getIdCaracteristica().getNombre().compareToIgnoreCase(b.getIdCaracteristica().getNombre()))
                    .map(oh -> oh.getIdCaracteristica().getNombre() + " (nivel " + oh.getNivel() + ")")
                    .collect(Collectors.toList());

            salida.add(new CandidatoPuestoDto(
                    o.getIdUsuario(),
                    o.getNombre(),
                    o.getApellido(),
                    o.getCorreo(),
                    o.getTelefono(),
                    o.getNacionalidad(),
                    o.getResidencia(),
                    o.getRutaCV(),
                    etiquetas,
                    coincidencias,
                    totalRequeridas,
                    porcentaje,
                    sumaNivelEnCubiertos,
                    sumaNivelTodas,
                    List.copyOf(cubiertos),
                    List.copyOf(faltantes),
                    encajeTotal
            ));
        }

        salida.sort(Comparator
                .comparingInt(CandidatoPuestoDto::coincidencias).reversed()
                .thenComparingInt(CandidatoPuestoDto::sumaNivelEnRequisitosCubiertos).reversed()
                .thenComparingInt(CandidatoPuestoDto::sumaNivelTodasLasHabilidades).reversed()
                .thenComparing(CandidatoPuestoDto::nombre, String.CASE_INSENSITIVE_ORDER));

        return salida;
    }

    @Transactional
    public void asignarOferenteYCerrarPuesto(String idEmpresa, Integer idPuesto, String idOferente) {
        Puesto p = puestoRepository.findByIdAndEmpresaWithCaracteristicas(idPuesto, idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));
        if (p.getOferenteAsignado() != null) {
            throw new IllegalStateException("El puesto ya tiene candidato asignado");
        }
        if (p.getActivo() == null || p.getActivo() != 1) {
            throw new IllegalStateException("El puesto no está activo");
        }
        Oferente o = oferenteRepository.findByIdWithEstado(idOferente)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));
        String est = o.getEstado() != null ? o.getEstado().getNombre() : "";
        if (!"APROBADO".equalsIgnoreCase(est)) {
            throw new IllegalStateException("El oferente no está aprobado");
        }
        if (!oferenteCumpleCaracteristicas(idOferente, p)) {
            throw new IllegalStateException("El oferente no cumple las habilidades del puesto");
        }
        p.setOferenteAsignado(o);
        p.setActivo((byte) 0);
        puestoRepository.save(p);
    }

    private boolean oferenteCumpleCaracteristicas(String idOferente, Puesto puesto) {
        Set<Integer> requeridas = puesto.getCaracteristicas().stream()
                .map(Caracteristica::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (requeridas.isEmpty()) {
            return true;
        }
        List<Oferentehabilidad> habs = oferenteHabilidadRepository.findByOferente(idOferente);
        Set<Integer> tiene = habs.stream()
                .map(oh -> oh.getIdCaracteristica().getId())
                .collect(Collectors.toSet());
        return tiene.containsAll(requeridas);
    }

    @Transactional
    public void crear(String idEmpresa, String descripcion, Double salario, List<Integer> idsCaracteristicas) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        Puesto p = new Puesto();
        p.setIdUsuario(empresa);
        p.setDescripcion(descripcion.trim());
        p.setSalario(salario);
        p.setActivo((byte) 1);
        p.setAcceso(ACCESO_PRIVADO);
        /* Sin características aún: Hibernate necesita idPuesto generado antes de insertar en puesto_caracteristica */
        p.setCaracteristicas(new LinkedHashSet<>());
        p = puestoRepository.saveAndFlush(p);

        if (idsCaracteristicas != null && !idsCaracteristicas.isEmpty()) {
            List<Integer> ids = idsCaracteristicas.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            log.info("Asociando características al puesto id={}: {}", p.getId(), ids);
            List<Caracteristica> cargadas = caracteristicaRepository.findAllById(ids);
            /* Recargar entidad gestionada: así Hibernate persiste bien filas en puesto_caracteristica */
            Puesto managed = puestoRepository.findWithCaracteristicasById(p.getId()).orElseThrow();
            for (Caracteristica car : cargadas) {
                boolean usable = car.getActivo() == null || car.getActivo() == 1;
                if (usable) {
                    managed.getCaracteristicas().add(car);
                }
            }
            puestoRepository.saveAndFlush(managed);
        }
    }

    @Transactional
    public void desactivar(String idEmpresa, Integer idPuesto) {
        Puesto p = puestoRepository.findByIdAndIdUsuario_IdUsuario(idPuesto, idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));
        p.setActivo((byte) 0);
        puestoRepository.save(p);
    }

    public List<Puesto> buscarPublicosPorCaracteristicas(List<Integer> caracteristicas) {
        // todos los publicos activos
        if (caracteristicas == null || caracteristicas.isEmpty()) {
            return puestoRepository.buscarTodosPublicosActivos();
        }
        // publicos activos por caracteristicas
        return puestoRepository.buscarPublicosPorCaracteristicas(caracteristicas);
    }

    @Transactional
    public void activar(String idEmpresa, Integer idPuesto) {
        Puesto p = puestoRepository.findByIdAndIdUsuario_IdUsuario(idPuesto, idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));
        if (p.getOferenteAsignado() != null) {
            throw new IllegalStateException("No se puede reactivar un puesto que ya tiene candidato asignado");
        }
        p.setActivo((byte) 1);
        puestoRepository.save(p);
    }

    @Transactional
    public void marcarAccesoPublico(String idEmpresa, Integer idPuesto) {
        Puesto p = puestoRepository.findByIdAndIdUsuario_IdUsuario(idPuesto, idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));
        p.setAcceso(ACCESO_PUBLICO);
        puestoRepository.save(p);
    }

    @Transactional
    public void marcarAccesoPrivado(String idEmpresa, Integer idPuesto) {
        Puesto p = puestoRepository.findByIdAndIdUsuario_IdUsuario(idPuesto, idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));
        p.setAcceso(ACCESO_PRIVADO);
        puestoRepository.save(p);
    }
}
