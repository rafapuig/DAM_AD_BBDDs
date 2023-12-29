package dam.ad.jdbc.stream.generation;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.stream.ThrowingFunction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;


public interface IStreamGenerator<T> {


    public ResultSet getResultSet();


    public DTOMapper<T> getDtoMapper();

    Stream<T> generate(ResultSet rs, DTOMapper<T> dtoMapper) throws SQLException;

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
