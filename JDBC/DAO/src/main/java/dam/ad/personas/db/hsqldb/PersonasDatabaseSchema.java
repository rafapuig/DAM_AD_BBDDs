package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.AbstractDatabaseSchema;
import dam.ad.dao.jdbc.DatabaseSchema;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;

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
