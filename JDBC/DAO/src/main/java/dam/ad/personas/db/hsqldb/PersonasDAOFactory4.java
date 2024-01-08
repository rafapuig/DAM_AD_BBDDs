package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.dao.jdbc.DAOManager;

public class PersonasDAOFactory4 implements DAOFactory {
    DAOManager daoManager;
    PersonasDAOFactory4(DAOManager daoManager) {
        this.daoManager = daoManager;
    }
    @Override
    public <DTO> DAO<DTO> createDAO(Class<DTO> dtoClass) {
        System.out.println("Creando un DAO de " + dtoClass.getSimpleName() + "...");
        return switch (dtoClass.getSimpleName()) {
            case "Persona" -> (DAO<DTO>) new DbPersonaDAO4(() -> this.daoManager.getConnection());
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + dtoClass.getSimpleName());
        };
    }
}
