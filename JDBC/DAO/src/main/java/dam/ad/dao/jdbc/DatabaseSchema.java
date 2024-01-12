package dam.ad.dao.jdbc;

import dam.ad.jdbc.query.JDBCQuery;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DatabaseSchema {
    String getCreateSchema();

    String getDropSchema();

    default void createSchema(DataSource dataSource) {
        System.out.println("Creando el esquema de la base de datos...");
        String SQLScript = this.getCreateSchema();
        System.out.println(SQLScript);

        execute(dataSource, SQLScript);

        System.out.println("Base de datos generada.");
    }

    default void dropSchema(DataSource dataSource) {
        System.out.println("Eliminando el esquema de la base de datos...");
        String SQLScript = this.getDropSchema();
        System.out.println(SQLScript);

        execute(dataSource, SQLScript);
        System.out.println("Base de datos eliminada.");
    }

    static void execute(DataSource dataSource, String SQLScript) {

        String[] sqlCommands = SQLScript.split(";");

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            for (String sql : sqlCommands) {

                if(!sql.isBlank()) {
                    JDBCQuery.update(connection, sql, null);
                }
            }
            connection.commit();
            connection.close(); //No hace falta cerrar al usar try-with-resources

        } catch (SQLException e) { // Si hay una excepción no se hace COMMIT y como se ciera la CON no hay cambios
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Devuelve el script de creación de la BD a partir de un archivo de texto que se
     * encuentra localizado en la URL proporcionada como parámetro
     * @param url localización del archivo de texto que contiene el script de creación de la BD
     * @return un String con el contenido del script
     */

    default String getCreateSchemaFrom(URL url) {
        System.out.println("Leyendo el contenido del archivo del esquema de la base de datos...");
        System.out.println("URL: " + url);

        //El método File.lines se debe usar obligatoriamente con try-with-resources
        try(Stream<String> lines = Files.lines(Path.of(url.toURI()))) {
            return lines
                    .collect(Collectors
                            .joining(System.lineSeparator()));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
