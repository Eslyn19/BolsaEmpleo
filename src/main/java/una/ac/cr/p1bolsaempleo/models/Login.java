package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "login")
public class Login {
    @Id
    @Column(name = "usuario", length = 120)
    private String usuario;

    @Column(name = "clave", nullable = false, length = 255)
    private String clave;
}
