package dam.ad.jdbc.stream.generation;

public class Generators {

    public enum Yield {
        EAGER,
        LAZY
    }

    public static  <T> StreamGenerator<T> getStreamGenerator(Yield resultIterationType) {
        return switch (resultIterationType) {
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
}
