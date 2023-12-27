package dam.ad.futbol.db.hsqldb;

import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FutbolEntityManager {
    List<Jugador> jugadores;
    List<Equipo> equipos;


    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }




    /**
     * Crear un mapa de equipos a partir de una lista de equipos, con entradas
     * cuya KEY es el campo ID entero del equipo
     * y cuyo VALUE es el objeto Equipo en sí
     */
    static Map<Integer, Equipo> getEquiposMap(List<Equipo> equipos) {
        return equipos.stream()
                .collect(Collectors
                        .toMap(
                                Equipo::getEquipoId, // key Integer ID del equipo
                                Function.identity())); // value referencia al objeto Equipo
    }

    static void bindJugadoresToEquipo(List<Jugador> jugadores, Map<Integer, Equipo> equiposMap) {
        // Asignar a cada jugador la referencia al equipo en el que juega a partir de su ID
        // Se establece el campo equipo del objeto jugador con jugador.setEquipo(Equipo e)
        jugadores
                .forEach(jugador -> jugador.setEquipo(
                        equiposMap.get(jugador.getEquipoId()))); //O(1) con el mapa de equipos
    }

    static void bindEquiposJugadores(List<Equipo> equipos, List<Jugador> jugadores) {
        var equiposMap = getEquiposMap(equipos);
        bindJugadoresToEquipo(jugadores, equiposMap); // Los jugadores de la lista quedan enlazados
        EquiposToJugadoresBinder.bindEquiposToJugadores4(equipos, jugadores); // Enlaza los equipos con su lista de jugadores
    }

    static void bindEquiposJugadores(List<Equipo> equipos, List<Jugador> jugadores, EquiposToJugadoresBinder binder) {
        var equiposMap = getEquiposMap(equipos);
        bindJugadoresToEquipo(jugadores, equiposMap); // Los jugadores de la lista quedan enlazados
        binder.bindEquiposToJugadores(equipos, jugadores); // Enlaza los equipos con su lista de jugadores
    }


    private Map<Integer, Equipo> getEquiposMap() {
        return getEquiposMap(this.getEquipos());
    }

    private void bindJugadoresToEquipo() {
        bindJugadoresToEquipo(jugadores, getEquiposMap());
    }

    private void bindEquiposToJugadores() {
        EquiposToJugadoresBinder.bindEquiposToJugadores4(equipos, jugadores);
    }

    public void bindEquiposJugadores() {
        bindJugadoresToEquipo();
        bindEquiposToJugadores();
    }



}
