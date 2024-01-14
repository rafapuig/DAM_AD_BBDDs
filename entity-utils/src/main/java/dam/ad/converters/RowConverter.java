package dam.ad.converters;

public interface RowConverter<T> {
    String getAsRow(T t);

}
