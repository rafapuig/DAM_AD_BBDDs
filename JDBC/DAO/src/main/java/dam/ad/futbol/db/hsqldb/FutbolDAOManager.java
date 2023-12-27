package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;

public class FutbolDAOManager {
    DAOFactory factory;
    DAO<Equipo> equipoDAO;
    DAO<Jugador> jugadorDAO;

    public FutbolDAOManager(DAOFactory factory) {
        this.factory = factory;
        equipoDAO = factory.createDAO(Equipo.class);
        jugadorDAO = factory.createDAO(Jugador.class);
    }

    public DAO<Equipo> getEquipoDAO() {
        return equipoDAO;
    }

    public DAO<Jugador> getJugadorDAO() {
        return jugadorDAO;
    }


}
