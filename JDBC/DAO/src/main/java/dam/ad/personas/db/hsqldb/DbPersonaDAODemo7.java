package dam.ad.personas.db.hsqldb;

import dam.ad.model.personas.Persona;

import java.util.stream.Stream;

public class DbPersonaDAODemo7 {
    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas60";

    public static void main(String[] args) throws Exception {

        PersonasDAOManager manager = new PersonasDAO4Manager(URL);

        Stream<String> nombres = manager
                .query(
                        "SELECT nombre + ' ' + apellidos AS nombre_completo FROM persona",
                        resultSet -> resultSet.getObject(1, String.class));

        nombres.forEach(System.out::println);

        Stream<String> nombres2 = manager
                .querySingleColumn(
                        "SELECT nombre + ' ' + apellidos AS nombre_completo FROM persona",
                        String.class);

        nombres2.forEach(System.out::println);


        manager.close();

    }

}
