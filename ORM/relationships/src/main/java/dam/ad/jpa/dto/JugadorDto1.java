package dam.ad.jpa.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link dam.ad.jpa.entities.Jugador}
 */
public record JugadorDto1(String nombre, LocalDate nacimiento, Set<PaisDto> nacionalidades,
                          String equipoNombre) implements Serializable {
    /**
     * DTO for {@link dam.ad.jpa.entities.Pais}
     */
    public record PaisDto(String iso3, String nombre) implements Serializable {
    }
}