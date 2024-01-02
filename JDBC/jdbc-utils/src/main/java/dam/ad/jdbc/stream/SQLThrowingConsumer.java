package dam.ad.jdbc.stream;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLThrowingConsumer<T> extends ThrowingConsumer<T, SQLException> {
    default SQLThrowingConsumer<T> andThen(SQLThrowingConsumer<T> after) {
        return (SQLThrowingConsumer<T>) ThrowingConsumer.super.andThen(after);
    }
}
