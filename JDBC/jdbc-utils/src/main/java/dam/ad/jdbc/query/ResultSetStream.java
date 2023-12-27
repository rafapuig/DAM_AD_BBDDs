package dam.ad.jdbc.query;

import dam.ad.jdbc.stream.generation.Generators;
import dam.ad.jdbc.stream.generation.StreamGenerator;
import dam.ad.stream.StreamAdapter;
import dam.ad.jdbc.stream.generation.IStreamGenerator;

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

    public ResultSetStream(StreamGenerator<T> generator) {
        super(generator.generate());
        this.resultSet = generator.getResultSet();
    }

    /**
     * Se llama a este constructor cada vez que se aplica un operador intermedio
     * Cada operador intermedio devuelve un nuevo objeto Stream, no modifica el estado del Stream
     */
    private ResultSetStream(ResultSetStream<T> resultSetStream, Stream<T> stream) {
        super(stream);
        this.resultSet = resultSetStream.resultSet;
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
