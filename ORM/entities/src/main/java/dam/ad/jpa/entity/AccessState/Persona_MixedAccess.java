package dam.ad.jpa.entity.AccessState;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EnumSet;

@AllArgsConstructor
@Entity
//Explícitamente,
// le decimos que por defecto use acceso a través de campos
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Persona_MixedAccess {

    enum Apellidos {PATERNO, MATERNO}

    @Getter @Setter
    @Id
    private long id;

    @Getter @Setter
    private String nombre;

    @Transient
    private String[] apellidos = new String[Apellidos.values().length];

    @Access(AccessType.PROPERTY)
    public String getApellidoPaterno() {
        return apellidos[Apellidos.PATERNO.ordinal()];
    }

    public void setApellidoPaterno(String apellido) {
        this.apellidos[0] = apellido;
    }

    @Access(AccessType.PROPERTY)
    public String getApellidoMaterno() {
        return apellidos[Apellidos.MATERNO.ordinal()];
    }

    public void setApellidoMaterno(String apellido) {
        this.apellidos[1] = apellido;
    }
}
