package dam.ad.jdbc.dao.hsqldb;

import dam.ad.dao.jdbc.DbDAOConnected;
import dam.ad.personas.model.Persona;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

public class DbPersonaDAOConnected extends DbDAOConnected<Persona> {

    /* protected DbPersonaDAOConnected(Connection connection) {
        super(connection);
        SQL_SELECT_ALL = "SELECT * FROM persona";
        SQL_SELECT_BY_ID = "SELECT * FROM persona WHERE personaId = ?";
        SQL_INSERT = "INSERT INTO persona VALUES (DEFAULT,?,?,?,?,?)";
        SQL_UPDATE = "UPDATE persona SET nombre = ?, apellidos = ?, sexo=?, NACIMIENTO=?, INGRESOS=? WHERE personaId = ?";
        SQL_DELETE = "DELETE FROM persona WHERE PERSONAID = ? ";
    } */

    protected DbPersonaDAOConnected(DataSource dataSource) {
        super(dataSource);
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
    protected void setAddStatementParams(PreparedStatement stmt, Persona persona) throws SQLException {
        stmt.setString(1, persona.getNombre());
        stmt.setString(2, persona.getApellidos());
        stmt.setString(3, persona.getSexo().getInicial());
        stmt.setObject(4, persona.getNacimiento(), Types.DATE);
        stmt.setDouble(5, persona.getIngresos());
    }

    @Override
    protected void setTransferObjectID(Persona persona, int id) {
        persona.setPersonaId(id);
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Persona persona) throws SQLException {
        stmt.setString(1, persona.getNombre());
        stmt.setString(2, persona.getApellidos());
        stmt.setString(3, persona.getSexo().getInicial());
        stmt.setObject(4, persona.getNacimiento(), Types.DATE);
        stmt.setDouble(5, persona.getIngresos());
        stmt.setInt(6, persona.getPersonaId());
    }

    protected void setDeleteParams(PreparedStatement stmt, Persona persona) throws SQLException {
        stmt.setInt(1, persona.getPersonaId());
    }

}
