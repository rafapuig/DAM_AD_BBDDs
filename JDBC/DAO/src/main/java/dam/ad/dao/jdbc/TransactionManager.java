package dam.ad.dao.jdbc;

import java.sql.Connection;
import java.util.function.Supplier;

public class TransactionManager {

    Supplier<Connection> connectionSupplier;
    public TransactionManager(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    private Connection getNewConnection() {
        return connectionSupplier.get();
    }

    

}
