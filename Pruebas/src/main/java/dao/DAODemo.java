package dao;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.DatabaseSchema;

import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;
import dam.ad.personas.db.hsqldb.DbPersonaDAO;
import dam.ad.personas.db.hsqldb.PersonasDatabaseSchema;

import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

public class DAODemo {

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:hsqldb:C:/BBDDs/hsqldb/personas10;create=true;shutdown=true");
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");
        properties.setProperty("shutdown", "true");

        DataSource dataSource = JDBCDataSourceFactory.createDataSource(properties);

        DatabaseSchema databaseSchema = new PersonasDatabaseSchema();

        //Connection connection = dataSource.getConnection();
        //connection.setAutoCommit(false);
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            System.out.println(databaseSchema.getCreateSchema());
            stmt.execute(databaseSchema.getCreateSchema());
            //connection.commit();
            //stmt.executeUpdate("INSERT INTO persona VALUES (DEFAULT,'Sandra', 'Matica', 'M', '1987-2-4', 2000.0)");
            //connection.commit();
            //stmt.execute("SHUTDOWN");

            stmt.close();
            //connection.close();
        }

        DAO<Persona> personaDAO = new DbPersonaDAO(dataSource); //, true);

        Optional<Persona> personaById = personaDAO.getById(13);

        personaById.ifPresent(System.out::println);

        Persona newPersona = new Persona(
                -1,
                "Aitor",
                "Tilla",
                Sexo.HOMBRE,
                LocalDate.parse("1998-01-14"),
                1900.0f);

        personaDAO.add(newPersona);


        personaById.ifPresent(persona -> {
                    Persona p2 = Persona.builder(personaById.get())
                            .nombre("Amador")
                            .apellidos("Denador")
                            .sexo(Sexo.MUJER)
                            .nacimiento(LocalDate.parse("1992-03-27"))
                            .ingresos(1800).build();

                    personaDAO.update(p2);
                }
        );

        personaById.ifPresent(personaDAO::delete);

        personaDAO.getAll()
                .forEach(System.out::println);

        System.out.println(personaDAO.getCount());
    }
}
