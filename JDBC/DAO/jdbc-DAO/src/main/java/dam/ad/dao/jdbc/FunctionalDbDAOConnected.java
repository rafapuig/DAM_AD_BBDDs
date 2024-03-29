package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;
import dam.ad.jdbc.JDBCUtil;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.stream.SQLThrowingConsumer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * En esta versión 2 del DbDAO, la implementación de los métodos de la interfaz DAO<T>
 * usa la conexión que obtienen a partir del método getConnection de la clase,
 * no obtienen una nueva conexión y cuando ejecutan el comando la cierran
 * Por otro lado, se delega la implementación en los métodos de la clase JDBCQuery
 * Podríamos decir que este DbDAO trabaja en modo conectado porque crea la conexión
 * al principio, en el constructor, y la reutiliza durante toda su vida
 * La implementación de los métodos utiliza un DTOMapper<T> para convertir los datos
 * del ResultSet en una instancia de tipo T. Las clases concretas que extienden esta clase
 * deben proporcionar el DTOMapper<T> concreto implementando el método abstracto getDTOMapper()
 */
public abstract class FunctionalDbDAOConnected<T> implements DAO<T> {

    protected final DataSource dataSource;
    Connection currentConnection;

    public FunctionalDbDAOConnected(DataSource dataSource) {
        this.dataSource = dataSource;
        currentConnection = getNewConnection();
    }

    private Connection getNewConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Proporciona la conexión que necesitan el resto de métodos sobre la cual ejecutar
     * el comando que deben enviar a la base de datos.
     */
    private Connection getConnection() {
            return currentConnection;
    }

    /**
     * La clase que extiende esta clase base debe implementar este método para que devuelva
     * un DTOMapper<T> donde T es el tipo del DTO.
     */
    protected abstract DTOMapper<T> getDTOMapper();

    /**
     * La clase que extiende esta clase base debe implementar este método para que devuelva
     * un String con la consulta SQL parametrizada que obtiene un registro de la tabla
     * filtrado por la clave primaria, de manera que se puede obtener un solo registro
     * como máximo si cuando se proporciona un valor de clave primaria que exista en la BD
     * La consulta solamente debe tener un parámetro, está limitado para claves simples de tipo entero
     */
    protected abstract String getSQLSelectByID();

    @Override
    public Optional<T> getById(int id) {

        return JDBCQuery.query(
                getConnection(),
                getSQLSelectByID(),
                preparedStatement -> preparedStatement.setInt(1, id),
                getDTOMapper())
                .findFirst();
    }


    // *********** ADD *****************************************************************

    /**
     * La clase que extiende esta clase base debe implementar este método para que devuelva
     * un SQLThrowingConsumer, un consumer que puede lanzar excepciones de tipo SQLException.
     * El método recibe como parámetro la referencia al objeto DTO del cual se van a
     * extraer los valores de sus atributos para establecer los valores de los parámetros
     * de la consulta SQL parametrizada que inserta un nuevo registro en la BD
     *
     * @param t el DTO a partir del cual se va a obtener la info para los parámetros de la SQL
     */
    protected abstract SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(T t);


    /**
     * La clase que extiende esta clase base debe implementar este método para que devuelva
     * un String con la consulta SQL parametrizada que inserta un nuevo registro en la tabla
     * de correspondiente de la Base de datos
     */
    protected abstract String getSQLInsert();


    //protected abstract void setDataTransferObjectID(T t, int id);

    protected abstract Consumer<T> getDataTransferObjectIDSetter(int id);

    @Override
    public boolean add(T t) {
        int id = JDBCQuery.insert(
                getConnection(),
                getSQLInsert(),
                getInsertParamSetter(t));
        if (id >= 0) {
            getDataTransferObjectIDSetter(id).accept(t);
            //setDataTransferObjectID(t, id);
            return true;
        }
        return false;
    }

    //*********** UPDATE ******************************************************

    protected abstract SQLThrowingConsumer<PreparedStatement> getUpdateParamSetter(T t);

    protected abstract String getSQLUpdate();

    @Override
    public boolean update(T t) {
        return JDBCQuery.update(
                getConnection(),
                getSQLUpdate(),
                getUpdateParamSetter(t));
    }


    // ************ DELETE ************************************
    protected abstract SQLThrowingConsumer<PreparedStatement> getDeleteParamSetter(T t);

    protected abstract String getSQLDelete();

    @Override
    public boolean delete(T t) {
        return JDBCQuery.update(
                getConnection(),
                getSQLDelete(),
                getDeleteParamSetter(t)
        );
    }

    protected abstract String getSQLSelectAll();

    @Override
    public Stream<T> getAll() {
        return JDBCQuery.query(
                getConnection(),
                getSQLSelectAll(),
                null,
                getDTOMapper());
    }

    protected abstract String getSQLCount();

    @Override
    public long getCount() {
        return JDBCQuery.queryScalar(
                getConnection(),
                getSQLCount(),
                null,
                Long.class)
                .longValue();
    }

    @Override
    public void close() {
        System.out.println("Cerrando el FunctionalDbDAOConnected...");
        JDBCUtil.close(this.currentConnection);
    }
}
