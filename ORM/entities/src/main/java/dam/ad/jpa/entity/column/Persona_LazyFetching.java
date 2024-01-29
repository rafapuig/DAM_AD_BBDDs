package dam.ad.jpa.entity.column;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyGroup;
import org.hsqldb.jdbc.JDBCBlob;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Access(AccessType.FIELD)
public class Persona_LazyFetching {
    @Id private int id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    @Setter
    @ToString.Exclude
    @Transient
    private Blob fotoBlob;

    @Basic(fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @Lob
    @LazyGroup("lobs")
    public Blob getFotoBlob() {
        return fotoBlob;
    }

    public void setFoto(byte[] foto) {
        try {
            fotoBlob = new JDBCBlob(foto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getFoto() {
        try {
            return getFotoBlob().getBinaryStream().readAllBytes();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
