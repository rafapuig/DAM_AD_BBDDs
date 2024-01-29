package dam.ad.audiovisuales;

import dam.ad.audiovisuales.model.Pelicula;
import dam.ad.dao.DAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.stream.Stream;

public class PeliculaDAO implements DAO<Pelicula> {
    DataSource dataSource;
    Connection connection;

    public PeliculaDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        connection = getNewConnection();
    }

    private Connection getConnection() {
        //return getNewConnection();
       return this.connection;
    }

    private Connection getNewConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Pelicula> getById(int id) {

        String sql = "SELECT * FROM pelicula WHERE peliculaID = ?";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet resultSet = stmt.executeQuery()) {

                if(resultSet.next()) {
                    Pelicula pelicula = createPelicula(resultSet);
                    return Optional.of(pelicula);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean add(Pelicula pelicula) {

        String sql = "INSERT INTO pelicula VALUES(DEFAULT,?,?,?,?,?)";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pelicula.getTitulo());
            stmt.setString(2, pelicula.getAka());
            stmt.setInt(3, pelicula.getAño());
            stmt.setInt(4, pelicula.getDuracion());
            stmt.setString(5, pelicula.getPais());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    keys.next();
                    pelicula.setPeliculaID(keys.getInt(1));
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Pelicula pelicula) {
        String sql = "UPDATE pelicula SET titulo = ?, aka = ?, año = ?, duracion = ?, pais = ? WHERE peliculaID = ?";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(sql)) {

            stmt.setString(1, pelicula.getTitulo());
            stmt.setString(2, pelicula.getAka());
            stmt.setInt(3, pelicula.getAño());
            stmt.setInt(4, pelicula.getDuracion());
            stmt.setString(5, pelicula.getPais());
            stmt.setInt(6, pelicula.getPeliculaID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Pelicula pelicula) {
        String sql = "DELETE FROM pelicula WHERE peliculaID = ?";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, pelicula.getPeliculaID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Pelicula createPelicula(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("peliculaID");
        String titulo = resultSet.getString("titulo");
        String aka = resultSet.getString("aka");
        int año = resultSet.getInt("año");
        int duracion = resultSet.getInt("duracion");
        String pais = resultSet.getString("pais");

        return new Pelicula(id, titulo, aka, año, duracion, pais);
    }

    @Override
    public Stream<Pelicula> getAll() {
        String sql = "SELECT * FROM pelicula";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(sql)) {

            try (ResultSet resultSet = stmt.executeQuery()) {

                Stream.Builder<Pelicula> builder = Stream.builder();

                while(resultSet.next()) {
                    Pelicula pelicula = createPelicula(resultSet);

                    builder.add(pelicula);
                }
                return builder.build();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getCount() {
        return DAO.super.getCount();
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
