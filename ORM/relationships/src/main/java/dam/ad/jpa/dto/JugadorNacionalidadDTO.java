package dam.ad.jpa.dto;

import dam.ad.dto.annotations.RowConvertible;
import dam.ad.dto.annotations.RowField;
import dam.ad.jpa.entities.Jugador;


@RowConvertible
public record JugadorNacionalidadDTO(
        @RowField(columnLength = 20, asComplex = true, expression = "nombre") Jugador jugador,
        @RowField(columnLength = 30) String nacionalidad) {
}

