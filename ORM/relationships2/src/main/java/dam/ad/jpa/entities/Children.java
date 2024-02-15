package dam.ad.jpa.entities;

import java.util.List;

import static dam.ad.jpa.entities.Parents.*;

public class Children {

    public static final Child C11 = new Child(null, "Child 1.1", P1);
    public static final Child C12 = new Child(null, "Child 1.2", P1);
    public static final Child C21 = new Child(null, "Child 2.1", P2);
    public static final Child C22 = new Child(null, "Child 2.2", P2);
    public static final Child C31 = new Child(null, "Child 3.1", P3);

    public static final List<Child> CHILDREN = List.of(C11, C12, C21, C22, C31);

}
