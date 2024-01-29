package dam.ad.jpa.entity.AccessState;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Persona_FieldAccess {
    //Dado que la anotación la indicamos en el campo
    //Se asume que se usa acceso a campos por el proveedor ORM
    @Id private long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
}
