package dam.ad.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface DAO<T> {
    Optional<T> getById(int id);

    boolean add(T t);

    //boolean add(Collection<T> collection);

    boolean update(T t);

    boolean delete(T t);

    Stream<T> getAll();

    default long getCount() {
        return getAll().count();
    }

}
