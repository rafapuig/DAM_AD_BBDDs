package dam.ad.jpa.entities;

import java.util.List;

public class Parents {

    public static final Parent P1 = new Parent(null, "Parent 1");
    public static final Parent P2 = new Parent(null, "Parent 2");
    public static final Parent P3 = new Parent(null, "Parent 3");

    public static final List<Parent> PARENTS = List.of(P1, P2, P3);
}
