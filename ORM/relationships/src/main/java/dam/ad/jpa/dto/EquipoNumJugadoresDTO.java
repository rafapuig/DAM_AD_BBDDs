package dam.ad.jpa.dto;

import dam.ad.jpa.entities.Equipo;

public record EquipoNumJugadoresDTO(
        Equipo e,
        Long jugadoresCount
) {
}
