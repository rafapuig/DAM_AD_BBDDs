import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AssertUtils {

    void assertQueryEquals(String expectedResourceName, String result) {
        String[] expected = getExpected(expectedResourceName);
        assertArrayEquals(expected, getLines(result));
    }

    static String[] getExpected(String resourceName) {
        URL url = TestQuery.class.getResource("/query_result_01.txt");

        try (Stream<String> lines = Files.lines(Path.of(url.toURI()))) {

            return lines
                    .map(String::trim)
                    .toArray(String[]::new);

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        //return getLines(expected);
    }

    static String load(URL url) {

        try (Stream<String> lines = Files.lines(Path.of(url.toURI()))) {

            return lines
                    .map(String::trim)
                    .collect(Collectors
                            .joining(System.lineSeparator()));

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static String[] getLines(String text) {
        String[] lines = text.split(System.lineSeparator());
        return Arrays.stream(lines).map(String::trim).toArray(String[]::new);
    }

}
