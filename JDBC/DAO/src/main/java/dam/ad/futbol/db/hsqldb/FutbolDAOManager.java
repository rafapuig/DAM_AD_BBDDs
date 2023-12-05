package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.futbol.model.Equipo;

import javax.sql.DataSource;

public class FutbolDAOManager {

    static <DTO> DAO<DTO> getDAO(Class<DTO> dtoClass, DataSource dataSource) {
        return switch (dtoClass.getSimpleName()) {
            case "Equipo" -> (DAO<DTO>) new DbEquipoDAO(dataSource);
            case "Jugador" -> (DAO<DTO>) new DbJugadorDAO(dataSource);
            default -> throw new IllegalStateException("Unexpected value: " + dtoClass.getSimpleName());
        };
    }
}
