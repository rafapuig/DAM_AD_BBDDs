package dam.ad.stream;

import dam.ad.jdbc.stream.generation.ThrowingFunction;
import dam.ad.jdbc.stream.generation.Generators;
import dam.ad.jdbc.stream.generation.StreamGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class StreamUtil {
    public static <T> Stream<T> generateStreamFromResultSet(
            ResultSet rs,
            ThrowingFunction<ResultSet, T, SQLException> dtoMapper,
            boolean lazy) throws SQLException {

        StreamGenerator<T> streamGenerator =
                Generators.getStreamGenerator(
                        lazy ? Generators.Yield.LAZY : Generators.Yield.EAGER);

        Stream<T> stream =
                streamGenerator.generate(rs, dtoMapper);

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
