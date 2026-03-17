package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Destreza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activa = true;

    @ManyToOne
    @JoinColumn(name = "padre_id")
    private Destreza padre; // referencia a la destreza padre
}

