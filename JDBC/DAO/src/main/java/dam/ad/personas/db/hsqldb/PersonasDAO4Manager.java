package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.AbstractDAOManager;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.model.personas.Persona;

import java.util.Map;
import java.util.function.Supplier;

/**
 * En esta versión del PersonasDAOManager
 * asignamos el campo daoFactory a una instancia de PersonasDA4Factory
 * para que nos fabrique PersonasDAO4 como DAO<Persona>
 */
public class PersonasDAO4Manager extends PersonasDAOManager {

    PersonasDAO4Manager(String url) throws Exception {
        super(url);
        daoFactory = new PersonasDAO4Factory(this);
    }

}
