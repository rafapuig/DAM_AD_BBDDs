package dam.ad.futbol.db.hsqldb.version2;

/**
 * La interfaz SQLs encapsula los comandos SQL que utilizan las clases PersonaDA
 * para mediante JDBC ejecutar los comandos contra la base de datos
 * dando valores a los parámetros en las sentencias SQL parametrizadas antes de lanzar
 * el comando
 * Todas las sentencias SQL dependen del schema de la tabla persona en la base de datos
 */
public interface EquipoSQL {
    String DROP_TABLE = "DROP TABLE IF EXISTS equipo";

    String SELECT_COUNT_ALL = "SELECT COUNT(*) FROM equipo";
    String SELECT_ALL = "SELECT * FROM equipo";
    String INSERT = "INSERT INTO equipo VALUES (DEFAULT, ?, ?)";
    String SELECT_BY_ID = "SELECT * FROM equipo WHERE equipoID = ?";

    String UPDATE = """
            UPDATE equipo
            SET nombre = ?, pais = ?
            WHERE equipoID = ?
            """;
    String DELETE = "DELETE FROM equipo WHERE equipoID = ?";
}
