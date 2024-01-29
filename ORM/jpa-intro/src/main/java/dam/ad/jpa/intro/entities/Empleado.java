package dam.ad.jpa.intro.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Empleado {
    @Id
    private int id;
    private String nombre;
    private String apellidos;
    private long salario;
}
