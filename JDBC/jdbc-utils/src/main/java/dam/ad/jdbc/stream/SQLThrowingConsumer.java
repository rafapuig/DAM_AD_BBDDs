package dam.ad.jdbc.stream;

import java.sql.SQLException;

public interface SQLThrowingConsumer<T> extends ThrowingConsumer<T, SQLException> {
}
