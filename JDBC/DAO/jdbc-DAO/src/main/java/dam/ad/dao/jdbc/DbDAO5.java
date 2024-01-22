package dam.ad.dao.jdbc;

import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.stream.SQLThrowingConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Supplier;

public abstract class DbDAO5<T> extends DbDAO4<T> {
    public DbDAO5(Supplier<Connection> connectionSupplier) {
        super(connectionSupplier);
    }

    protected abstract Object[] getInsertParamValues(T t);

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(T t) {
        Object[] paramValues = getInsertParamValues(t);
        return JDBCQuery.createParamSetterFrom(paramValues);
    }
}
