package dam.ad.futbol.file;

import dam.ad.dao.DAO;
import dam.ad.futbol.model.Equipo;

import java.util.Optional;
import java.util.stream.Stream;

public class FileEquipoDAO implements DAO<Equipo> {

    EquipoDTOReader reader = new EquipoDTOReader();


    @Override
    public Optional<Equipo> getById(int id) {
        return this.getAll()
                .filter(equipo -> equipo.getEquipoId() == id)
                .findAny();
    }

    @Override
    public boolean add(Equipo equipo) {
        return false;
    }

    @Override
    public boolean update(Equipo equipo) {
        return false;
    }

    @Override
    public boolean delete(Equipo equipo) {
        return false;
    }

    @Override
    public Stream<Equipo> getAll() {
        return reader.read();
    }
}
