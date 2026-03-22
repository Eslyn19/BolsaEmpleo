package una.ac.cr.p1bolsaempleo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import una.ac.cr.p1bolsaempleo.data.CaracteristicaRepository;
import una.ac.cr.p1bolsaempleo.data.EmpresaRepository;
import una.ac.cr.p1bolsaempleo.data.PuestoRepository;
import una.ac.cr.p1bolsaempleo.models.Caracteristica;
import una.ac.cr.p1bolsaempleo.models.Empresa;
import una.ac.cr.p1bolsaempleo.models.Puesto;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PuestoService {

    private static final Logger log = LoggerFactory.getLogger(PuestoService.class);

    private final PuestoRepository puestoRepository;
    private final EmpresaRepository empresaRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    public PuestoService(PuestoRepository puestoRepository, EmpresaRepository empresaRepository,
                         CaracteristicaRepository caracteristicaRepository) {
        this.puestoRepository = puestoRepository;
        this.empresaRepository = empresaRepository;
        this.caracteristicaRepository = caracteristicaRepository;
    }

    public List<Puesto> listarPorEmpresa(String idEmpresa) {
        return puestoRepository.findByIdUsuario_IdUsuarioOrderByIdDesc(idEmpresa);
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
}
