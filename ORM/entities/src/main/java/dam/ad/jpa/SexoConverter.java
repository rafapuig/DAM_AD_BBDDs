package dam.ad.jpa;

import dam.ad.jpa.entity.Sexo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SexoConverter implements AttributeConverter<Sexo, String> {
    @Override
    public String convertToDatabaseColumn(Sexo attribute) {
        return switch (attribute) {
            case HOMBRE -> "H";
            case MUJER -> "M";
        };
    }

    @Override
    public Sexo convertToEntityAttribute(String dbData) {
        return switch (dbData){
            case "H" -> Sexo.HOMBRE;
            case "M" -> Sexo.MUJER;
            default -> throw new IllegalStateException("Unexpected value: " + dbData);
        };
    }
}
