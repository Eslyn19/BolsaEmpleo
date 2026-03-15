package una.ac.cr.p1bolsaempleo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private String id;
    private String nombre;
    private int nivel;       // 1 a 5
}
