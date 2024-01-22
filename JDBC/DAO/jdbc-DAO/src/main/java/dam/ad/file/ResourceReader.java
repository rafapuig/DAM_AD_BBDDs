package dam.ad.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResourceReader {
    public static String getSQL(String name) {
        try (InputStream input = ResourceReader.class.getResourceAsStream(name)) {
            InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(input));
            BufferedReader br = new BufferedReader(isr);

            return br.lines()
                    .map(String::trim)
                    .collect(Collectors.joining(System.lineSeparator()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
