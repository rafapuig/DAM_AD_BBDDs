package dam.ad.dao;

public interface DAOFactory {
    <DTO> DAO<DTO> createDAO(Class<DTO> dtoClass);
}
