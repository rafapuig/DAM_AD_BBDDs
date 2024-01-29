package dam.ad.jpa;

import dam.ad.headers.DefaultDTOHeaderProvider;
import dam.ad.headers.HeaderProvider;
import dam.ad.jpa.entities.Jugador;

public class HeaderDemo {

    public static void main(String[] args) {

        HeaderProvider headerProvider = new DefaultDTOHeaderProvider<>(Jugador.class, 0,0,0,0,20,20);
        System.out.println(headerProvider.getHeader());

        HeaderProvider h2 = new DefaultDTOHeaderProvider<>(Jugador.class);
        System.out.println(h2.getHeader());


    }
}
