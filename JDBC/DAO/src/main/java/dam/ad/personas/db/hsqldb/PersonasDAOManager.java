package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAOFactory;
import dam.ad.dao.jdbc.AbstractDAOManager;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.model.personas.Persona;

import java.util.Map;
import java.util.function.Supplier;

public class PersonasDAOManager extends AbstractDAOManager {
    Map<Class<?>, Supplier<DTOMapper<?>>> DTOMappersMap;

    PersonasDAOManager(String url) throws Exception {
        //dataSource = DataSourceFactory.getInstance().createDataSource(url);
        dataSource = DataSourceFactory.createDataSource(url);
        daoFactory = new PersonasDAOFactory(this);
        dbSchema = new PersonasDatabaseSchema();

        DTOMappersMap = Map.of(Persona.class, PersonaDTOMapper::getInstance);
    }

    @Override
    protected Map<Class<?>, Supplier<DTOMapper<?>>> getDTOMappersMap() {
        return DTOMappersMap;
    }

}
