package dam.ad.jpa.entities;

import dam.ad.dto.annotations.RowField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
public class Pais {
    @RowField(columnLength = 4)
    @Id
    @Column(name = "iso3", columnDefinition = "CHAR(3)")
    private String iso3;

    @RowField(columnLength = 50)
    @Column(length = 50)
    private String nombre;
}
