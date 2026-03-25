package una.ac.cr.p1bolsaempleo.dto;

import java.util.List;

/**
 * Comparativa oferente vs requisitos del puesto. Orden sugerido: más coincidencias,
 * luego más suma de nivel en requisitos cubiertos, luego más experiencia total (suma niveles).
 */
public record CandidatoPuestoDto(
        String idUsuario,
        String nombre,
        String apellido,
        String correo,
        String telefono,
        String nacionalidad,
        String residencia,
        String rutaCV,
        List<String> habilidadesEtiqueta,
        int coincidencias,
        int totalRequeridas,
        int porcentajeEncaje,
        int sumaNivelEnRequisitosCubiertos,
        int sumaNivelTodasLasHabilidades,
        List<String> requisitosCubiertos,
        List<String> requisitosFaltantes,
        boolean encajeTotal
) {}
