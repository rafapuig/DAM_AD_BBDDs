package dam.ad.jdbc.stream.generation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;


public interface StreamGenerator<T> {
    Stream<T> generate(ResultSet rs, ThrowingFunction<ResultSet, T, SQLException> dtoMapper) throws SQLException;

    /**
     * Cierra un ResultSet, se debe llamar cuando ya hemos acabado de leer los datos que contenía
     */
    default void close(ResultSet resultSet) {
        try {
            System.out.println("StreamGenerator: Cerrando resultSet...");
            if (!resultSet.isClosed()) {
                resultSet.close();
                System.out.println("StreamGenerator: ResultSet cerrado.");
            } else {
                System.out.println("StreamGenerator: El ResultSet ya estaba cerrado.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
