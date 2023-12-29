package dam.ad.jdbc.statements.personas;

/**
 * La interfaz SQLs encapsula los comandos SQL que utilizan las clases PersonaDA
 * para mediante JDBC ejecutar los comandos contra la base de datos
 * dando valores a los parámetros en las sentencias SQL parametrizadas antes de lanzar
 * el comando
 * Todas las sentencias SQL dependen del schema de la tabla persona en la base de datos
 */
public interface SQLs {
    String CREATE_TABLE_PERSONA = """
            CREATE TABLE IF NOT EXISTS persona(
                personaID INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY,
                nombre VARCHAR(20) NOT NULL,
                apellidos VARCHAR(30) NOT NULL,
                sexo CHAR(1) NOT NULL,
                nacimiento DATE,
                ingresos REAL)
                """;

    String DROP_TABLE_PERSONA = "DROP TABLE IF EXISTS persona";

    String SELECT_ALL_PERSONAS = "SELECT * FROM persona";
    String INSERT_PERSONA = "INSERT INTO persona VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    String SELECT_PERSONA_BY_ID = "SELECT * FROM persona WHERE personaID = ?";
    String UPDATE_PERSONA = """
            UPDATE persona
            SET nombre = ?, apellidos = ?, sexo = ?, nacimiento = ?, ingresos = ?
            WHERE personaID = ?
            """;
    String DELETE_PERSONA = "DELETE FROM persona WHERE personaID = ?";

    String SELECT_PERSONAS_BY_SEXO = "SELECT * FROM persona WHERE sexo  = ?";
    String SELECT_PERSONAS_BY_NACIMIENTO = "SELECT * FROM persona WHERE nacimiento > ?";
}
