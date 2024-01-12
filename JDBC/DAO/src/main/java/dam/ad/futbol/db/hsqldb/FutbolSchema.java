package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.jdbc.AbstractDatabaseSchema;
import dam.ad.dao.jdbc.DatabaseSchema;
import dam.ad.file.ResourceReader;

import java.net.URL;

public class FutbolSchema extends AbstractDatabaseSchema {
    @Override
    protected String getResourceName() {
        return "/futbol/DatabaseSchema.sql";
    }

    @Override
    public String getDropSchema() {
        return "DROP TABLE jugador;DROP TABLE equipo";
    }
}
