package dam.ad.jdbc.stream;

import java.util.function.Consumer;

public interface ThrowingConsumer<T, E extends Exception> {
     void accept(T t) throws E;
}
