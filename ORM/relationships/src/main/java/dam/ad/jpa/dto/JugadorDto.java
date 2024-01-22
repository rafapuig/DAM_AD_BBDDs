package dam.ad.jpa.dto;

import dam.ad.jpa.entities.Demarcacion;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link dam.ad.jpa.entities.Jugador}
 */
public record JugadorDto(Long id, String nombre, LocalDate nacimiento, Demarcacion demarcacion,
                         Set<PaisDto> nacionalidades, String equipoNombre, int dorsal) implements Serializable {
    /**
     * DTO for {@link dam.ad.jpa.entities.Pais}
     */
    public record PaisDto(String iso3, String nombre) implements Serializable {
    }
}