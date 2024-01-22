package dam.ad.dao;

import java.util.Optional;
import java.util.stream.Stream;

public interface DAO<T> extends AutoCloseable {

    /**
     * Recupera del origen de datos un DTO del modelo de datos a partir de un identificador
     * que identifica de manera unívoca a un solo elemento dentro del conjunto
     * Dependiendo del valor proporcionado como ID se puede devolver un Optional vacío
     * o uno que contenga el objeto DTO identificado con la ID proporcionada
     * @param id identificador del elemento DTO dentro del conjunto (su clave)
     * @return un Optional que contiene el DTO o vacío
     */
    Optional<T> getById(int id);

    /**
     * Añade un elemento de tipo T (el DTO) a la fuente de datos
     * @param t referencia al objeto DTO que se va a añadir a los datos existentes
     * @return true si se ha podido añadir con éxito el nuevo elemento DTO al conjunto de datos
     */
    boolean add(T t);

    //boolean add(Collection<T> collection);

    boolean update(T t);

    boolean delete(T t);

    Stream<T> getAll();

    default long getCount() {
        return getAll().count();
    }

    @Override
    default void close() { }
}
