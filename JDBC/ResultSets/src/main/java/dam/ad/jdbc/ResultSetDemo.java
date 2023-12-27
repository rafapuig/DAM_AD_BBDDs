package dam.ad.jdbc;

import java.sql.*;
import java.time.LocalDate;

public class ResultSetDemo {

    static String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";

    public static void main(String[] args) {
        queryPersonaTest();
    }

    static void queryPersonaTest() {
        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection(URL);
            connection.setAutoCommit(false);

            String SQL = "SELECT * FROM persona";

            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery(SQL);
                ResultSetPrinter.printResultSet(rs, System.out);
                //printPersonaResultSet(rs);
            } finally {
                JDBCUtil.close(stmt);
                System.out.println(rs.isClosed());
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            JDBCUtil.close(connection);
        }
    }


    static void queryPersona(String sql) {

    }

    static void printPersonaResultSet(ResultSet rs) throws SQLException {
        String format = "%-2s %-20s %-30s %-4s %-10s %s\n";
        System.out.printf(format, "ID", "NOMBRE", "APELLIDOS", "SEXO", "NACIMIENTO", "INGRESOS");

        while (rs.next()) {
            printPersonaRow(rs);
        }
        JDBCUtil.close(rs);
    }

    static void printPersonaRow(ResultSet rs) throws SQLException {
        int personaId = rs.getInt("personaId");
        String nombre = rs.getString("nombre");
        String apellidos = rs.getString("apellidos");
        String sexo = rs.getString("sexo");

        LocalDate nacimiento = rs.getDate("nacimiento").toLocalDate();
        boolean isNacimientoNull = rs.wasNull();

        float ingresos = rs.getFloat("ingresos");
        boolean isIngresosNull = rs.wasNull();

        String format = "%2s %-20s %-30s %4s %10s %.2f\n";
        System.out.printf(format, personaId, nombre, apellidos, sexo, nacimiento, ingresos);
    }


}
