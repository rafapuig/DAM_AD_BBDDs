package dam.ad.jdbc.stream.generation;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.stream.ThrowingFunction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public final class EagerStreamGenerator<T> extends StreamGenerator<T> {

    public EagerStreamGenerator() {
        super(null, null);
    }

    public EagerStreamGenerator(ResultSet resultSet, DTOMapper<T> dtoMapper) {
        super(resultSet, dtoMapper);
    }

    /**
     * Genera un Stream mediante un Stream.Builder
     * En este caso se itera el ResultSet hasta al final y, por tanto, se consume
     * @param rs ResultSet del que se van a extraer los registros recorriéndolo
     * @param dtoMapper mapea un ResultSet posicionado en una fila a un DTO
     * @return Un stream con los DTOs generados a partir de recorrer el resultSet
     * @param <T> el tipo del DTO
     * @throws SQLException si ocurre un excepción en el método next
     */
    @Override
    public Stream<T> generate(
            ResultSet rs,
            DTOMapper<T> dtoMapper) throws SQLException {

        // Vamos a generar un Stream<T> mediante un Stream Builder
        Stream.Builder<T> builder = Stream.builder();

        while (rs.next()) { // mientras leamos un nuevo registro en el ResultSet ...
                builder.add(dtoMapper.apply(rs)); // Añadimos un DTO al builder
        }
        //builder.build().map(t -> Try.of(() -> dtoMapper.apply(rs)));
        close(rs);
        return builder.build();
    }

    @Override
    public Stream<T> generate() {
        try {
            return generate(resultSet, dtoMapper);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
