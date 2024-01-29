package dam.ad.printers;

import dam.ad.consumers.DtoPrinter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Printers {
    private static final Map<Class<?>, StreamPrinter<?>> map =
            new HashMap<>();

    public static <T> void registerPrinter(Class<T> type) {
        System.out.println("Registrando un RowPrinter de " + type.getName());
        map.putIfAbsent(type, new DefaultDTOPrinter<>(type));
    }

    public static <T> StreamPrinter<T> getPrinter(Class<T> type) {
        if(!map.containsKey(type)) {
            registerPrinter(type);
        }
        StreamPrinter<T> printer = (StreamPrinter<T>) map.get(type);
        return printer;
    }

    public static <T> StreamPrinter<T> getPrinter(T t) {
        return getPrinter((Class<T>) t.getClass());
    }


    public static <T> void print(Class<T> type, Stream<T> stream) {
        //stream.peek(t ->  setClass(t.getClass()));
        StreamPrinter<T> printer = (StreamPrinter<T>) getPrinter(type);
        printer.print(stream);
    }

    public static <T> void print(Stream<T> stream) {

        //DtoPrinter<T> dtoPrinter = new DtoPrinter<>();
        //stream.forEach(dtoPrinter);

        final AtomicBoolean first = new AtomicBoolean(true);
        final AtomicReference<StreamPrinter<T>> printer = new AtomicReference<>();

        stream
                .peek(t -> {
                    if (first.get()) {
                        printer.set(getPrinter(t));
                        printer.get().printHeader();
                        first.set(false);
                    }
                })
                .forEach(t ->  printer.get().printRow(t));
    }
}
