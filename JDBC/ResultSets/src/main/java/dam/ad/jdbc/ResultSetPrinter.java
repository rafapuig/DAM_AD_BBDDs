package dam.ad.jdbc;

import dam.ad.stream.Success;
import dam.ad.stream.Try;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResultSetPrinter implements Consumer<ResultSet> {
    PrintStream printer;

    public ResultSetPrinter(PrintStream printer) {
        this.printer = printer;
    }

    public void printResultSet(ResultSet rs) throws SQLException {
        printResultSet(rs, this.printer);
    }

    @Override
    public void accept(ResultSet resultSet) {
        try {
            printResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Error consumiendo el ResulSet", e);
        }
        finally {
            JDBCUtil.close(resultSet);
        }
    }

    private static String[] getColumnLabels(ResultSetMetaData rsmd) throws SQLException {

        String[] headers = IntStream.rangeClosed(1, rsmd.getColumnCount())
                .mapToObj(index -> Try.of(() -> rsmd.getColumnName(index)))
                //.map(name -> name.map(Function.identity()))
                .filter(name -> name instanceof Success<String>)
                .map(Try::getResult)
                /*.map(name -> switch (name) {
                    case Success(String result) -> result;
                    case Failure(Throwable throwable) -> "Error: " + throwable.getMessage();
                })*/
                .toArray(String[]::new);

        return headers;
    }

    record ColumnData(
            Try columnLength,
            Try columnAlignment
    ){}

    private static String getRowStringFormat(ResultSetMetaData rsmd) throws SQLException {

        String lengths = IntStream.rangeClosed(1, rsmd.getColumnCount())
                .mapToObj(index -> new ColumnData(
                        Try.of(() -> getColumnLength(rsmd, index)),
                        Try.of(() -> getColumnAlignment(rsmd, index))))
                .filter(tries -> tries.columnLength instanceof Success<?> && tries.columnAlignment instanceof Success<?>)
                .map(tries -> (int) tries.columnLength.getResult() * (int) tries.columnAlignment.getResult())
                .map(String::valueOf)
                .map(s -> "%" + s + "s")
                .collect(Collectors.joining(" ", "", "\n"));

        return lengths;
    }

    private static String getRowStringFormat2(ResultSetMetaData rsmd) throws SQLException {

        String lengths = IntStream.rangeClosed(1, rsmd.getColumnCount())

                .mapToObj(index -> new Try[]{
                        Try.of(() -> getColumnLength(rsmd, index)),
                        Try.of(() -> getColumnAlignment(rsmd, index))})
                .filter(tries -> tries[0] instanceof Success<?> && tries[1] instanceof Success<?>)
                .map(tries -> (int) tries[0].getResult() * (int) tries[1].getResult())
                .map(String::valueOf)
                .map(s -> "%" + s + "s")
                .collect(Collectors.joining(" ", "", "\n"));

        return lengths;
    }

    private static String getHeaderStringFormat(ResultSetMetaData rsmd) throws SQLException {

        String lengths = IntStream.rangeClosed(1, rsmd.getColumnCount())
                .mapToObj(index -> Try.of(() -> getColumnLength(rsmd, index)))
                .filter(size -> size instanceof Success<Integer>)
                .map(Try::getResult)
                .map(String::valueOf)
                .map(s -> "%-" + s + "s")
                .collect(Collectors.joining(" ", "", "\n"));

        return lengths;
    }

    public static void printResultSet(ResultSet rs, PrintStream printer) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();

        printer.printf(getHeaderStringFormat(rsmd), getColumnLabels(rsmd));



        while (rs.next()) {
            printRow(rs,printer);
        }
        JDBCUtil.close(rs);
    }

    static void printRow(ResultSet rs, PrintStream printStream) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();

        String rowFormat = getRowStringFormat(rsmd);

        Object[] values = new Object[rsmd.getColumnCount()];

        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            switch (rsmd.getColumnType(i + 1)) {
                case Types.INTEGER:
                    values[i] = rs.getInt(i + 1);
                    if(rs.wasNull()) values[i] = null;
                    break;
                case Types.DOUBLE:
                    values[i] = rs.getDouble(i + 1);
                    if(rs.wasNull()) values[i] = null;
                    break;
                default:
                    values[i] = rs.getString(i + 1);
                    if(rs.wasNull()) values[i] = null;
            }
        }

        printStream.printf(rowFormat, values);
    }

    void printRow(ResultSet rs) throws SQLException {
        printRow(rs, this.printer);
    }

    private static int getColumnLength(ResultSetMetaData rsmd, int index) throws SQLException {
        int length = 0;
        length = rsmd.getColumnLabel(index).length();

        if (rsmd.getColumnType(index) == Types.VARCHAR) {
            length = rsmd.getColumnDisplaySize(index);
        }
        return length;
    }




    private static class ColumnAlignment {
        public static int LEFT = -1;
        public static int RIGHT = 1;
    }
    private static int getColumnAlignment(ResultSetMetaData rsmd, int index) throws SQLException {

        if (rsmd.getColumnType(index) == Types.VARCHAR) {
            return ColumnAlignment.LEFT;
        }
        return ColumnAlignment.RIGHT;
    }
}
