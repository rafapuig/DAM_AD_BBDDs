package dam.ad.jpa;

import dam.ad.jpa.entities.Children;
import dam.ad.jpa.entities.Parent;
import dam.ad.jpa.entities.Parents;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.
                createEntityManagerFactory("ORM-relationships2");

        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();

        Parents.PARENTS.forEach(manager::persist);
        Children.CHILDREN.forEach(manager::persist);

        manager.flush();

        Parents.PARENTS.forEach(manager::detach);
        Children.CHILDREN.forEach(manager::detach);


        Parent p = manager.find(Parent.class, 1);

        System.out.println(p);
        p.getChildren().forEach(System.out::println);



        manager.getTransaction().commit();
        manager.close();
        factory.close();
    }
}
