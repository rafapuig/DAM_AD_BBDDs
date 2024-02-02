package dam.ad.printers;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Printers {
    private static final Map<Class<?>, StreamPrinter<?>> map =
            new HashMap<>();

    static final Writer CONSOLE = new PrintWriter(System.out, true);
    static Writer writer = CONSOLE;
    public static void setWriter(Writer writer) {
        Printers.writer = writer;
    }

    public static void resetWriter() {
        Printers.writer = CONSOLE;
    }

    public static <T> void registerPrinter(Class<T> type) {
        //System.out.println("Registrando un StreamPrinter de " + type.getName());
        Supplier<StreamPrinter<T>> printerSupplier = () -> {
            DefaultDTOPrinter<T> printer = new DefaultDTOPrinter<>(type);
            //printer.setWriter(writer);
            return printer;
        };

        map.putIfAbsent(type, printerSupplier.get());
    }

    public static <T> StreamPrinter<T> getPrinter(Class<T> type) {
        //System.out.println("Obteniendo un printer para el tipo " + type.getName());
        if(!map.containsKey(type)) {
            //System.out.println("No registrado!");
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

    public static void print(Stream<Object[]> rowValues, String[] labels, int[] lengths) {
        ObjectArrayPrinter printer = new ObjectArrayPrinter(labels, lengths);
        printer.print(rowValues);
    }

    public static void print(Stream<Object[]> rowValues, String label1, int length1, String label2, int length2) {
        ObjectArrayPrinter printer = new ObjectArrayPrinter(label1, length1, label2, length2);
        printer.print(rowValues);
    }


    public static <T> String getAsRow(T t) {
        return getPrinter(t).getRowConverter().getAsRow(t);
    }

    public static <T> String getString(Stream<T> stream) {

        //DtoPrinter<T> dtoPrinter = new DtoPrinter<>();
        //stream.forEach(dtoPrinter);

        final AtomicBoolean first = new AtomicBoolean(true);
        final AtomicReference<StreamPrinter<T>> printer = new AtomicReference<>();

        String result = stream
                .map(t -> {
                    String text = "";
                    if (first.get()) {
                        printer.set(getPrinter(t));
                        text = printer.get().getHeaderProvider().getHeader(true);
                        text += System.lineSeparator();
                        first.set(false);
                    }
                    text += printer.get().getRowConverter().getAsRow(t);
                    return text;
                })
                .collect(Collectors
                        .joining(System.lineSeparator()));

        return result;
    }
}
