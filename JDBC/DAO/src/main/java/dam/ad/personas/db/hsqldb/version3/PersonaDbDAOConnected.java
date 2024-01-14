package dam.ad.personas.db.hsqldb.version3;

import dam.ad.dao.jdbc.FunctionalDbDAOConnected;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.stream.SQLThrowingConsumer;
import dam.ad.model.personas.Persona;
import dam.ad.personas.db.hsqldb.PersonaDTOMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.function.Consumer;

public class PersonaDbDAOConnected extends FunctionalDbDAOConnected<Persona> {

    DTOMapper<Persona> personaDTOMapper;

    public PersonaDbDAOConnected(DataSource dataSource) {
        super(dataSource);
        this.personaDTOMapper = new PersonaDTOMapper();
    }

    @Override
    protected DTOMapper<Persona> getDTOMapper() {
        return this.personaDTOMapper;
    }

    @Override
    protected String getSQLSelectByID() {
        return "SELECT * FROM persona WHERE personaId = ?";
    }

    @Override
    protected String getSQLInsert() {
        return "INSERT INTO persona VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(Persona persona) {
        return statement -> {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellidos());
            statement.setString(3, persona.getSexo().getInicial());
            statement.setObject(4, persona.getNacimiento(), Types.DATE);
            statement.setDouble(5, persona.getIngresos());

            /*statement.setObject(1,persona.getNombre());
            statement.setObject(2, persona.getApellidos());
            statement.setObject(3, persona.getSexo().getInicial());
            statement.setObject(4,persona.getNacimiento());
            statement.setObject(5, persona.getIngresos());*/
        };
    }


    /*@Override
    protected void setDataTransferObjectID(Persona persona, int id) {
        persona.setPersonaId(id);
    }*/

    @Override
    protected Consumer<Persona> getDataTransferObjectIDSetter(int id) {
        return persona -> persona.setPersonaId(id);
    }

    @Override
    protected String getSQLUpdate() {
        return """
                  UPDATE persona
                  SET nombre = ?, apellidos = ?, sexo = ?, nacimiento = ?, ingresos = ?
                  WHERE personaId = ?""";
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getUpdateParamSetter(Persona persona) {
        return getInsertParamSetter(persona).andThen(
                preparedStatement ->
                        preparedStatement.setInt(6, persona.getPersonaId()));
    }


    @Override
    protected String getSQLDelete() {
        return "DELETE FROM persona WHERE personaId = ?";
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getDeleteParamSetter(Persona persona) {
        return preparedStatement -> preparedStatement.setInt(1, persona.getPersonaId());
    }


    @Override
    protected String getSQLSelectAll() {
        return "SELECT * FROM persona";
    }

    @Override
    protected String getSQLCount() {
        return "SELECT COUNT(*) FROM persona";
    }

}
