package dam.ad.futbol.db.hsqldb.version2;

import dam.ad.dao.jdbc.AbstractDAOManager;
import dam.ad.futbol.db.hsqldb.FutbolSchema;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;
import dam.ad.personas.db.hsqldb.DataSourceFactory;

import java.util.Map;
import java.util.function.Supplier;

public class FutbolDAOManager extends AbstractDAOManager {
    FutbolDAOManager(String url) throws Exception {
        dataSource = DataSourceFactory.createDataSource(url);
        daoFactory = new FutbolDAOFactory(this);
        dbSchema = new FutbolSchema();
    }
    @Override
    protected Map<Class<?>, Supplier<DTOMapper<?>>> getDTOMappersMap() {
        return Map.of(
                Equipo.class, EquipoDTOMapper::new,
                Jugador.class, JugadorDTOMapper::new);
    }
}
