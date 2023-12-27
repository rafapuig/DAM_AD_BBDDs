package dam.ad.jdbc.stream.generation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ResultSetLazySpliterator<T> extends Spliterators.AbstractSpliterator<T> {
    ResultSet resultSet;
    ThrowingFunction<ResultSet, T, SQLException> dtoMapper;

    protected ResultSetLazySpliterator(
            ResultSet resultSet, ThrowingFunction<ResultSet, T, SQLException> dtoMapper) {

        super(
                Long.MAX_VALUE,     //Al no saber el tamaño pasamos Long.MAX_VALUE
                Spliterator.ORDERED);   //Aseguramos que se va recorriendo en orden

        this.resultSet = resultSet;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        try {
            //Si al avanzar el cursor al siguiente registro no hay registro
            // Devolvemos false para indicar que ya no existe elemento al que avanzar
            if (!resultSet.next()) return false;

            //Obtenemos el elemento T a partir de la posición actual del cursor en el ResultSet
            // Mediante el DTOMapper
            T elem = getElement();

            //Consumimos el elemento T
            action.accept(elem);

            //Devolvemos true para indicar que hemos podido avanzar y consumir un elemento
            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Cannot advance spliterator", e);
        }
    }

    private T getElement() {
        try {
            return dtoMapper.apply(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("ERROR mapeando el registro con el DTO-Mapper", e);
        }
    }
}
