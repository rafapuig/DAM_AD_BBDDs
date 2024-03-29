package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.DbDAO4;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.stream.SQLThrowingConsumer;
import dam.ad.model.personas.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.function.Supplier;

public class DbPersonaDAO4 extends DbDAO4<Persona> {

    DTOMapper<Persona> personaDTOMapper;

    public DbPersonaDAO4(Supplier<Connection> connectionSupplier) {
        super(connectionSupplier);
        System.out.println("Instanciando un DbPersonaDAO 4...");
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
    protected SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(Persona persona) {

        return getParamSetterFromParamList(
                List.of(
                        persona.getNombre(),
                        persona.getApellidos(),
                        persona.getSexo().getInicial(),
                        persona.getNacimiento(),
                        persona.getIngresos()
                )
        );

        /*return statement -> {
            /*statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellidos());
            statement.setString(3, persona.getSexo().getInicial());
            statement.setObject(4, persona.getNacimiento(), Types.DATE);
            statement.setDouble(5, persona.getIngresos());*/



       /*     statement.setObject(1,persona.getNombre());
            statement.setObject(2, persona.getApellidos());
            statement.setObject(3, persona.getSexo().getInicial());
            statement.setObject(4,persona.getNacimiento());
            statement.setObject(5, persona.getIngresos());
        };*/
    }

    @Override
    protected String getSQLInsert() {
        return "INSERT INTO persona VALUES (DEFAULT,?,?,?,?,?)";
    }

    @Override
    protected void setDataTransferObjectID(Persona persona, int id) {
        persona.setPersonaId(id);
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getUpdateParamSetter(Persona persona) {
        return getInsertParamSetter(persona).andThen(
                preparedStatement ->
                        preparedStatement.setInt(6, persona.getPersonaId()));
    }

    @Override
    protected String getSQLUpdate() {
        return "UPDATE persona SET nombre = ?, apellidos = ?, sexo=?, NACIMIENTO=?, INGRESOS=? WHERE personaId = ?";
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getDeleteParamSetter(Persona persona) {
        return preparedStatement -> preparedStatement.setInt(1, persona.getPersonaId());
    }

    @Override
    protected String getSQLDelete() {
        return "DELETE FROM persona WHERE personaId = ?";
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
