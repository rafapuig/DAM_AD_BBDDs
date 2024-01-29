package dam.ad.consumers;

import dam.ad.printers.StreamPrinter;
import dam.ad.printers.Printers;

import java.util.function.Consumer;

public class DtoPrinter<T> implements Consumer<T> {
    boolean isFirst = true;
    StreamPrinter<T> printer;
    @Override
    public void accept(T t) {

        if(isFirst) {
            printer = Printers.getPrinter(t);
            printer.printHeader();
            isFirst = false;
        }
        printer.printRow(t);
    }
}
