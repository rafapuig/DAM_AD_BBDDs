package dam.ad.headers;

public record HeaderColumn(String label, int length) {
    String format() {
        return String.format("%-" + length + "s", label);
    }
}
