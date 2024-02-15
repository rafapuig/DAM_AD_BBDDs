package dam.ad.jpa.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String name;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    List<Child> children;

    public Parent(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
