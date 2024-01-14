package dam.ad.printers;

import java.util.StringJoiner;

public interface EntityHeaderProvider {

    String getHeader();

    default String getHeader(boolean withLine) {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
        stringJoiner.add(getHeader());
        if(withLine) {
                stringJoiner.add(getLine());
        }
        return stringJoiner.toString();
    }

    default String getLine() {
        return "-".repeat(getHeader().length());
    }

}
