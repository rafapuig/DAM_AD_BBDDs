package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.dao.jdbc.AbstractDAOFactory;
import dam.ad.dao.jdbc.DAOManager;
import dam.ad.model.personas.Persona;

import java.util.Map;
import java.util.function.Supplier;


public class PersonasDAOFactory extends AbstractDAOFactory {
    DAOManager daoManager;

    PersonasDAOFactory(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    protected Map<Class<?>, Supplier<DAO<?>>> getDAOSuppliersMap() {
        return Map.of(Persona.class, () -> new DbPersonaDAO3(daoManager));
    }

}
