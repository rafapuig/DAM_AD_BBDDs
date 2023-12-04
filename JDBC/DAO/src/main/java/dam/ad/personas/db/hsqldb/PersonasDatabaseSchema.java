package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.DatabaseSchema;

import java.io.*;
import java.net.URL;
import java.util.Objects;

public class PersonasDatabaseSchema implements DatabaseSchema {

    String sqlCreateTablePersona;

    public PersonasDatabaseSchema() {

        InputStream input = null;
        try {
            URL url = this.getClass().getResource("/PersonasSchema.sql");
            //System.out.println(url);

            input = Objects.requireNonNull(url).openStream();

        InputStreamReader isr = new InputStreamReader(input);
        BufferedReader br = new BufferedReader(isr);

        sqlCreateTablePersona = br.lines()
                .reduce("",
                        (text, line) -> text + line + "\n",
                        String::concat);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCreateSchema() {


        return sqlCreateTablePersona;


        /*return """
            CREATE TABLE IF NOT EXISTS persona(
            personaID INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY,
            nombre VARCHAR(20) NOT NULL,
            apellidos VARCHAR(30) NOT NULL,
            sexo CHAR(1) NOT NULL,
            nacimiento DATE,
            ingresos REAL)
            """;*/
    }

    @Override
    public String getDropSchema() {
        return """
                DROP TABLE PERSONA IF EXISTS
                """;
    }
}
