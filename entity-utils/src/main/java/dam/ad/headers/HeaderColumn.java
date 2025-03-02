package dam.ad.headers;

import dam.ad.util.Strings;

interface HeaderColumnFormatter {
    String format(HeaderColumn headerColumn);
}

public record HeaderColumn(String label, int length, Alignment alignment) {

    public enum Alignment {START, CENTER, END}

    public HeaderColumn(String label, int length) {
        this(label, length, Alignment.CENTER);
    }

    String format() {
        //return String.format("%-" + length + "s", label);

        return getFormatter(this.alignment).format(this);
    }

    static HeaderColumnFormatter getFormatter(Alignment alignment) {
        return switch (alignment) {
            case START -> startAligned;
            case CENTER -> centerAligned;
            case END -> endAligned;
        };
    }

    static HeaderColumnFormatter startAligned = headerColumn ->
            Strings.startAligned(headerColumn.label, headerColumn.length);


    static HeaderColumnFormatter endAligned = headerColumn ->
            Strings.endAligned(headerColumn.label, headerColumn.length);


    static HeaderColumnFormatter centerAligned = headerColumn ->
            Strings.centerAligned(headerColumn.label, headerColumn.length);
}
