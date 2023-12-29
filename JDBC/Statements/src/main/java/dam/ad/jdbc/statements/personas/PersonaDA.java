package dam.ad.jdbc.statements.personas;

import java.sql.*;
import java.time.LocalDate;

/**
 * PersonaDA actúa como componente de acceso a datos (Data Access)
 * Mediante JDBC interacciona con una base de datos relacional
 * a través de una conexión previamente establecida y que le es inyectada
 * en el constructor
 */
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

    /**
     * Crea la tabla Persona en la base de datos si no existe dicha tabla
     */
    public void createTablePersona() {

        try {
            execute(SQLs.CREATE_TABLE_PERSONA);

        } catch (RuntimeException e) {
            throw new RuntimeException("ERROR creando la tabla Persona", e);
        }
    }

    /**
     * Método para insertar una nueva fila en la tabla persona
     * Los valores para asignar a los parámetros de la consulta se proporcionan como parámetros
     * de entrada del método
     *
     * @return Se devuelve true si se ha podido insertar con exito el registro o fila en la tabla
     */
    public boolean insertPersona(String nombre, String apellidos, String sexo, LocalDate nacimiento, float ingresos) {

        //Necesitamos un PreparedStatement porque la SQL es paramétrica:
        // INSERT INTO persona VALUES (DEFAULT, ?, ?, ?, ?, ?)
        // Como la variable que contiene la referencia la colocamos dentro de los
        // paréntesis del try-with-resources no será necesario llamar al close
        // del statement en un bloque finally
        try (PreparedStatement stmt = connection.prepareStatement(SQLs.INSERT_PERSONA)) {

            // Establecemos los valores de los 5 parámetros del comando SQL
            // Cada parámetro se identifica por la posición que ocupa el ? en el comando SQL
            // mediante un índice (que empieza por 1)
            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, sexo);
            //
            stmt.setDate(4, Date.valueOf(nacimiento));
            stmt.setDouble(5, ingresos);

            try {
                // executeUpdate devuelve el numero de filas que se han insertado, modificada o eliminado
                int result = stmt.executeUpdate();

                return result > 0;  //Por tanto, si es mayor que cero devolvemos true

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL: " + SQLs.INSERT_PERSONA, e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + SQLs.INSERT_PERSONA, e);
        }
    }

    /**
     * Método para eliminar una persona identificada mediante su valor personaID pasado
     * como parámetro, ya que es la clave primaria de la tabla persona
     */
    public boolean deletePersona(int personaId) {

        try (PreparedStatement stmt = connection.prepareStatement(SQLs.DELETE_PERSONA)) {

            stmt.setInt(1, personaId);

            try {
                int result = stmt.executeUpdate();

                return result > 0;

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL: " + SQLs.DELETE_PERSONA, e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + SQLs.DELETE_PERSONA, e);
        }
    }


    /**
     * Método para cambiar los valores de los campos de una fila de la tabla persona
     * La fila es identificada a partir de la clave primaria, en este caso personaID
     */
    public boolean updatePersona(int personaId, String nombre, String apellidos, String sexo, LocalDate nacimiento, float ingresos) {

        try (PreparedStatement stmt = connection.prepareStatement(SQLs.UPDATE_PERSONA)) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, sexo);
            stmt.setDate(4, Date.valueOf(nacimiento));
            stmt.setDouble(5, ingresos);
            stmt.setInt(6, personaId);

            try {
                int result = stmt.executeUpdate();

                return result > 0;

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL: " +
                                           SQLs.UPDATE_PERSONA, e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " +
                                       SQLs.UPDATE_PERSONA, e);
        }
    }

    public boolean subirSalarioPersona(int personaId, float aumento) {
        return false;
    }

    // Los siguientes métodos devuelven un ResultSet al llamador
    // En la práctica real esto no es lo más conveniente
    // Lo normal es consumir el ResultSet en el propio método del componente de Acceso a datos

    /**
     * Obtiene un ResultSet que debería tener como mucho una fila
     * Ya que la consulta SQL está filtrando mediante la clave primaria
     */
    public ResultSet getPersona(int personaID) {
        try (PreparedStatement stmt = connection.prepareStatement(SQLs.SELECT_PERSONA_BY_ID)) {

            stmt.setInt(1, personaID);

            try {
                return stmt.executeQuery();

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" +
                                           SQLs.SELECT_PERSONA_BY_ID, e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " +
                                       SQLs.SELECT_PERSONA_BY_ID, e);
        }
    }

    /**
     * Obtiene un ResultSet con todas las filas de la tabla persona
     */
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
                throw new RuntimeException("ERROR ejecutando el comando SQL:" +
                                           SQLs.SELECT_PERSONAS_BY_NACIMIENTO, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " +
                                       SQLs.SELECT_PERSONAS_BY_NACIMIENTO, e);
        }
    }

}
