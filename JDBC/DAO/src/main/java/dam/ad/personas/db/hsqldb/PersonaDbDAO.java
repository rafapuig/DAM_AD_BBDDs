package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.TemplateDbDAO;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

public class PersonaDbDAO extends TemplateDbDAO<Persona> {

    public PersonaDbDAO(DataSource dataSource) {
        super(dataSource);
        initSQLs();
    }

    private void initSQLs() {
        SQL_SELECT_ALL = "SELECT * FROM persona";
        SQL_SELECT_BY_ID = "SELECT * FROM persona WHERE personaId = ?";
        SQL_INSERT = "INSERT INTO persona VALUES (DEFAULT,?,?,?,?,?)";
        SQL_UPDATE = "UPDATE persona SET nombre = ?, apellidos = ?, sexo=?, nacimiento=?, ingresos=? WHERE personaId = ?";
        SQL_DELETE = "DELETE FROM persona WHERE personaId = ?";
        SQL_SELECT_COUNT = "SELECT COUNT(*) FROM persona";
    }

    @Override
    protected Persona createDataTransferObject(ResultSet resultSet) throws SQLException {
        return new Persona(
                resultSet.getInt("personaId"),
                resultSet.getString("nombre"),
                resultSet.getString("apellidos"),
                Sexo.fromInicial(resultSet.getString("sexo")),
                resultSet.getObject("nacimiento", LocalDate.class),
                resultSet.getFloat("ingresos")
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
    protected void setDataTransferObjectID(Persona persona, int id) {
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
