package dam.ad.dao.jdbc;

import dam.ad.jdbc.query.DTOMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class GenericDTOMapper<T> implements DTOMapper<T> {
    Class<T> type;
    Field[] fields;

    public GenericDTOMapper(Class<T> type) {
        this.type = type;
        fields = type.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
    }

    @Override
    public T apply(ResultSet resultSet) throws SQLException {

        Object[] values = new Object[resultSet.getMetaData().getColumnCount()];

        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            values[i] = resultSet.getObject(i+1);
        }

        try {
            Constructor<T> constructor = type.getDeclaredConstructor(
                    Arrays.stream(type.getDeclaredFields())
                            .map(Field::getType).toArray(Class[]::new));

            return constructor.newInstance(values);

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
