package dam.ad.jdbc.query;


import dam.ad.jdbc.stream.ThrowingFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DTOMapper está pensado para generar una instancia del DTO a partir de un ResultSet
 * con el cursor posicionado en el registro (fila) de la cual vamos a leer los campos
 * para asignar a los atributos del DTO
 * @param <T>
 */
@FunctionalInterface
public interface DTOMapper<T> extends ThrowingFunction<ResultSet, T, SQLException> {
    @Override
    T apply(ResultSet resultSet) throws SQLException;
}
