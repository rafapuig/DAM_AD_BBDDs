package dam.ad.dao.jdbc;

import dam.ad.jdbc.query.DTOMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public interface DAOManager {

    DataSource getDataSource();

    Connection getConnection();

    void beginTransaction();

    void commit();

    void rollback();

    <T> DTOMapper<T> getDTOMapper(Class<T> tClass);

    <T> Stream<T> query(String SQL, DTOMapper<T> dtoMapper, Object... params);

    default <T> Stream<T> query(String SQL, Class<T> tClass, Object... params) {
        return query(SQL, getDTOMapper(tClass), params);
    }
    default Stream<List<Object>> query(String SQL, Object... params) {
        return query(SQL, GenericDTOMapper.getInstance(), params);
    }


}
