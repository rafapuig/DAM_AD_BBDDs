package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.DatabaseSchema;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonasDatabaseSchema implements DatabaseSchema {
    String sqlCreateTablePersona;

    public PersonasDatabaseSchema() {
        initCreateSchemaScript();
    }

    protected void initCreateSchemaScript() {

        URL url = this.getClass().getResource("/personas/PersonasSchema.sql");

        this.sqlCreateTablePersona = DatabaseSchema.super.getCreateSchemaFrom(url);
        //sqlCreateTablePersona = getCreateSchemaFrom(url);
    }

    @Override
    public String getCreateSchemaFrom(URL url) {
        System.out.println("Version de reemplazo en la case PersonaDatabaseSchema");
        try (InputStream input = Objects.requireNonNull(url).openStream();
             InputStreamReader isr = new InputStreamReader(input);
             BufferedReader br = new BufferedReader(isr)) {

            return br.lines().reduce("",
                    (text, line) -> text + line + System.lineSeparator(),
                    String::concat);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCreateSchema() {
        return sqlCreateTablePersona;
    }

    @Override
    public String getDropSchema() {
        return "DROP TABLE PERSONA IF EXISTS";
    }
}
