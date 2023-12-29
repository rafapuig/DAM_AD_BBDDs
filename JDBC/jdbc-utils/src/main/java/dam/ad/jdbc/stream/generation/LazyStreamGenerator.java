package dam.ad.jdbc.stream.generation;

import dam.ad.jdbc.query.DTOMapper;

import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class LazyStreamGenerator<T> extends StreamGenerator<T> {

    public LazyStreamGenerator() {
        super(null, null);
    }

    public LazyStreamGenerator(ResultSet resultSet, DTOMapper<T> dtoMapper) {
        super(resultSet, dtoMapper);
    }

    @Override
    public Stream<T> generate(
            ResultSet rs, DTOMapper<T> dtoMapper) {

        Spliterator<T> spliterator = new ResultSetLazySpliterator<>(rs, dtoMapper);

        Stream<T> resultSetStream = StreamSupport.stream(spliterator, false)
                .onClose(() -> close(rs));

        return Stream.of(resultSetStream, Stream.<T>empty())
                .flatMap(stream -> stream); //flatMap auto-cierra los streams internos
    }

    @Override
    public Stream<T> generate() {
        return generate(this.getResultSet(), this.getDtoMapper());
    }
}
