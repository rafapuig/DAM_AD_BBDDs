package dam.ad.jpa.intro.dao;

import dam.ad.jpa.intro.entities.Empleado;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.stream.Stream;

public class EmpleadoDAO {
    EntityManager manager;

    public EmpleadoDAO(EntityManager manager) {
        this.manager = manager;
    }

    public synchronized Optional<Empleado> getById(int id) {
        Empleado empleado = manager.find(Empleado.class, id);
        return Optional.ofNullable(empleado);
    }

    public synchronized boolean add(Empleado empleado) {
        manager.persist(empleado);
        return true;
    }

    public synchronized boolean update(Empleado empleado) {
        //Empleado e = manager.find(Empleado.class, empleado.getId());
        manager.merge(empleado);
        return true;
    }

    public synchronized boolean delete(Empleado empleado) {
        Optional<Empleado> e = getById(empleado.getId());
        if(e.isPresent()) {
            manager.remove(e.get());
            return true;
        } else {
            return false;
        }
    }

    public synchronized Stream<Empleado> getAll() {
        return manager.createQuery("SELECT e FROM Empleado e", Empleado.class)
                .getResultStream();
    }

    public synchronized long getCount() {
        return manager.createQuery("SELECT count(e) FROM Empleado e", Long.class)
                .getSingleResult();
    }


}
