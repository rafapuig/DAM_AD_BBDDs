package dam.ad.jpa.entity.table;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Personas")
public class Persona_OverrideDefaultTableName {
      @Id
      private long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
}
