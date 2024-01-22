package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.AbstractDatabaseSchema;

public class PersonasDatabaseSchema extends AbstractDatabaseSchema {
    @Override
    protected String getResourceName() {
        return "/personas/PersonasSchema.sql";
    }

    @Override
    public String getDropSchema() {
        return "DROP TABLE PERSONA IF EXISTS";
    }
}
