package dam.ad.dao.jdbc;

import java.sql.Connection;
import java.util.function.Supplier;

public abstract class DbDAO4<T> extends DbDAO3<T> {
    private final Supplier<Connection> connectionSupplier;
    public DbDAO4(Supplier<Connection> connectionSupplier) {
        super(null);
        this.connectionSupplier = connectionSupplier;
    }
    @Override
    protected Connection getConnection() {
        return connectionSupplier.get();
    }
}
