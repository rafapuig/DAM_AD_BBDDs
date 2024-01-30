package dam.ad.jpa.intro;

import dam.ad.jpa.intro.entities.Empleado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;
import java.util.Optional;

public class EmpleadoDAODemo {
    public static void main(String[] args) {
        EntityManagerFactory managerFactory =
                Persistence.createEntityManagerFactory("ORM-intro",
                        Map.of("hibernate.show_sql", true));

        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        testDAO(manager);
        manager.getTransaction().commit();

        manager.close();
        managerFactory.close();
    }


    private static void testDAO(EntityManager manager) {
        //Es importante recordar que las operaciones persist, merge, remove no implican una operación
        // INSERT, UPDATE o DELETE necesariamente inmediata contra la base de datos
        // Lo que hacen modificar el contexto del entity manager (añadiendo, quitando y alterando
        // los objetos que allí se encuentran)
        // En el momento del commit si un objeto se encuentra en el contexto y no en la BBDD se
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

        System.out.println("Añadiendo dos entidades empleado...");
        //Añadimos dos entidades Empleado
        empleadoDAO.add(new Empleado(1, "Armando", "Bronca Segura", 2000));
        empleadoDAO.add(new Empleado(2, "Belen", "Tilla", 1800));

        System.out.println("\nRecuperar todas las entidades...");
        //Probamos a recuperar todos los empleados
        empleadoDAO.getAll().forEach(System.out::println);

        System.out.println("\nObtener una entidad empleado a partir de ID (no SELECT ya en contexto)...");
        //Obtenemos un empleado a partir de su ID (clave primaria)
        Optional<Empleado> armandoOpt = empleadoDAO.getById(1);
        armandoOpt.ifPresentOrElse(System.out::println, () -> System.out.println("No se encuentra"));

        System.out.println("\nLe subimos el sueldo a Armando...");
        armandoOpt.ifPresent(empleado -> empleado.setSalario(empleado.getSalario() + 1000));
        System.out.println("\nVemos que efecto tiene en la entidad (no UPDATE, solo en CTX)...");
        //Y vemos que tiene efecto en el contexto (ahora su sueldo es 3000)
        System.out.println(empleadoDAO.getById(1).orElse(null)); //3000

        System.out.println("\nLe subimos el sueldo a Armando otros 600 ...");
        Empleado armando = armandoOpt.orElse(null);
        armando.setSalario(armando.getSalario() + 600);
        System.out.println("Imprimimos...");
        System.out.println(armando);

        //No es necesario porque ya está administrado por el entity manager
        //empleadoDAO.update(armando.orElse(null));

        System.out.println("\nRecuperamos otra referencia a Armando via getById...");
        Empleado empleado = empleadoDAO.getById(1).orElse(null);
        System.out.println("Imprimiendo valor actualizado...");
        System.out.println(empleado); //Podemos comprobar que imprime 3000 en sueldo

        System.out.println("\nHacemos un detach del empleado Armando (se saca del CTX)...");
        //Pero si no está administrado...
        manager.detach(empleado); //Se saca al empleado del contexto, se perdieran los cambios...
        System.out.println("Le ponemos sueldo 1500...");
        empleado.setSalario(1500);
        System.out.println("Imprimiendo desde la referencia detached...");
        System.out.println(empleado); //La referencia al objeto si tiene sueldo 1500
        //Pero si lo obtenemos desde el contexto no
        System.out.println("Imprimiendo valor de una referencia llevada al contexto (SELECT)...");
        System.out.println(empleadoDAO.getById(1).orElse(null));

        System.out.println("\nVolvemos a meter la referencia detached al contexto con merge...");
        //Ahora lo volvemos a poner en el contexto para que sea administrado
        empleadoDAO.update(empleado);
        System.out.println("E imprimimos el valor que leemos desde el contexto");
        System.out.println(empleadoDAO.getById(1).orElse(null));

        System.out.println("\nGuardamos otra referencia a la entidad (no SELECT)...");
        Empleado emp = empleadoDAO.getById(1).orElse(null);
        System.out.println("Subimos el sueldo 10000 (no UPDATE)...");
        emp.setSalario(emp.getSalario() + 10000);
        System.out.println("Imprimimos los datos desde otra referencia traida desde el contexto (no SELECT)...");
        System.out.println(empleadoDAO.getById(1).orElse(null));

        System.out.println("\nEliminando el empleado 1 del contexto (no DELETE)...");
        //Eliminar un empleado
        armandoOpt.ifPresent(empleadoDAO::delete);
        System.out.println("Quedan:(para mostrarlo hay que actualizar la info de la BD)");
        //Para poder hacer esta consulta hay que actualizar la info en la DB
        //Por tanto ahora si que se elimina el empleado, DELETE
        // como es eliminado no es necesario el UPDATE del salario
        empleadoDAO.getAll().forEach(System.out::println);


        //Ahora eliminamos un empleado que no esté en el contexto,
        // funcionará ya que el empleadoDAO primero lo añade si no está en el contexto
        System.out.println("\nEliminando el empleado 2 del contexto...");
        empleadoDAO.delete(new Empleado(2, "", "", 0));
        System.out.println("Quedan: ");
        //De nuevo, la consulta provoca que previamente se actualice la info de la BD (DELETE id=2)
        System.out.println(empleadoDAO.getCount());


        System.out.println("\nCrear una entidad pero no añadirla al contexto...");
        //Si tenemos una referencia a objeto que no está en el contexto no se sigue lo que le pase
        Empleado amador = new Empleado(3, "Amador", "Denador", 1000);
        //Se realiza la SELECT pero no encuentra nada con ID 3
        empleadoDAO.getById(3)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No existe en el contexto el empleado " + 3));

        System.out.println("\nMetemos a Armando en el CTX mediante un merge...(provoca SELECT pq no esta)");
        // Si hay un objeto en el contexto le asigna los valores de la referencia pasada como argumento
        // Si no, trae el objeto al contexto y le asigna los mismos valores que los que tiene el objeto argumento
        empleadoDAO.update(amador); //Internamente llama a merge
        System.out.println("Ahora si que tiene exito el getById...");
        System.out.println(empleadoDAO.getById(3).orElse(null));

        //La referencia sigue sin estar administrada por el entity manager
        amador.setSalario(amador.getSalario() + 300); //Si solo subo el salario, pero no hago de nuevo el merge
        //empleadoDAO.update(amador);
        System.out.println(empleadoDAO.getById(3).orElse(null)); //1000 el valor anterior sin modificar
        amador.setSalario(amador.getSalario() + 400);

        //Si hacemos update, se pasan los valores del objeto argumento al objeto administrado
        empleadoDAO.update(amador);
        //Por eso ahora sí obtenemos el valor último del salario 1000 + 300 + 400
        System.out.println(empleadoDAO.getById(3).orElse(null)); //1700

        //Obtener una referencia administrada (merge devuelve una referencia administrada a la instancia)
        Empleado amadorManaged = manager.merge(amador);
        System.out.println("\nSubiendo el salario a Amador desde una referencia administrada...");
        amadorManaged.setSalario(amadorManaged.getSalario() + 2000);
        System.out.println(empleadoDAO.getById(3).orElse(null)); //3700

        System.out.println("\nConsultamos todos los empleados...");
        //Antes de hacer el SELECT se hará el INSERT de Amador y el UPDATE
        empleadoDAO.getAll().forEach(System.out::println);


        System.out.println("\nCreamos a Pedro y lo añadimos al CTX...");
        //En este caso la referencia al objeto la pasaremos al método add (persist)
        //Se seguirán los cambios que se realizan en la instancia para al final en el commit
        //Realizar una operación INSERT con los últimos valores de lo campos del objeto
        Empleado pedro = new Empleado(4, "Pedro", "Gado", 1200);
        //Hacer un persist sobre una referencia a objeto la convierte en una referencia administrada
        empleadoDAO.add(pedro); //Implica llamar a persist y añadirlo al contexto para administrarlo
        //Como el objeto al que apunta la variable pedro está administrado
        //Sus cambios los podemos ver si obtenemos otra referencia a la misma entidad (empleado con misma ID)
        pedro.setSalario(pedro.getSalario() + 600);
        System.out.println(empleadoDAO.getById(4).orElse(null)); //1800
        pedro.setSalario(pedro.getSalario() + 500);
        System.out.println(empleadoDAO.getById(4).orElse(null)); //2300
        //Como el código termina aquí y después vendrá el commit, ahi se realizaran las operaciones
        //de actualización INSERT, UPDATE o DELETE que hayan quedado pendientes
    }

}
