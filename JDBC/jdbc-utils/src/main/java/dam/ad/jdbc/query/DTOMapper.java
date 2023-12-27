package dam.ad.jdbc.query;

import dam.ad.jdbc.stream.generation.ThrowingFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DTOMapper<T> implements ThrowingFunction<ResultSet,T, SQLException> {
    @Override
    public abstract T apply(ResultSet resultSet) throws SQLException;
}
