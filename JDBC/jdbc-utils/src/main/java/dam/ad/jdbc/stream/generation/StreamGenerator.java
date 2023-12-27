package dam.ad.jdbc.stream.generation;

import dam.ad.jdbc.query.DTOMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public abstract class StreamGenerator<T> {
    ResultSet resultSet;
    DTOMapper<T> dtoMapper;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public DTOMapper<T> getDtoMapper() {
        return dtoMapper;
    }

    public StreamGenerator(ResultSet resultSet, DTOMapper<T> dtoMapper) {
        this.resultSet = resultSet;
        this.dtoMapper = dtoMapper;
    }

    public abstract Stream<T> generate();

    /**
     * Cierra un ResultSet, se debe llamar cuando ya hemos acabado de leer los datos que contenía
     */
    public void close(ResultSet resultSet) {
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
