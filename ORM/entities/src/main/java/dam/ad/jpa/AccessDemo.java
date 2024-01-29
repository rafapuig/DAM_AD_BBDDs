package dam.ad.jpa;

import dam.ad.jpa.entity.AccessState.Persona_MixedAccess;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;

public class AccessDemo {
    public static void main(String[] args) {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory(
                        "ORM-entities",
                        Map.of("hibernate.show_sql", true));

        EntityManager manager = factory.createEntityManager();

        manager.getTransaction().begin();

        Persona_MixedAccess persona = new Persona_MixedAccess();
        persona.setId(1);
        persona.setNombre("Armando");
        persona.setApellidoPaterno("Bronca");
        persona.setApellidoMaterno("Segura");

        manager.persist(persona);

        manager.getTransaction().commit();

        manager.close();
        factory.close();
    }
}
