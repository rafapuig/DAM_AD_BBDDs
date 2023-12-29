package dam.ad.jdbc.query;


import dam.ad.jdbc.stream.ThrowingFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface DTOMapper<T> extends ThrowingFunction<ResultSet, T, SQLException> {
    @Override
    T apply(ResultSet resultSet) throws SQLException;
}
