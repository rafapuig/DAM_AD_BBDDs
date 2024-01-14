package dam.ad.futbol.db.hsqldb.version2;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.dao.jdbc.AbstractDAOFactory;
import dam.ad.dao.jdbc.DAOManager;
import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;

import java.sql.Connection;
import java.util.Map;
import java.util.function.Supplier;

public class FutbolDAOFactory extends AbstractDAOFactory {
    DAOManager daoManager;
    public FutbolDAOFactory(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    Supplier<Connection> getConnectionSupplier() {
        return () -> daoManager.getConnection();
    }

    @Override
    protected Map<Class<?>, Supplier<DAO<?>>> getDAOSuppliersMap() {
        return Map.of(
                Equipo.class, () -> new DbEquipoDAO(getConnectionSupplier()),
                Jugador.class, () -> new DbJugadorDAO(getConnectionSupplier()));
    }
}
