package dam.ad.futbol.file;

import dam.ad.file.DTOReader;
import dam.ad.futbol.model.Equipo;

import java.net.URL;

public class EquipoDTOReader extends DTOReader<Equipo> {

    static String DEFAULT_RESOURCE_NAME = "/futbol/equipos.csv";
    String resourceName;

    public EquipoDTOReader(String resourceName) {
        this.resourceName = resourceName;
    }

    public EquipoDTOReader() {
        this(DEFAULT_RESOURCE_NAME);
    }

    @Override
    protected URL getURL() {
        return this.getClass().getResource(resourceName);
    }

    @Override
    protected Equipo read(String line) {

        String[] parts = line.split(",");

        String nombre = parts[0].trim();
        String pais = parts[1].trim();

        return new Equipo(0, nombre, pais);
    }
}
