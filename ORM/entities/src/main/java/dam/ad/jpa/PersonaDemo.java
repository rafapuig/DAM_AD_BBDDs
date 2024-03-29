package dam.ad.jpa;

import dam.ad.jpa.entity.Direccion;
import dam.ad.jpa.entity.Persona;
import dam.ad.jpa.entity.Sexo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersonaDemo {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", "true");

        EntityManagerFactory factory = Persistence
                .createEntityManagerFactory(
                        "ORM-entities",
                        properties);

        EntityManager manager = factory.createEntityManager();

        manager.getTransaction().begin();

        Persona persona = new Persona();
        persona.setNombre("Perico");
        persona.setApellidos("Palotes");
        persona.setSexo(Sexo.HOMBRE);
        persona.setNacimiento(LocalDate.parse("2000-10-08"));
        persona.setIngresos(1000.0);

        Direccion direccion = new Direccion();
        //direccion.setCodigoPostal("46025");
        //direccion.setMunicipio("Valencia");
        persona.setDireccion(direccion);

        manager.persist(persona);


        String jpql = """
                SELECT p FROM Persona p
                """;

        TypedQuery<Persona> query = manager
                .createQuery(jpql, Persona.class);

        Persona p1 = manager.find(Persona.class, 1);
        p1.setNombre("Fofo");

        query.getResultStream().forEach(System.out::println);


        query.getResultStream().forEach(p -> p.setIngresos(p.getIngresos() * 1.05));




        manager.getTransaction().commit();
        manager.close();
        factory.close();
    }
}
