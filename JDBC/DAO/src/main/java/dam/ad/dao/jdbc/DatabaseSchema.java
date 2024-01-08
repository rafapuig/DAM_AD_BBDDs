package dam.ad.dao.jdbc;

import dam.ad.jdbc.query.JDBCQuery;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Stream;

public interface DatabaseSchema {
    String getCreateSchema();

    String getDropSchema();

    default void createSchema(DataSource dataSource) {
        System.out.println("Creando el esquema de la base de datos...");
        String SQLScript = this.getCreateSchema();
        execute(dataSource, SQLScript);
    }

    default void dropSchema(DataSource dataSource) {
        System.out.println("Eliminando el esquema de la base de datos...");
        String SQLScript = this.getDropSchema();
        execute(dataSource, SQLScript);
    }

    static void execute(DataSource dataSource, String SQLScript) {

        String[] sqls = SQLScript.split(";");

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            for (String sql : sqls) {

                if(!sql.isBlank()) {
                    JDBCQuery.update(connection, sql, null);
                }
            }
            connection.commit();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    default String initCreateSchemaScript(URL url) {
        System.out.println("Leyendo el contenido del archivo del esquema de la base de datos...");
        URI uri = URI.create(url.toString());
        System.out.println(uri);

        try(Stream<String> lines = Files.lines(Paths.get(uri))) {
            return lines.reduce("",
                    (text, line) -> text + line + System.lineSeparator(),
                    String::concat);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
