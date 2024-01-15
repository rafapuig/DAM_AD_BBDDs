import dam.ad.jpa.entity.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ORM-entities");
        EntityManager manager = factory.createEntityManager();


        manager.getTransaction().begin();

        Persona persona = new Persona();
        persona.setNombre("Perico");
        persona.setApellidos("Palotes");
        persona.setSexo("H");
        persona.setNacimiento(Date.valueOf("2000-10-8"));
        persona.setIngresos(1000.0);

        manager.persist(persona);


        String jpql = """
                SELECT p FROM Persona p
                """;

        TypedQuery<Persona> query = manager.createQuery(jpql, Persona.class);

        query.getResultStream().forEach(System.out::println);

        manager.getTransaction().commit();
        manager.close();
        factory.close();
    }
}
