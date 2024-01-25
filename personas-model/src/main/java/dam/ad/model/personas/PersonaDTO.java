package dam.ad.model.personas;

import dam.ad.dto.annotations.RowField;

public record PersonaDTO(
        @RowField(columnLength = 8, label = "ID")
        int personaID,
        @RowField(columnLength = 20, label = "Nombre")
        String nombre,
        @RowField(columnLength = 5, label = "Si/no")
        boolean isCasado
) { }
