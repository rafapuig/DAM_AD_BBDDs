package dam.ad.jdbc.stream.generation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class LazyStreamGenerator<T> implements StreamGenerator<T> {
    @Override
    public Stream<T> generate(
            ResultSet rs, ThrowingFunction<ResultSet, T, SQLException> dtoMapper) {

        Spliterator<T> spliterator = new ResultSetLazySpliterator<>(rs, dtoMapper);

        Stream<T> resultSetStream = StreamSupport.stream(spliterator, false)
                .onClose(() -> close(rs));

        return Stream.of(resultSetStream, Stream.<T>empty())
                .flatMap(stream -> stream); //flatMap auto-cierra los streams internos
    }

}
