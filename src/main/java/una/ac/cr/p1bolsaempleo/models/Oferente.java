package una.ac.cr.p1bolsaempleo.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;


//Oferente: El sistema deberá permitirles a los oferentes realizar las siguientes funciones:
//• Registrar sus datos (identificación, nombre, primer apellido, nacionalidad, teléfono, correo electrónico y
//lugar de residencia).
//• Ingresar al sistema usando correo la clave, y mostrar su tablero (dashboard)
//• Registrar (actualizar) la lista de sus característica o destrezas y el nivel que tiene en ellas.
//• Subir su currículo en formato pdf
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Oferente {
    @Id
    private String id;
    private String nombre;
    private String primerAp;
    private int nacionalidad;
    private String telefono;
    private String correo;
    private String lugarResidencia;
    @OneToMany(mappedBy = "oferente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OferenteDestreza> destrezas = new ArrayList<>();

}