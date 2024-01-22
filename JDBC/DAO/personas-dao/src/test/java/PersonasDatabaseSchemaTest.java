import dam.ad.personas.db.hsqldb.PersonasDatabaseSchema;
import org.junit.jupiter.api.Test;

public class PersonasDatabaseSchemaTest {

    @Test
    void testGetSchema() {
        System.out.println(new PersonasDatabaseSchema().getCreateSchema());
    }


}
