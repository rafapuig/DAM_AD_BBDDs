package dam.ad.audiovisuales.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pelicula {

    private int peliculaID;

    private String titulo;

    private String aka;

    private int año;

    private int duracion;

    private String pais;
}
