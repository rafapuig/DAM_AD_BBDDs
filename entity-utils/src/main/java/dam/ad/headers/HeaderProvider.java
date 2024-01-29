package dam.ad.headers;

import java.util.StringJoiner;

public interface HeaderProvider {

    String getHeader();

    default String getHeader(boolean withLine) {
        if (!withLine) return getHeader();

        return new StringJoiner(System.lineSeparator())
                .add(getHeader())
                .add(getLine())
                .toString();
    }

    default String getLine() {
        return "-".repeat(getHeader().length());
    }

}
