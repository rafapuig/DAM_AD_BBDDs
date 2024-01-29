package dam.ad.jpa;

import dam.ad.jpa.entity.AccessState.Persona_MixedAccess;
import dam.ad.jpa.entity.column.Persona_LazyFetching;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;

public class LazyFetchDemo {

    public static void main(String[] args) {


        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory(
                        "ORM-entities",
                        Map.of("hibernate.show_sql", true,
                                "hibernate.bytecode.use_reflection_optimizer",true));

        EntityManager manager = factory.createEntityManager();

        manager.getTransaction().begin();

        Persona_LazyFetching persona = new Persona_LazyFetching();
        persona.setId(1);
        persona.setNombre("Armando");
        persona.setApellidoPaterno("Bronca");
        persona.setApellidoMaterno("Segura");
        persona.setFoto("-".repeat(100).getBytes());
        manager.persist(persona);
        manager.getTransaction().commit();

        manager.close();

        manager = factory.createEntityManager();
        //manager.getTransaction().begin();
        //manager.detach(persona);

        manager.getTransaction().begin();
        Persona_LazyFetching p = manager.find(Persona_LazyFetching.class, 1);
        //manager.createQuery("SELECT p FROM Persona_LazyFetching p", Persona_LazyFetching.class).getSingleResult();
        System.out.println(p.getNombre());
        System.out.println(new String(p.getFoto()));
        manager.getTransaction().commit();

        manager.close();
        factory.close();
    }
}
