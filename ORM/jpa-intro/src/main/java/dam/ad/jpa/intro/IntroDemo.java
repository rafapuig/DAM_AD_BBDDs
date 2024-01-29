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

        //testDAO1(manager);

        manager.getTransaction().begin();

        //testDAO2(manager);
        testDAO3(manager);
        //main(manager);

        manager.getTransaction().commit();

        manager.close();
        managerFactory.close();
    }

    private static void testDAO1(EntityManager manager) {
        //Creamos el DAO de empleado
        EmpleadoDAO empleadoDAO = new EmpleadoDAO(manager);

        manager.getTransaction().begin();
        empleadoDAO.add(new Empleado(1,"Armando","Bronca Segura",1000));
        manager.getTransaction().commit();

        manager.getTransaction().begin();
        empleadoDAO.update(new Empleado(1,"Armando","Bronca Segura",2000));
        manager.getTransaction().commit();

        //No provoca una SELECT porque ya se tiene la info en el contexto
        manager.getTransaction().begin();
        empleadoDAO.getById(1).ifPresent(System.out::println);
        manager.getTransaction().commit();

        //Si sacamos a la entidad Empleado 1 del contexto
        ////////manager.detach(manager.find(Empleado.class, 1));

        //Entonces sí que hará falta realizar un SELECT a la BBDD
        manager.getTransaction().begin();
        empleadoDAO.getById(1).ifPresent(System.out::println);
        manager.getTransaction().commit();

        //Aquí hará falta un SELECT * FROM Empleado (sin WHERE id = ?)
        manager.getTransaction().begin();
        empleadoDAO.getAll().forEach(System.out::println);
        manager.getTransaction().commit();

        //Aquí se hará un SELECT COUNT(*)
        manager.getTransaction().begin();
        System.out.println(empleadoDAO.getCount());
        manager.getTransaction().commit();


        Empleado belen = new Empleado(2, "Belen", "Tilla", 2000);
        manager.getTransaction().begin();
        empleadoDAO.add(belen);
        manager.getTransaction().commit();

        //Aquí hará falta un SELECT * FROM Empleado (sin WHERE id = ?)
        manager.getTransaction().begin();
        empleadoDAO.getAll().forEach(System.out::println);
        manager.getTransaction().commit();

        manager.getTransaction().begin();
        empleadoDAO.delete(belen);
        System.out.println(empleadoDAO.getCount());
        manager.getTransaction().commit();
    }

    private static void testDAO2(EntityManager manager) {
        //Creamos el DAO de empleado
        EmpleadoDAO empleadoDAO = new EmpleadoDAO(manager);

        empleadoDAO.add(new Empleado(1,"Armando","Bronca Segura",1000));

        empleadoDAO.update(new Empleado(1,"Armando","Bronca Segura",2000));

        //No provoca una SELECT porque ya se tiene la info en el contexto
        empleadoDAO.getById(1).ifPresent(System.out::println);

        //Sí sacamos a la entidad Empleado 1 del contexto
        manager.detach(manager.find(Empleado.class, 1)); //Fallo de concurrencia

        //Entonces sí que hará falta realizar un SELECT a la BBDD
        empleadoDAO.getById(1).ifPresent(System.out::println);

        //Aquí hará falta un SELECT * FROM Empleado (sin WHERE id = ?)
        empleadoDAO.getAll().forEach(System.out::println);


        //Aquí se hará un SELECT COUNT(*)
        System.out.println(empleadoDAO.getCount());


        Empleado belen = new Empleado(2, "Belen", "Tilla", 2000);
        empleadoDAO.add(belen);

        //Aquí hará falta un SELECT * FROM Empleado (sin WHERE id = ?)
        empleadoDAO.getAll().forEach(System.out::println);

        empleadoDAO.delete(belen);
        System.out.println(empleadoDAO.getCount());
    }

    private static void testDAO3(EntityManager manager) {
        //Creamos el DAO de empleado


        manager.persist(new Empleado(1,"Armando","Bronca Segura",1000));

        manager.merge(new Empleado(1,"Armando","Bronca Segura",2000));

        //No provoca una SELECT porque ya se tiene la info en el contexto
        System.out.println(manager.find(Empleado.class,1));

        //Sí sacamos a la entidad Empleado 1 del contexto
        //manager.detach(manager.find(Empleado.class, 1)); //Fallo de concurrencia

        //Entonces sí que hará falta realizar un SELECT a la BBDD
        System.out.println(manager.find(Empleado.class,1));

        //Aquí hará falta un SELECT * FROM Empleado (sin WHERE id = ?)
        manager.createQuery("SELECT e FROM Empleado e", Empleado.class).getResultStream().forEach(System.out::println);


        //Aquí se hará un SELECT COUNT(*)
        System.out.println(manager.createQuery("SELECT count(e) FROM Empleado e", Long.class).getSingleResult());


        Empleado belen = new Empleado(2, "Belen", "Tilla", 2000);
        manager.persist(belen);

        manager.createQuery("SELECT e FROM Empleado e", Empleado.class).getResultStream().forEach(System.out::println);

        manager.remove(belen);
        System.out.println(manager.createQuery("SELECT count(e) FROM Empleado e", Long.class).getSingleResult());
    }



    private static void main(EntityManager manager) {
        //Es importante recordar que las operaciones persist, merge, remove no implican una operación
        // INSERT, UPDATE o DELETE necesariamente contra la base de datos
        // Lo que hacen modificar el contexto del entity manager (añadiendo, quitando y alterando
        // los objetos que allí se encuentran)
        // En el momento del commit si un objeto se encuentra en el contexto y no el la BBDD se
        // realizará un INSERT con los últimos valores que han adquirido los campos de la entidad
        // Se realizará un UPDATE si la entidad existe pero los valores de los campos son diferentes
        // Y se realizará un DELETE si una entidad existente en la BBDD se ha solicitado ser removida
        // del contexto, operación remove del EntityManager
        // Si se quiere quitar una entidad del contexto sin que implique eliminarla de la BBDD se usa
        // la operación detach del EntityManager
        // Dado que los metodos add, update y delete del DAO se basan en operaciones del EntityManager
        // Entonces:
        // Lo que en realidad realiza add es añadir al contexto un objeto que no existe en la BBDD
        // delete quitar del contexto y pedir que se quite de la BBDD tras el commit,
        // modificar los valores de los campos un objeto con la misma ID en el contexto con los del objeto
        // pasado como argumento (update)


        //Creamos el DAO de empleado
        EmpleadoDAO empleadoDAO = new EmpleadoDAO(manager);

        empleadoDAO.add(new Empleado(1, "Armando", "Bronca Segura", 2000));
        empleadoDAO.add(new Empleado(2, "Belen", "Tilla", 1800));

        //Probamos a recuperar todos los empleados
        empleadoDAO.getAll().forEach(System.out::println);

        //Obtenemos un empleado a partir de su ID (clave primaria)
        Optional<Empleado> armandoOpt = empleadoDAO.getById(1);
        armandoOpt.ifPresentOrElse(System.out::println, () -> System.out.println("No se encuentra"));

        System.out.println("Le subimos el sueldo a Armando...");
        armandoOpt.ifPresent(empleado -> empleado.setSalario(empleado.getSalario() + 1000));
        //Y vemos que tiene efecto en el contexto (ahora su sueldo es 3000)
        System.out.println(empleadoDAO.getById(1).orElse(null)); //3000

        Empleado armando = armandoOpt.orElse(null);
        armando.setSalario(armando.getSalario() + 600);
        System.out.println(armando);

        //No es necesario porque ya está administrado por el entity manager
        //empleadoDAO.update(armando.orElse(null));

        Empleado empleado = empleadoDAO.getById(1).orElse(null);
        System.out.println("Imprimiendo valor actualizado:");
        System.out.println(empleado); //Podemos comprobar que imprime 3000 en sueldo

        //Pero si no está administrado...
        manager.detach(empleado); //Se saca al empleado del contexto, se perdieron los cambios...
        empleado.setSalario(1500);
        System.out.println("Imprimiendo desde la referencia detached");
        System.out.println(empleado); //La referencia al objeto si tiene sueldo 1500
        //Pero si lo obtenemos desde el contexto no
        System.out.println("Imprimiendo valor desde el contexto (el valor original):");
        System.out.println(empleadoDAO.getById(1).orElse(null));

        //Ahora lo volvemos a poner en el contexto para que sea administrado
        empleadoDAO.update(empleado);
        System.out.println(empleadoDAO.getById(1).orElse(null));

        Empleado emp = empleadoDAO.getById(1).orElse(null);
        emp.setSalario(emp.getSalario() + 10000);
        System.out.println(empleadoDAO.getById(1).orElse(null));

        System.out.println("Eliminando el empleado 1 del contexto...");
        //Eliminar un empleado
        armandoOpt.ifPresent(empleadoDAO::delete);
        System.out.println("Quedan:");
        empleadoDAO.getAll().forEach(System.out::println);

        //Ahora eliminamos un empleado que no esté en el contexto,
        // funcionará ya que el empleadoDAO primero lo añade si no está en el contexto
        System.out.println("Eliminando el empleado 2 del contexto...");
        empleadoDAO.delete(new Empleado(2, "","",0));
        System.out.println("Quedan:");
        System.out.println(empleadoDAO.getCount());


        //Si tenemos una referencia a objeto que no está en el contexto no se sigue lo que le pase
        Empleado amador = new Empleado(3, "Amador", "Denador", 1000);
        empleadoDAO.getById(3)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No existe en el contexto el empleado " + 3));

        // Si hay un objeto en el contexto le da los valores de la referencia pasada como argumento
        // Si no crea un objeto en el contexto y le asigna los mismos valores que los que tiene el objeto argumento
        empleadoDAO.update(amador); //Internamente llama a merge
        System.out.println(empleadoDAO.getById(3).orElse(null));

        //La referencia sigue sin estar administrada por en entity manager
        amador.setSalario(amador.getSalario() + 300);
        //empleadoDAO.update(amador);
        System.out.println(empleadoDAO.getById(3).orElse(null)); //1000 el valor anterior sin modificar
        amador.setSalario(amador.getSalario() + 400);

        //Si hacemos update, se pasan los valores del objeto argumento al objeto administrado
        empleadoDAO.update(amador);
        //Por eso ahora sí obtenemos el valor último del salario 1000 + 300 + 400
        System.out.println(empleadoDAO.getById(3).orElse(null)); //1700

        //Obtener una referencia administrada
        Empleado amadorManaged = manager.merge(amador);
        amadorManaged.setSalario(amadorManaged.getSalario() + 2000);
        System.out.println(empleadoDAO.getById(3).orElse(null)); //3700


        //En este caso la referencia al objeto la pasaremos al método add (persist)
        //Se seguirán los cambios que se realizan en la instancia para al final en el commit
        //Realizar una operación INSERT con los últimos valores de lo campos del objeto
        Empleado pedro = new Empleado(4, "Pedro", "Gado", 1200);
        empleadoDAO.add(pedro); //Implica llamar a persist y añadirlo al contexto para administrarlo
        pedro.setSalario(pedro.getSalario() + 600);
        System.out.println(empleadoDAO.getById(4).orElse(null)); //1800
        pedro.setSalario(pedro.getSalario() + 500);
        System.out.println(empleadoDAO.getById(4).orElse(null)); //2300
    }
}
