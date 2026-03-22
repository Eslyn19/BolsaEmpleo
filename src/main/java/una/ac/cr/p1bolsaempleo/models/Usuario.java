package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "idUsuario", nullable = false, length = 50)
    private String idUsuario;

    @Column(name = "clave", nullable = false, length = 300)
    private String clave;

    @Lob
    @Column(name = "rol", nullable = false)
    private String rol;


}