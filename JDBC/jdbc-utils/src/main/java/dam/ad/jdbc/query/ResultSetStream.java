package dam.ad.jdbc.query;

import dam.ad.jdbc.stream.generation.Generators;
import dam.ad.stream.StreamAdapter;
import dam.ad.jdbc.stream.generation.StreamGenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ResultSetStream<T> extends StreamAdapter<T> {
    private final ResultSet resultSet;

    /*public ResultSetStream(ResultSet rs, DTOMapper<T> dtoMapper) {
        this(Generators.getStreamGenerator(Generators.Yield.LAZY), rs, dtoMapper);
    }*/

    public ResultSetStream(StreamGenerator<T> generator, ResultSet rs, DTOMapper<T> dtoMapper) {
        super(generate(generator, rs, dtoMapper));
        this.resultSet = rs;
    }

    /**
     * Se llama a este constructor cada vez que se aplica un operador intermedio
     * Cada operador intermedio devuelve un nuevo objeto Stream, no modifica el estado del Stream
     */
    private ResultSetStream(ResultSetStream<T> resultSetStream, Stream<T> stream) {
        super(stream);
        this.resultSet = resultSetStream.resultSet;
    }


    private static <T> Stream<T> generate(
            StreamGenerator<T> generator, ResultSet rs, DTOMapper<T> dtoMapper) {

        try {
            return generator.generate(rs, dtoMapper);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static <T> Stream<T> generate(ResultSet rs, DTOMapper<T> dtoMapper) {

        StreamGenerator<T> streamGenerator =
                Generators.getStreamGenerator(Generators.Yield.LAZY);

        return generate(streamGenerator, rs, dtoMapper);


        /*Spliterator<T> spliterator = new Spliterators.AbstractSpliterator<>(
                Long.MAX_VALUE,
                Spliterator.ORDERED
        ) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                try {
                    if (!rs.next()) return false;
                    action.accept(dtoMapper.apply(rs));
                    return true;
                } catch (SQLException e) {
                    throw new RuntimeException("Cannot advance spliterator", e);
                }
            }
        };

        //return StreamSupport.stream(spliterator, false);
        //.onClose(() -> close(rs));

        Stream<T> resultSetStream = StreamSupport.stream(spliterator, false)
                .onClose(()-> close(rs));

        return Stream.of(resultSetStream, Stream.<T>empty())
                .flatMap(stream -> stream); //flatMap auto-cierra los streams internos*/


    }


    /**
     * Usarla implica que se continúa con un Stream especifico de tipo ResultSetStream
     */
    @Override
    public ResultSetStream<T> filter(Predicate<? super T> predicate) {
        System.out.println("ResultSetStream: Llamada a filter");
        return new ResultSetStream<>(this, super.filter(predicate));
    }

    @Override
    public ResultSetStream<T> limit(long maxSize) {
        System.out.println("ResultSetStream: Llamada a limit");
        return new ResultSetStream<>(this, super.limit(maxSize));
    }

    @Override
    public ResultSetStream<T> skip(long n) {
        System.out.println("ResultSetStream: Llamada a skip");
        return new ResultSetStream<>(this, super.skip(n));
    }


    @Override
    public void forEach(Consumer<? super T> action) {
        System.out.println("ResultSetStream.forEach: Llamando a forEach de la base...");
        super.forEach(action);

        // Ya no hace falta llamar a close para asegurar el cierre del ResultSet
        // Si el Stream se ha generado a partir de flatmap
        //System.out.println("ResultSetStream.forEach: Llamando a close()...");
        //close();
    }


    /**
     * Metodo close que implementa la interfaz Closeable
     */
    @Override
    public void close() {
        System.out.println("Llamada al autoclose en ResultSetStream");
        close(this.resultSet);
        super.close();
    }

    /**
     * Cierra un ResultSet, se debe llamar cuando ya hemos acabado de leer los datos que contenía
     */
    static void close(ResultSet resultSet) {
        System.out.println("ResultSetStream: llamada a close(ResultSet)");
        try {
            if (!resultSet.isClosed()) {
                System.out.println("ResultSetStream: Cerrando resultSet");
                resultSet.close();
            } else {
                System.out.println("ResultSetStream: El resultSet ya estaba cerrado.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
