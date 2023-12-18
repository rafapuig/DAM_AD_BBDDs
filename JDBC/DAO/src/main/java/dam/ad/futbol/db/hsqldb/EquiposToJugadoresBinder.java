package dam.ad.futbol.db.hsqldb;

import dam.ad.futbol.model.Equipo;
import dam.ad.futbol.model.Jugador;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FunctionalInterface
public interface EquiposToJugadoresBinder {

    /**
     * Establece los jugadores de cada equipo
     * Asigna el campo jugadores de un equipo mediante equipo.setJugadores(List<Jugador> l)
     *
     * @param equipos   lista de equipos a vincular con su lista de jugadores
     * @param jugadores lista de jugadores, previamente enlazados con el equipo
     */
    void bindEquiposToJugadores(List<Equipo> equipos, List<Jugador> jugadores);


    // Implementaciones del binder

    static void bindEquiposToJugadores1(List<Equipo> equipos, List<Jugador> jugadores) {
        equipos.forEach(equipo -> equipo.setJugadores(jugadores.stream()
                .filter(jugador -> jugador.getEquipo().equals(equipo))
                .toList()));
    }

    static void bindEquiposToJugadores2(List<Equipo> equipos, List<Jugador> jugadores) {

        Map<Equipo, List<Jugador>> jugadoresByEquipoMap = jugadores.stream()
                .collect(Collectors.groupingBy(Jugador::getEquipo));

        equipos.forEach(equipo ->
        {
            jugadoresByEquipoMap.computeIfPresent(
                    equipo, //Clave: el equipo está presente en el mapa
                    (key, jugadoresList) -> { //Dada una key (equipo) y su value (lista de equipos)
                        key.setJugadores(jugadoresList); //Establecer el campo jugadores del obj Equipo
                        return jugadoresList;   //Como es una BiFunction devolvemos la lista
                    });

            //Si el equipo no está en el mapa es porque no hay jugadores que jueguen en ese equipo
            jugadoresByEquipoMap.computeIfAbsent(
                    equipo, // Si el equipo no esta en el mapa
                    key -> { // Dado ese equipo como Key de la entrada
                        List<Jugador> empty = List.of();
                        key.setJugadores(empty); // Le asignamos una lista vacia de jugadores
                        return empty;
                    });
        });
    }

    static void bindEquiposToJugadores3(List<Equipo> equipos, List<Jugador> jugadores) {

        Map<Equipo, List<Jugador>> jugadoresByEquipoMap = jugadores.stream()
                .collect(Collectors.groupingBy(Jugador::getEquipo));

        // Estas instrucciones, solamente asignarían lista de jugadores a equipos que tienen
        // algún jugador y por eso han sido añadidos como claves en el mapa
        // El resto de equipos devolvería un null si hacemos un getJugadores()

        //jugadoresByEquipoMap.
        //        forEach((equipo, jugadoresList) -> equipo.setJugadores(jugadoresList));

        jugadoresByEquipoMap.
                forEach(Equipo::setJugadores);

        equipos.forEach(equipo ->
        {
            // Eliminamos el computeIfPresent, dado que es lo que hace la instruccion de arriba

            //Si el equipo no está en el mapa es porque no hay jugadores que jueguen en ese equipo
            jugadoresByEquipoMap.computeIfAbsent(
                    equipo, // Si el equipo no esta en el mapa
                    key -> { // Dado ese equipo como Key de la entrada
                        List<Jugador> empty = List.of();
                        key.setJugadores(empty); // Le asignamos una lista vacia de jugadores
                        return empty;
                    });
        });
    }

    static void bindEquiposToJugadores4(List<Equipo> equipos, List<Jugador> jugadores) {

        // Esta vez cuando creamos en mapa aprovechamos para establecer
        // el campo jugadores del objeto equipo
        // Partimos de una lista de jugadores ya vinculados con su equipo

        Map<Equipo, List<Jugador>> jugadoresByEquipoMap = jugadores.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Jugador::getEquipo), // Coleccionamos el mapa
                        map -> {
                            map.forEach(Equipo::setJugadores); //Establecemos los jugadores del equipo
                            return map;
                        }));

        //Ahora hacemos de otra manera que los equipos que no tienen jugadores
        //como tendrán su campo a null podemos filtrar y quedarnos con estos
        //para establecer en lugar de null una lista vacía
        equipos.stream()
                .filter(equipo -> equipo.getJugadores() == null)
                .forEach(equipo -> equipo.setJugadores(List.of()));
    }

    /**
     * En este caso al hacerlo con Streams no hay que tener en cuenta si un equipo no tiene jugadores
     * En ese caso el stream devuelve una lista vacía
     * @param equipos
     * @param jugadores en este caso no es necesario que la los jugadores esten enlazados con su equipo
     */
    static void bindEquiposToJugadores5(List<Equipo> equipos, List<Jugador> jugadores) {
        equipos
                .forEach(equipo -> equipo.setJugadores(jugadores.stream()
                        .filter(jugador -> jugador.getEquipoId() == equipo.getEquipoId())
                        .toList()));
    }

    static void bindEquiposToJugadores6(List<Equipo> equipos, List<Jugador> jugadores) {
        equipos
                .forEach(equipo -> equipo.setJugadores(jugadores.stream()
                        .filter(jugador -> jugador.getEquipo().equals(equipo))
                        .toList()));
    }

}
