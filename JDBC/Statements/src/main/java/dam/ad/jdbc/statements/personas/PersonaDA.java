package dam.ad.jdbc.statements.personas;

import java.sql.*;
import java.time.LocalDate;

public class PersonaDA {

    protected Connection connection;

    public PersonaDA(Connection connection) {
        this.connection = connection;
    }

    protected void execute(String commandSQL) {
        try (Statement stmt = this.connection.createStatement()) {

            try {
                stmt.execute(commandSQL);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR creando el comando", e);
        }
    }

    public void createTablePersona() {

        try {
            execute(SQLs.CREATE_TABLE_PERSONA);

        } catch (RuntimeException e) {
            throw new RuntimeException("ERROR creando la tabla Persona", e);
        }
    }

    public boolean insertPersona(String nombre, String apellidos, String sexo, LocalDate nacimiento, float ingresos) {

        try (PreparedStatement stmt = connection.prepareStatement(SQLs.INSERT_PERSONA)) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, sexo);
            stmt.setDate(4, Date.valueOf(nacimiento));
            stmt.setDouble(5, ingresos);

            int result = stmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + SQLs.INSERT_PERSONA, e);
        }
    }

    public boolean deletePersona(int personaId) {

        try (PreparedStatement stmt = connection.prepareStatement(SQLs.DELETE_PERSONA)) {

            stmt.setInt(1, personaId);

            int result = stmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + SQLs.DELETE_PERSONA, e);
        }
    }

    public boolean subirSalarioPersona(int personaId, float aumento) {
        return false;
    }

    public ResultSet getAllPersonas() {

        try (PreparedStatement stmt = connection.prepareStatement(SQLs.SELECT_ALL_PERSONAS)) {
            try {
                return stmt.executeQuery();

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + SQLs.SELECT_ALL_PERSONAS, e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + SQLs.SELECT_ALL_PERSONAS, e);
        }
    }

    public ResultSet getPersonasBySexo(String sexo) {

        try (PreparedStatement stmt = connection.prepareStatement(SQLs.SELECT_PERSONAS_BY_SEXO)) {

            stmt.setString(1, sexo);

            try {
                return stmt.executeQuery();

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + SQLs.SELECT_PERSONAS_BY_SEXO, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + SQLs.SELECT_PERSONAS_BY_SEXO, e);
        }
    }

    public ResultSet getPersonasNacidasAfter(LocalDate date) {

        try (PreparedStatement stmt = connection.prepareStatement(SQLs.SELECT_PERSONAS_BY_NACIMIENTO)) {

            stmt.setDate(1, Date.valueOf(date));

            try {
                return stmt.executeQuery();

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + SQLs.SELECT_PERSONAS_BY_NACIMIENTO, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + SQLs.SELECT_PERSONAS_BY_NACIMIENTO, e);
        }
    }

}
