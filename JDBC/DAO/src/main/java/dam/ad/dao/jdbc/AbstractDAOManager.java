package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.jdbc.JDBCUtil;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class AbstractDAOManager implements DAOManager, AutoCloseable {

    protected DataSource dataSource;
    protected DatabaseSchema dbSchema;
    protected DAOFactory daoFactory;

    private Map<Class<?>, Supplier<DTOMapper<?>>> dtoMappersMap;

    public AbstractDAOManager() {
        this.dtoMappersMap = getDTOMappersMap();
    }

    protected abstract Map<Class<?>, Supplier<DTOMapper<?>>> getDTOMappersMap();

    @Override
    public DAOFactory getDAOFactory() {
        return daoFactory;
    }

    public void generateSchema() {
        dbSchema.createSchema(dataSource);
    }

    public void dropSchema() {
        dbSchema.dropSchema(dataSource);
    }

    public void shutdown() {
        DatabaseSchema.execute(dataSource, "SHUTDOWN");
    }

    public <DTO> DAO<DTO> createDAO(Class<DTO> dtoClass) {

        return daoFactory.createDAO(dtoClass);

        /*return switch (dtoClass.getSimpleName()) {
            case "Persona" -> (DAO<DTO>) new DbPersonaDAO2(dataSource);
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + dtoClass.getSimpleName());
        };*/
    }

    @Override
    public final <T> DTOMapper<T> getDTOMapper(Class<T> tClass) {
        DTOMapper<?> dtoMapper = this.dtoMappersMap.get(tClass).get();
        return (DTOMapper<T>) dtoMapper;
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    Connection currentConnection;

    @Override
    public Connection getConnection() {

        // Si no estamos en medio de una transacción
        if(transactionState != TransactionState.STARTED) {
            currentConnection = getNewConnection(); //Obtenemos una nueva conexion
        }

        //currentConnection.isClosed();

        return currentConnection;

        /*return Objects.requireNonNullElseGet(
                currentConnection,
                this::getNewConnection);*/

        //return currentConnection != null ? currentConnection : getNewConnection();
    }

    @Override
    public void close() throws Exception {
        System.out.println("Cerrando el DAOManager...");
        JDBCUtil.close(currentConnection);
    }

    enum TransactionState {NONE, STARTED}

    TransactionState transactionState = TransactionState.NONE;

    @Override
    public void beginTransaction() {
        if (transactionState == TransactionState.STARTED)
            throw new IllegalStateException("Already in a transaction!!!");

        JDBCUtil.close(currentConnection); //Cerrar la conexión con autocommit si la hubiera

        System.out.println("Beginning transaction...");
        transactionState = TransactionState.STARTED;
        currentConnection = getNewConnection();

        // Establecemos que la conexión no use AUTO-COMMIT
        JDBCUtil.setAutoCommit(currentConnection, false);
    }

    private Connection getNewConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void commit() {
        if (transactionState == TransactionState.NONE)
            throw new IllegalStateException("Not in a transaction");

        System.out.println("Commiting operations to DB...");
        JDBCUtil.commit(currentConnection);
        JDBCUtil.close(currentConnection);
        transactionState = TransactionState.NONE;
        currentConnection = null;
    }

    @Override
    public void rollback() {
        if (transactionState == TransactionState.NONE)
            throw new IllegalStateException("Not in a transaction");

        JDBCUtil.rollback(currentConnection);
        JDBCUtil.close(currentConnection);
        currentConnection = null;
    }

    @Override
    public <T> Stream<T> query(String SQL, DTOMapper<T> dtoMapper, Object... params) {
        return JDBCQuery.query(
                getConnection(),
                SQL,
                dtoMapper,
                params);
    }
}
