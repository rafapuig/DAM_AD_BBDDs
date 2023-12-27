package dam.ad.jdbc.dataaccess;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface DTOSupplier<T> extends Supplier<Stream<T>> {
    @Override
    Stream<T> get();

    default Stream<T> getStream() {
        return get();
    }

    default List<T> asList() {
        try (Stream<T> stream = getStream()) {
            return stream.toList();
        }
    }

    default Map<Integer, T> asMap(
            Function<T, Integer> keyMapper) {

        try (Stream<T> stream = getStream()) {
            return stream.collect(Collectors.toMap(
                    keyMapper,
                    Function.identity()
            ));
        }
    }

}

