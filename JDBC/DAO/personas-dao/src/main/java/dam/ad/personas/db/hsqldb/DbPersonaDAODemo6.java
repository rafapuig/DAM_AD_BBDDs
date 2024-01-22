package dam.ad.personas.db.hsqldb;

import static dam.ad.personas.db.hsqldb.DbPersonaDAODemo5.testWithManager;

public class DbPersonaDAODemo6 {
    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas60";

    public static void main(String[] args) throws Exception {

        PersonasDAOManager manager = new PersonasDAO4Manager(URL);

        testWithManager(manager);

    }

}
