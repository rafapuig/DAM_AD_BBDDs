package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;

import javax.sql.DataSource;

public class DbFutbolDAOFactory implements DAOFactory {
    DataSource dataSource;
    public DbFutbolDAOFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <DTO> DAO<DTO> createDAO(Class<DTO> dtoClass) {
        return switch (dtoClass.getSimpleName()) {
            case "Equipo" -> (DAO<DTO>) new DbEquipoDAO(dataSource);
            case "Jugador" -> (DAO<DTO>) new DbJugadorDAO(dataSource);
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + dtoClass.getSimpleName());
        };
    }
}
