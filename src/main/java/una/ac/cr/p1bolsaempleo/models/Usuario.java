package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Alineado con script.sql: tabla Usuario, columnas idUsuario, clave, rol.
 */
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "idUsuario", nullable = false, length = 9)
    private String idUsuario;

    @Column(name = "clave", nullable = false, length = 300)
    private String clave;

    @Column(name = "rol", nullable = false, columnDefinition = "ENUM('ADMIN','OFERENTE','EMPRESA')")
    private String rol;
}
