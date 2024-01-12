package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.DAOManager;
import dam.ad.dao.jdbc.DbDAO3;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.stream.SQLThrowingConsumer;
import dam.ad.model.personas.Persona;

import java.sql.PreparedStatement;

public class DbPersonaDAO3 extends DbDAO3<Persona> {
    DTOMapper<Persona> personaDTOMapper;

    public DbPersonaDAO3(DAOManager daoManager) {
        super(daoManager);
        System.out.println("Instanciando un DbPersonaDAO 3...");
        this.personaDTOMapper = daoManager.getDTOMapper(Persona.class);
    }

    @Override
    protected DTOMapper<Persona> getDTOMapper() {
        return this.personaDTOMapper;
    }

    @Override
    protected String getSQLSelectByID() {
        return PersonaSQL.SELECT_PERSONA_BY_ID;
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(Persona persona) {

        return JDBCQuery.createParamSetterFrom(
                persona.getNombre(),
                persona.getApellidos(),
                persona.getSexo().getInicial(),
                persona.getNacimiento(),
                persona.getIngresos()
        );

        /* return getParamSetterFromParamList(
                List.of(
                        persona.getNombre(),
                        persona.getApellidos(),
                        persona.getSexo().getInicial(),
                        persona.getNacimiento(),
                        persona.getIngresos()
                )
        );*/

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
        return PersonaSQL.INSERT_PERSONA;
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
        return PersonaSQL.UPDATE_PERSONA;
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getDeleteParamSetter(Persona persona) {
        return preparedStatement -> preparedStatement.setInt(1, persona.getPersonaId());
    }

    @Override
    protected String getSQLDelete() {
        return PersonaSQL.DELETE_PERSONA;
    }

    @Override
    protected String getSQLSelectAll() {
        return PersonaSQL.SELECT_ALL_PERSONAS;
    }

    @Override
    protected String getSQLCount() {
        return PersonaSQL.SELECT_COUNT_ALL_PERSONAS;
    }

}
