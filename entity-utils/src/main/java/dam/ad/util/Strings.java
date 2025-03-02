package dam.ad.util;

public class Strings {

    public static String startAligned(String text, int width) {
        return String.format("%-" + width + "s", text);
    }

    public static String endAligned(String text, int width) {
        return String.format("%" + width + "s", text);
    }

    public static String centerAligned(String text, int width) {
        int padding = width - text.length();
        int paddingLeft = padding / 2;
        return String.format("%-" + width + "s",
                String.format("%" + (paddingLeft + text.length()) + "s",
                        text));
    }

}
