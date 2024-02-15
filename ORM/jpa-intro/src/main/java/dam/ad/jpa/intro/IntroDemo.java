package dam.ad.jpa.intro;

import dam.ad.jpa.intro.entities.Empleado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;
import java.util.Optional;

public class IntroDemo {

    public static void main(String[] args) {
        EntityManagerFactory managerFactory =
                Persistence.createEntityManagerFactory("ORM-intro",
                        Map.of("hibernate.show_sql", true));

        EntityManager manager = managerFactory.createEntityManager();

        manager.getTransaction().begin();

        testEntityManager(manager);

        manager.getTransaction().commit();
        manager.close();
        managerFactory.close();
    }

    /**
     * En este método de test hacemos las operaciones directamente con el EntityManager,
     * sin usar un DAO
     */
    private static void testEntityManager(EntityManager manager) {

        System.out.println("Añadiendo una nueva entidad empleado al contexto... (no INSERT inmediato)");
        //Equivale a añadir una nueva entidad al contexto y que no existe en la BD
        manager.persist(new Empleado(1, "Armando", "Bronca Segura", 1000));
        //No provoca un INSERT inmediato sobre la BD (queda pendiente)
        System.out.println("Empleado 1 añadido al contexto.");

        System.out.println("\nImprimiendo la información del empleado 1...(no SELECT pq esta en el contexto)");
        //No provoca una SELECT porque ya se tiene la info en el contexto
        System.out.println(manager.find(Empleado.class, 1));

        System.out.println("\nActualizando los datos (cambio de salario) del objeto existente...(no UPDATE inmediata");
        //Los valores del objeto empleado actualizarán los de empleado con el mismo Id que exista en el contexto
        manager.merge(new Empleado(1, "Armando", "Bronca Segura", 2000));

        System.out.println("\nImprimiendo la información del empleado 1...");
        //No provoca una SELECT porque ya se tiene la info en el contexto
        System.out.println(manager.find(Empleado.class, 1));

        System.out.println("\nSincronizando el contexto con la BD...(Ahora si INSERT y UPDATE)");
        manager.flush();    //Llamar antes de detach para evitar el fallo de concurrencia
        //Sí sacamos a la entidad Empleado 1 del contexto
        manager.detach(manager.find(Empleado.class, 1)); //Fallo de concurrencia


        System.out.println("\nImprimiendo el empleado detached y ahora recuperado de la BD...(SELECT pq no en contexto)");
        //Entonces sí que hará falta realizar un SELECT a la BBDD
        System.out.println(manager.find(Empleado.class, 1));


        System.out.println("\nConsulta a la BD todos los empleados...");
        //Aquí hará un SELECT * FROM Empleado (sin WHERE id = ?)
        manager.createQuery("SELECT e FROM Empleado e", Empleado.class)
                .getResultStream().forEach(System.out::println);

        System.out.println("\nConsulta a la BD todos los cantidad de empleados...");
        //Aquí se hará un SELECT COUNT(*)
        System.out.println("Numero de empleados: " +
                           manager.createQuery("SELECT count(e) FROM Empleado e", Long.class)
                                   .getSingleResult());


        System.out.println("\nCreamos un nuevo empleado y lo añadimos al contexto...(no INSERT)");
        Empleado belen = new Empleado(2, "Belen", "Tilla", 2000);
        manager.persist(belen);
        //No se realiza el INSERT simplemente por hacer el persist

        //Ahora que vamos a consultar el ORM sabe que hay un empleado que no está aún en la BD
        //Y lo inserta para que se pueda recuperar en la consulta SELECT
        System.out.println("\nVolvemos a consultar...");
        manager.createQuery("SELECT e FROM Empleado e", Empleado.class)
                .getResultList().forEach(System.out::println);

        System.out.println("\nEliminamos la empleada Belen...");
        manager.remove(belen);  //NO produce un DELETE inmediato, solo cuando sea necesario
        System.out.println("Empleada eliminada del contexto y pendiente de eliminar de la BD");

        //Como se va a consultar de nuevo, para la empleada eliminada no se devuelva en la SELECT
        //Se realiza previamente a la consulta un DELETE de la empleada
        System.out.println("\nConsultando cantidad de empleados tras eliminar la empleada...");
        System.out.println("Numero de empleados: " +
                           manager.createQuery("SELECT count(e) FROM Empleado e", Long.class)
                                   .getSingleResult());
    }

}
