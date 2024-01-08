package dam.ad.jdbc.stream;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@FunctionalInterface
public interface SQLThrowingConsumer<T> extends ThrowingConsumer<T, SQLException> {
    default SQLThrowingConsumer<T> andThen(SQLThrowingConsumer<T> after) {
        return t -> {
            accept(t);
            after.accept(t);
        };
        //return (SQLThrowingConsumer<T>) ThrowingConsumer.super.andThen(after);
    }


}
