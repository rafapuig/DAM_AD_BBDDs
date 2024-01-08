package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.dao.jdbc.AbstractDAOManager;
import dam.ad.dao.jdbc.DAOManager;
import dam.ad.dao.jdbc.DataSourceFactory;
import dam.ad.dao.jdbc.DatabaseSchema;
import dam.ad.jdbc.JDBCUtil;
import dam.ad.jdbc.query.DTOMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class PersonasDAOManager extends AbstractDAOManager {

    PersonasDAOManager(String url) throws Exception {
        //dataSource = DataSourceFactory.getInstance().createDataSource(url);
        dataSource = PersonasDataSourceFactory.createDataSource(url);
        daoFactory = new PersonasDAOFactory(this);
        dbSchema = new PersonasDatabaseSchema();
    }

    @Override
    public <T> DTOMapper<T> getDTOMapper(Class<T> tClass) {
        return switch (tClass.getSimpleName()) {
            case "Persona" -> (DTOMapper<T>) PersonaDTOMapper.getInstance();
            default -> throw new IllegalStateException("Unexpected value: " + tClass.getSimpleName());
        };
    }
}
