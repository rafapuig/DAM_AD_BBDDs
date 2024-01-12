package dam.ad.personas.db.hsqldb;

import java.util.stream.Stream;

import static dam.ad.personas.db.hsqldb.DbPersonaDAODemo5.testWithManager;

public class DbPersonaDAODemo6 {
    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas60";

    public static void main(String[] args) throws Exception {

        PersonasDAOManager manager = new PersonasDAO4Manager(URL);

        //testWithManager(manager);

        Stream<String> nombres = manager
                .query("SELECT nombre + ' ' + apellidos AS nombre_completo FROM persona",
                        resultSet -> resultSet.getObject(1, String.class));

        nombres.forEach(System.out::println);

        manager.close();

    }

}
