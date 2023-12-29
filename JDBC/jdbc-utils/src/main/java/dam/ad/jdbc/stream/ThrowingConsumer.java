package dam.ad.jdbc.stream;

public interface ThrowingConsumer<T, E extends Exception> {
     void accept(T t) throws E;
}
