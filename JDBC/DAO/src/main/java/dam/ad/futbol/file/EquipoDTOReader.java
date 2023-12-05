package dam.ad.futbol.file;

import dam.ad.file.DTOReader;
import dam.ad.futbol.model.Equipo;

public class EquipoDTOReader extends DTOReader<Equipo> {
    @Override
    protected Equipo read(String line) {
        String[] parts = line.split(",");
        return new Equipo(0, parts[0].trim(), parts[1].trim());
    }
}
