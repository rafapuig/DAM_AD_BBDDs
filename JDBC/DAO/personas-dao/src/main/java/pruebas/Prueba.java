package pruebas;

import dam.ad.personas.db.hsqldb.PersonasDatabaseSchema;

import java.net.URL;

public class Prueba {

    public static void main(String[] args) {
        PersonasDatabaseSchema schema = new PersonasDatabaseSchema();
        URL url = PersonasDatabaseSchema.class.getResource("/personas/PersonasSchema.sql");
        System.out.println(url);
    }
}
