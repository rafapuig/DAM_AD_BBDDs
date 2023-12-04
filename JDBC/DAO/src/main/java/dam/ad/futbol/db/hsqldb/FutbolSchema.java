package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.jdbc.DatabaseSchema;
import dam.ad.file.ResourceReader;

public class FutbolSchema implements DatabaseSchema {
    @Override
    public String getCreateSchema() {
        return ResourceReader.getSQL("/futbol/DatabaseSchema.sql");
    }

    @Override
    public String getDropSchema() {
        return "DROP TABLE jugador;DROP TABLE equipo";
    }
}
