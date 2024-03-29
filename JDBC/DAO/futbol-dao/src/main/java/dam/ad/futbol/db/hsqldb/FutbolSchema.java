package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.jdbc.AbstractDatabaseSchema;

public class FutbolSchema extends AbstractDatabaseSchema {
    @Override
    protected String getResourceName() {
        return "/futbol/DatabaseSchema.sql";
    }

    @Override
    public String getDropSchema() {
        return "DROP TABLE IF EXISTS jugador; DROP TABLE IF EXISTS equipo";
    }
}
