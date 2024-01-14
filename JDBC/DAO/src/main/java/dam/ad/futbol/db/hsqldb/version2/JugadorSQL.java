package dam.ad.futbol.db.hsqldb.version2;

/**
 * La interfaz SQLs encapsula los comandos SQL que utilizan las clases PersonaDA
 * para mediante JDBC ejecutar los comandos contra la base de datos
 * dando valores a los parámetros en las sentencias SQL parametrizadas antes de lanzar
 * el comando
 * Todas las sentencias SQL dependen del schema de la tabla jugador en la base de datos
 */
public interface JugadorSQL {
    String DROP_TABLE = "DROP TABLE IF EXISTS jugador";

    String SELECT_COUNT_ALL = "SELECT COUNT(*) FROM jugador";
    String SELECT_ALL = "SELECT * FROM jugador";

    String INSERT = "INSERT INTO jugador VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
    String SELECT_BY_ID = "SELECT * FROM jugador WHERE jugadorID = ?";

    String UPDATE = """
            UPDATE equipo
            SET nombre = ?, pais = ?, nacimiento = ?
            estatura = ?, peso = ?, dorsal = ?
            equipoID = ?, capital = ?
            WHERE equipoID = ?
            """;

    String DELETE = "DELETE FROM jugador WHERE jugadorID = ?";
}
