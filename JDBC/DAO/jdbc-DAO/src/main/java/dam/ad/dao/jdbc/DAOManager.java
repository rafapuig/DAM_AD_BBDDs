package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.jdbc.query.DTOMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface DAOManager {

    DataSource getDataSource();

    Connection getConnection();

    void beginTransaction();

    void commit();

    void rollback();

    DAOFactory getDAOFactory();

    default <DTO> DAO<DTO> createDAO(Class<DTO> dtoClass) {
        return getDAOFactory().createDAO(dtoClass);
    }



    <T> DTOMapper<T> getDTOMapper(Class<T> tClass);

    <T> Stream<T> query(String SQL, DTOMapper<T> dtoMapper, Object... params);

    default <T> Stream<T> query(String SQL, Class<T> tClass, Object... params) {
        return query(SQL, getDTOMapper(tClass), params);
    }
    default Stream<List<Object>> query(String SQL, Object... params) {
        return query(SQL, BasicDTOMapper.getInstance(), params);
    }

    default <T> Optional<T> querySingleResult(String SQL, Class<T> tClass, Object... params) {
        return query(SQL, tClass, params).findFirst();
    }

    default <T> Optional<T> queryScalar(String SQL, Class<T> scalarClass, Object... params) {
        return querySingleColumn(SQL,scalarClass, params).findFirst();
    }

    default <T> Stream<T> querySingleColumn(String SQL, Class<T> tClass, Object... params) {
        return query(SQL,
                resultSet -> resultSet.getObject(1, tClass),
                params);
    }

}
