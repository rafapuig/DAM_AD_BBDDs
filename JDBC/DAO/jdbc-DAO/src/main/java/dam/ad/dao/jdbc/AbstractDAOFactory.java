package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;

import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractDAOFactory implements DAOFactory {
    Map<Class<?>, Supplier<DAO<?>>> daoSuppliersMap;
    protected abstract Map<Class<?>, Supplier<DAO<?>>> getDAOSuppliersMap();

    public AbstractDAOFactory() {
        this.daoSuppliersMap = getDAOSuppliersMap();
    }

    @Override
    public <DTO> DAO<DTO> createDAO(Class<DTO> dtoClass) {
        System.out.println("Creando un DAO de " + dtoClass.getSimpleName() + "...");

        DAO<?> dao = daoSuppliersMap.get(dtoClass).get();
        return (DAO<DTO>) dao;
    }

}
