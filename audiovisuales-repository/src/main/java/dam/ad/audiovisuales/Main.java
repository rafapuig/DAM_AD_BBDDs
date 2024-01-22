package dam.ad.audiovisuales;

import dam.ad.audiovisuales.model.Pelicula;
import dam.ad.dao.DAO;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static String URL = "jdbc:hsqldb:file:BBDDs/hsqldb/audiovisuales";

    public static void main(String[] args) {

        DataSource dataSource = createDataSource(URL);

        dropSchema(dataSource);

        generateSchema(dataSource);

        try(DAO<Pelicula> peliculaDAO = new PeliculaDAO(dataSource)) {

            Pelicula pelicula = new Pelicula();
            pelicula.setTitulo("The Terminator");
            pelicula.setAño(1984);
            pelicula.setDuracion(108);
            pelicula.setPais("USA");
            peliculaDAO.add(pelicula);

            peliculaDAO.getAll().forEach(System.out::println);

            peliculaDAO.getById(1).ifPresent(System.out::println);

            pelicula.setDuracion(10000);

            peliculaDAO.update(pelicula);

            peliculaDAO.getAll().forEach(System.out::println);

            peliculaDAO.delete(pelicula);

            peliculaDAO.getAll().forEach(System.out::println);
        }

    }

    static String loadScript(URL url) {

        try (Stream<String> lines = Files.lines(Path.of(url.toURI()))) {

            return lines
                    .map(String::trim)
                    .collect(Collectors
                            .joining(System.lineSeparator()));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static DataSource createDataSource(String url) {
        JDBCDataSource jdbcDataSource = new JDBCDataSource();
        jdbcDataSource.setURL(url);
        jdbcDataSource.setUser("SA");
        jdbcDataSource.setPassword("");
        return jdbcDataSource;
    }

    static void executeScript(DataSource dataSource, String scriptSQL) {

        String[] sqlCommands = scriptSQL.split(";");

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            connection.setAutoCommit(false);

            for (String sqlCommand : sqlCommands) {
                if (!sqlCommand.isBlank()) {
                    stmt.execute(sqlCommand);
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void generateSchema(DataSource dataSource) {
        URL url = Main.class.getResource("/AudioVisualesSchema.sql");
        String createSchemaScript = loadScript(url);
        executeScript(dataSource, createSchemaScript);
    }

    static void dropSchema(DataSource dataSource) {
        executeScript(dataSource, "DROP TABLE pelicula IF EXISTS");
    }

}
