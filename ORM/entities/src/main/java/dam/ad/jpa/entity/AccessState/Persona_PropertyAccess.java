package dam.ad.jpa.entity.AccessState;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
public class Persona_PropertyAccess {

    private long id;
    @Getter
    private String nombre;
    @Getter
    private String apellidoPaterno;
    @Getter
    private String apellidoMaterno;

    //Dado que la anotación la indicamos en la propiedad (getter)
    //Se asume que se usa acceso a través de propiedades por el proveedor ORM
    @Id
    public long getId() {
        return id;
    }

}
