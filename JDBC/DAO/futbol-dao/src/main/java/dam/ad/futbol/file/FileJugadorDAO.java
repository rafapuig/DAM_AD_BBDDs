package dam.ad.futbol.file;

import dam.ad.dao.DAO;
import dam.ad.file.DTOReader;
import dam.ad.model.futbol.Jugador;

import java.util.Optional;
import java.util.stream.Stream;

public class FileJugadorDAO implements DAO<Jugador> {

    DTOReader<Jugador> dtoReader = new JugadorDTOReader();

    @Override
    public Optional<Jugador> getById(int id) {
        return this.getAll()
                .filter(jugador -> jugador.getJugadorId() == id)
                .findAny();
    }

    @Override
    public boolean add(Jugador jugador) {
        return false;
    }

    @Override
    public boolean update(Jugador jugador) {
        return false;
    }

    @Override
    public boolean delete(Jugador jugador) {
        return false;
    }

    @Override
    public Stream<Jugador> getAll() {
        return dtoReader.read();
    }
}
