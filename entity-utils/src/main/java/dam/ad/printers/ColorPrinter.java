package dam.ad.printers;

import java.util.StringJoiner;

public class ColorPrinter {

    private String currentColor = ConsoleColors.RESET;

    private ColorPrinter(String color) {
        currentColor = color;
    }

    public static ColorPrinter in(String color) {
        return new ColorPrinter(color);
    }

    private String text;

    public ColorPrinter text(Object text) {
        this.text = text.toString();
        return this;
    }

    public void println(String text) {
        System.out.println();
    }

    public void print(String text) {
        System.out.print(currentColor + text + ConsoleColors.RESET);
    }

    @Override
    public String toString() {
        return currentColor + text + ConsoleColors.RESET;
    }
}
