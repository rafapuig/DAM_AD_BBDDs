package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.DbDAO;
import dam.ad.personas.model.Persona;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

public class DbPersonaDAO extends DbDAO<Persona> {

    public DbPersonaDAO(DataSource dataSource) {
        super(dataSource);
        initSQLs();
    }
    /*public DbPersonaDAO(DataSource dataSource, boolean keepConnected) {
        super(dataSource, keepConnected);
        initSQLs();
    }*/

    private void initSQLs() {
        SQL_SELECT_ALL = "SELECT * FROM persona";
        SQL_SELECT_BY_ID = "SELECT * FROM persona WHERE personaId = ?";
        SQL_INSERT = "INSERT INTO persona VALUES (DEFAULT,?,?,?,?,?)";
        SQL_UPDATE = "UPDATE persona SET nombre = ?, apellidos = ?, sexo=?, NACIMIENTO=?, INGRESOS=? WHERE personaId = ?";
        SQL_DELETE = "DELETE FROM persona WHERE PERSONAID = ? ";
    }

    @Override
    protected Persona createDataTransferObject(ResultSet resultSet) throws SQLException {
        return new Persona(
                resultSet.getInt("personaId"),
                resultSet.getString("nombre"),
                resultSet.getString("apellidos"),
                Persona.Sexo.fromInicial(resultSet.getString("sexo")),
                resultSet.getObject("nacimiento", LocalDate.class),
                resultSet.getDouble("ingresos")
        );
    }

    @Override
    protected void setAddStatementParams(PreparedStatement statement, Persona persona) throws SQLException {
        statement.setString(1, persona.getNombre());
        statement.setString(2, persona.getApellidos());
        statement.setString(3, persona.getSexo().getInicial());
        statement.setObject(4, persona.getNacimiento(), Types.DATE);
        statement.setDouble(5, persona.getIngresos());
    }

    @Override
    protected void setTransferObjectID(Persona persona, int id) {
        persona.setPersonaId(id);
    }

    @Override
    protected void setUpdateParams(PreparedStatement statement, Persona persona) throws SQLException {
        statement.setString(1, persona.getNombre());
        statement.setString(2, persona.getApellidos());
        statement.setString(3, persona.getSexo().getInicial());
        statement.setObject(4, persona.getNacimiento(), Types.DATE);
        statement.setDouble(5, persona.getIngresos());
        statement.setInt(6, persona.getPersonaId());
    }

    protected void setDeleteParams(PreparedStatement statement, Persona persona) throws SQLException {
        statement.setInt(1, persona.getPersonaId());
    }

}