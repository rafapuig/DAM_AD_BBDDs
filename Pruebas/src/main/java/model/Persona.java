package model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class Persona {
    String nombre;
    String apellidos;

    public static void main(String[] args) {
        Persona.PersonaBuilder builder = Persona.builder();
        Persona p = builder
                .nombre("Armando")
                .apellidos("Bronca")
                .build();

        p.setNombre("Armando");
        p.setApellidos("Bronca");
        System.out.println(p);

        p = p.withNombre("Amador");
        System.out.println(p);
    }
}
