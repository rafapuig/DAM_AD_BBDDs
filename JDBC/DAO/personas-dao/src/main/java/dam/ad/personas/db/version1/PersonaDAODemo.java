package dam.ad.personas.db.version1;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.DatabaseSchema;

import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;
import dam.ad.personas.db.hsqldb.PersonasDatabaseSchema;

import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

public class PersonaDAODemo {

    static final String URL =
            "jdbc:hsqldb:C:/BBDDs/hsqldb/personas10;create=true;shutdown=true";

    public static void main(String[] args) throws Exception {

        DataSource dataSource = createDataSource();

        generateSchema(dataSource);

        DAO<Persona> personaDAO = new PersonaDAO(dataSource);

        Persona newPersona = new Persona(
                -1,
                "Aitor",
                "Tilla",
                Sexo.HOMBRE,
                LocalDate.parse("1998-01-14"),
                1900.0f);

        personaDAO.add(newPersona);

        Optional<Persona> personaById = personaDAO.getById(1);

        personaById.ifPresent(System.out::println);

        personaById.ifPresent(persona -> {
                    Persona p2 = Persona.builder(personaById.get())
                            .nombre("Amador")
                            .apellidos("Denador")
                            .sexo(Sexo.MUJER)
                            .nacimiento(LocalDate.parse("1992-03-27"))
                            .ingresos(1800.0f).build();

                    personaDAO.update(p2);
                }
        );

        personaById.ifPresent(personaDAO::delete);

        personaDAO.getAll()
                .forEach(System.out::println);

        System.out.println(personaDAO.getCount());

        shutdown(dataSource);
    }

    private static DataSource createDataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("url", URL);
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");
        properties.setProperty("shutdown", "true");

        DataSource dataSource = JDBCDataSourceFactory.createDataSource(properties);
        return dataSource;
    }

    private static void generateSchema(DataSource dataSource) throws SQLException {
        DatabaseSchema databaseSchema = new PersonasDatabaseSchema();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            System.out.println(databaseSchema.getCreateSchema());
            stmt.execute(databaseSchema.getCreateSchema());
        }
    }

    private static void shutdown(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("SHUTDOWN");
        }
    }
}
