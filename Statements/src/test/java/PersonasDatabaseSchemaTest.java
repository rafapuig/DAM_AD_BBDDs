import dam.ad.jdbc.dao.hsqldb.PersonasDatabaseSchema;
import org.junit.jupiter.api.Test;

public class PersonasDatabaseSchemaTest {

    @Test
    void testGetSchema() {
        System.out.println(new PersonasDatabaseSchema().getCreateSchema());
    }


}
