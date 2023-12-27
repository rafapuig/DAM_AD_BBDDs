package dam.ad.stream;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.stream.generation.StreamGenerator;
import dam.ad.jdbc.stream.generation.ThrowingFunction;
import dam.ad.jdbc.stream.generation.Generators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class StreamUtil {
    public static <T> Stream<T> generateStreamFromResultSet(
            ResultSet rs,
            ThrowingFunction<ResultSet, T, SQLException> dtoMapper,
            boolean lazy) throws SQLException {

        StreamGenerator<T> streamGenerator =
                Generators.getStreamGenerator(rs, (DTOMapper<T>) dtoMapper, lazy);

        Stream<T> stream = streamGenerator.generate();

        //System.out.println("Llamando a cerrar resultSet...");
        if(!lazy) streamGenerator.close(rs);

        return stream;
    }

    public static <T> Stream<T> generateStreamFromResultSet(
            ResultSet rs,
            ThrowingFunction<ResultSet, T, SQLException> dtoMapper) throws SQLException {

        return generateStreamFromResultSet(rs, dtoMapper, false);
    }

}
