package dam.ad.jdbc.statements.personas;

public interface SQLs {
    public static String CREATE_TABLE_PERSONA = """
            CREATE TABLE IF NOT EXISTS persona(
                personaID INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY,
                nombre VARCHAR(20) NOT NULL,
                apellidos VARCHAR(30) NOT NULL,
                sexo CHAR(1) NOT NULL,
                nacimiento DATE,
                ingresos REAL)
                """;

    public static String INSERT_PERSONA = """
            INSERT INTO persona VALUES (DEFAULT, ?, ?, ?, ?, ?)
            """;

    public static String DELETE_PERSONA = """
            DELETE FROM persona WHERE personaID = ?
            """;

    public static String SELECT_ALL_PERSONAS = """
            SELECT * FROM persona
            """;

    public static String SELECT_PERSONAS_BY_SEXO = """
            SELECT * FROM persona WHERE sexo  = ?
            """;

    public static String SELECT_PERSONAS_BY_NACIMIENTO = """
            SELECT * FROM persona WHERE nacimiento > ?
            """;

}
