package dam.ad.jdbc.query;

import dam.ad.jdbc.JDBCUtil;
import dam.ad.jdbc.stream.generation.Generators;
import dam.ad.stream.StreamUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class JDBCQuery {

    public static void query(
            Connection connection,
            String sql,
            Consumer<PreparedStatement> paramSetter,
            Consumer<ResultSet> resultSetConsumer) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            paramSetter.accept(stmt);

            try {
                ResultSet rs = stmt.executeQuery();
                resultSetConsumer.accept(rs);
                JDBCUtil.close(rs);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    public static <T> ResultSetStream<T> query(
            Connection connection,
            String sql,
            Consumer<PreparedStatement> paramSetter,
            DTOMapper<T> dtoMapper,
            boolean lazy) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try {
                ResultSet rs = stmt.executeQuery();

                return new ResultSetStream<>(Generators.getStreamGenerator(lazy), rs, dtoMapper);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

}
