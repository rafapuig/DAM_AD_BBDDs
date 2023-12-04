import dam.ad.file.ResourceReader;
import org.junit.jupiter.api.Test;

public class ResourceReaderTest {
    @Test
    void readResource() {
        System.out.println(
        ResourceReader.getSQL("/futbol/DatabaseSchema.sql"));
    }
}
