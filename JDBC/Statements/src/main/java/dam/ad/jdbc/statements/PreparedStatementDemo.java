package dam.ad.jdbc.statements;

import java.sql.*;
import java.time.LocalDate;


// En esta demo vamos a reutilizar la misma instancia de conexión durante toda la ejecución
// En lugar de componer las sentencias SQL mediante concatenación de String
// usaremos las SQL con parámetros mediante el uso de la interfaz PreparedStatement

public class PreparedStatementDemo {

    //URL de conexión a la BBDD futbol de HSQLDB
    static final String HSQLDB_FUTBOL_URL =
            "jdbc:hsqldb:file:C:/BBDDs/hsqldb/futbol;ifexists=true";

    public static void main(String[] args) {
        Connection connection = null;   //La vamos a reutilizar, pasándola a todos los métodos
        try {
            connection = openConnection(HSQLDB_FUTBOL_URL, "SA", "");

            dropTable(connection, "jugador");
            dropTable(connection, "equipo");
            createTableEquipo(connection);
            createTableJugador(connection);
            deleteJugadores(connection);
            deleteEquipos(connection);
            insertEquipo(connection, "Real Madrid C.F.", "ESP");
            insertEquipo(connection, "F.C. Barcelona", "ESP");
            insertEquipo(connection, "Club Atlético Newell's Old Boys", "ARG");
            findEquipoById(connection, 2);
            findEquipoById(connection, 100);
            printJugadoresEquipo(connection,1);


            commit(connection); // Validar los comandos

        } catch (RuntimeException ex) {
            rollback(connection);   // Echar para atrás los comandos
            System.out.println(ex.getMessage());
            if (ex.getCause() != null) {
                System.out.println(ex.getCause().getMessage());
            }

        } finally {
            shutdown(connection);   // Apagar el motor de la base de datos
            closeConnection(connection);    //Cerrar la conexión
        }
    }

    // Obtiene el objeto Connection a partir del DriverManager
    static Connection openConnection(String url, String user, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("ERROR al obtener la conexión con la base de datos", e);
        }
    }

    static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("ERROR al cerrar la conexión", e);
            }
        }
    }

    static void commit(Connection connection) {
        try {
            System.out.println("Validando los comandos ejecutados...");
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR en el COMMIT", e);
        }
    }

    static void rollback(Connection connection) {
        try {
            if (connection != null) {
                System.out.println("Deshaciendo las operaciones pendientes de confirmación...");
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR durante el ROLLBACK", e);
        }
    }

    static void executeStatement(Connection connection, final String sql) {
        try (Statement stmt = connection.createStatement()) {

            try {
                stmt.execute(sql);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR al ejecutar el comando", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR al crear el comando", e);
        }
    }

    static void createTableEquipo(Connection connection) {
        System.out.println("Creando la tabla equipo...");
        executeStatement(connection, SQLStatements.SQL_CREATE_TABLE_EQUIPO);
    }

    static void createTableJugador(Connection connection) {
        System.out.println("Creando la tabla jugador...");
        executeStatement(connection, SQLStatements.SQL_CREATE_TABLE_JUGADOR);
    }

    static void deleteEquipos(Connection connection) {
        System.out.println("Eliminado equipos...");
        executeStatement(connection, SQLStatements.SQL_DELETE_EQUIPOS);
    }

    static void deleteJugadores(Connection connection) {
        System.out.println("Eliminando jugadores...");
        executeStatement(connection, SQLStatements.SQL_DELETE_JUGADOR);
    }

    static void dropTable(Connection connection, String tableName) {
        System.out.println("Eliminado tabla " + tableName + "...");
        executeStatement(connection, "DROP TABLE " + tableName);
    }

    static void shutdown(Connection connection) {
        if (connection == null) return;
        System.out.println("Cerrando la base de datos...");
        executeStatement(connection, "SHUTDOWN");
    }

    // Uso del interface PreparedStatement para consultas con parámetros
    // ------------------------------------------------------------------------------
    static void insertEquipo(Connection connection, String nombre, String pais) {

        String SQL = "INSERT INTO equipo VALUES (DEFAULT, ?, ?)";

        // Con un PrepareStatement la SQL se debe indicar en la construcción
        // Aquí vemos otra forma de acceder a los campos auto-generados
        try (PreparedStatement stmt = connection.prepareStatement(SQL, new int[]{1})) {

            stmt.setString(1, nombre);
            stmt.setString(2, pais);

            try {
                stmt.executeUpdate();
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if(rs.next()) {
                        System.out.println("Insertado equipo " +
                                           nombre +
                                           " con ID=" +
                                           rs.getInt(1));
                    }

                    rs.close(); //Con un try-with-resources el close no es necesario hacerlo explicito
                } catch (SQLException e) {
                    throw new RuntimeException("ERROR obteniendo las claves generadas", e);
                }

            } catch (SQLException e) {
                throw new RuntimeException("ERROR insertando equipo", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR creando el comando", e);
        }
    }

    static void findEquipoById(Connection connection, int equipoId) {

        final String SQL = "SELECT * FROM equipo WHERE equipoId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            // Establecemos el valor del parámetro
            stmt.setInt(1, equipoId);

            try {
                //Ejecutamos la consulta
                ResultSet rs = stmt.executeQuery();

                final String format = "%3s %-35s %-4s\n";

                if (rs.next()) { // Iteramos fila a fila moviendo el cursor
                    System.out.printf(format, "ID", "NOMBRE", "PAIS");
                    // Valor de la primera columna de la fila actual como int
                    int equipoID = rs.getInt(1);
                    // Valor de la segunda columna de la fila actual como String
                    String nombre = rs.getString(2);
                    // Valor de la tercera columna de la fila actual como String
                    String pais = rs.getString(3);

                    System.out.printf("%3s %-35s %-4s\n", equipoID, nombre, pais);
                } else {
                    System.out.println("No se encontró ningún equipo con ID=" + equipoId);
                }
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException("ERROR al ejecutar el comando", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR al preparar el comando", e);
        }
    }

    static void printJugadoresEquipo(Connection connection, int equipoId) {

        final String SQL = "SELECT * FROM jugador WHERE equipoId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setInt(1, equipoId);
            try {
                ResultSet rs = stmt.executeQuery();
                final String format = "%3s %-30s %-4s %10s %8s %4s %6s\n";
                System.out.printf(format, "ID", "NOMBRE", "PAIS", "NACIMIENTO", "ESTATURA", "PESO", "DORSAL");

                while (rs.next()) { // Iteramos fila a fila moviendo el cursor
                    // Valor de la primera columna de la fila actual como int
                    int jugadorId = rs.getInt(1);
                    // Valor de la segunda columna de la fila actual como String
                    String nombre = rs.getString(2);
                    // Valor de la tercera columna de la fila actual como String
                    String pais = rs.getString(3);
                    // Valor de la comuna con nombre 'nacimiento'
                    LocalDate nacimiento = rs.getObject("nacimiento", LocalDate.class);
                    // Valor de la columna con nombre 'estatura' como double
                    double estatura = rs.getDouble("estatura");
                    // Valor de la columna peso como int
                    int peso = rs.getInt("peso");
                    // Valor de columna dorsal como int
                    int dorsal = rs.getInt("dorsal");

                    System.out.printf(format, jugadorId, nombre, pais, nacimiento.toString(), estatura, peso, dorsal);
                }
                rs.close();

            } catch (SQLException e) {
                System.out.println("ERROR ejecutando el comando");
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("ERROR preparando el comando");
            System.out.println(e.getMessage());
        }

    }
}
