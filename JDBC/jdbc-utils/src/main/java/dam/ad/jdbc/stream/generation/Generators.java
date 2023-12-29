package dam.ad.jdbc.stream.generation;

import dam.ad.jdbc.query.DTOMapper;

import java.sql.ResultSet;

public class Generators {

    public enum Yield {
        EAGER,
        LAZY
    }

    public static  <T> StreamGenerator<T> getStreamGenerator(
            ResultSet resultSet, DTOMapper<T> dtoMapper, Yield yieldType) {
        return switch (yieldType) {
            case LAZY -> new LazyStreamGenerator<>(resultSet, dtoMapper);
            case EAGER -> new EagerStreamGenerator<>(resultSet, dtoMapper);
            case null, default -> throw new IllegalArgumentException("ResultIterationType not expected");
        };
    }

    public static  <T> StreamGenerator<T> getStreamGenerator(Yield yieldType) {
        return switch (yieldType) {
            case LAZY -> new LazyStreamGenerator<>();
            case EAGER -> new EagerStreamGenerator<>();
            case null, default -> throw new IllegalArgumentException("ResultIterationType not expected");
        };
    }

    public static  <T> StreamGenerator<T> getStreamGenerator(boolean lazy) {
        return getStreamGenerator(lazy ? Yield.LAZY : Yield.EAGER);
    }

    public static  <T> StreamGenerator<T> getStreamGenerator() {
        return getStreamGenerator(true);
    }

    public static  <T> StreamGenerator<T> getStreamGenerator(ResultSet resultSet, DTOMapper<T> dtoMapper, boolean lazy) {
        return getStreamGenerator(resultSet, dtoMapper, lazy ? Yield.LAZY : Yield.EAGER);
    }

    public static  <T> StreamGenerator<T> getStreamGenerator(ResultSet resultSet, DTOMapper<T> dtoMapper) {
        return getStreamGenerator(resultSet, dtoMapper, true);
    }
}
