package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.DAOManager;
import dam.ad.model.personas.Persona;

import java.util.Map;
import java.util.function.Supplier;

public class PersonasDAO4Factory extends PersonasDAOFactory {

    PersonasDAO4Factory(DAOManager daoManager) {
        super(daoManager);
    }

    @Override
    protected Map<Class<?>, Supplier<DAO<?>>> getDAOSuppliersMap() {
        return Map.of(
                Persona.class,
                () -> new DbPersonaDAO4(() -> daoManager.getConnection()));
    }

}
