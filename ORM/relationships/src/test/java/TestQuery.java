import dam.ad.jpa.exercise.QueryFutbolApp;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestQuery {

    static EntityManagerFactory factory;
    static EntityManager manager;

    @BeforeAll
    static void setUp() {
        factory = Persistence
                .createEntityManagerFactory("ORM-futbol",
                        Map.of("hibernate.show_sql", false));
        manager = factory.createEntityManager();
    }

    @AfterAll
    static void tearDown() {
        manager.close();
        factory.close();
    }

    @Test
    void testQueryExample() {

        //Que el metodo devuelva el Stream y ya lo gestionaremos aqui

        //String result = QueryFutbolApp.printJugadoresNoESPJueganUCL(manager);

        //assertQueryEquals("/query_result_01.txt", result);
    }

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
