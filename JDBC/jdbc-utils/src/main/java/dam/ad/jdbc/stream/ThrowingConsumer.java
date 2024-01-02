package dam.ad.jdbc.stream;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;

    default ThrowingConsumer<T, E> andThen(ThrowingConsumer<T, E> after) {
        return t -> {
            accept(t);
            after.accept(t);
        };
    }

}
