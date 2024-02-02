package dam.ad.jpa.exercise;

import dam.ad.converters.ObjectArrayRowConverter;
import dam.ad.dto.annotations.RowConvertible;
import dam.ad.headers.GenericHeaderProvider;
import dam.ad.headers.HeaderColumn;
import dam.ad.jpa.entities.Equipo;
import dam.ad.jpa.entities.Jugador;
import dam.ad.jpa.entities.Pais;
import dam.ad.printers.ConsoleColors;
import dam.ad.printers.ObjectArrayPrinter;
import dam.ad.printers.Printers;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Stream;

import static dam.ad.printers.ColorPrinter.in;
import static dam.ad.printers.ConsoleColors.*;

public class QueryFutbolApp {
    public static void main(String[] args) {
        try (EntityManagerFactory factory = Persistence
                .createEntityManagerFactory("ORM-futbol",
                        Map.of("hibernate.show_sql", false));
             EntityManager manager = factory.createEntityManager()) {

            printAllEquipos(manager);
            printDatosEquipoLevante(manager);
            printJugadoresBarcelona(manager);
            printJugadoresRealMadridAlturaSup180(manager);

            printDatosEquipo(manager, "Manchester");

            printJugadoresEquipoAlturaSuperiorPositionParams(manager, "Barcelona", 1.7f);
            printJugadoresEquipoAlturaSuperiorNamedParams(manager, "Real", 1.85f);

            printJugadorMasVeterano(manager);
            printJugadorMasVeteranoYCompañeros(manager);

            printEquiposDePais(manager, "ESP");
            printEquiposDePais2(manager, "ESP");
            printAlturaMediaJugadores(manager);

            printAlturaMediaJugadoresYCantidad(manager);
            printAlturaMediaJugadoresPorEquipo(manager);
            printCantidadJugadoresPorEquipo(manager);
            printAlturaMediaCantidadJugadoresPorEquipo(manager);
            printEquiposEspañolesJueganUCL(manager);
            printEquiposEspañolesJueganUCL2(manager);
            printEquiposEspañolesJueganLigaYNOUCL(manager);
            printEquipoConJugadorMasAlto(manager);
            printEquipoConJugadorMasAlto2(manager);
            printEquipoConAlmenosDosJugadores(manager);

            printPaisCantidadJugadores(manager);
            printPaisConMasJugadores(manager);

            printPaisConMasJugadores2(manager);
            printPaisConMasJugadores3(manager);

            printEquiposCantidadJugadores(manager);
            printEquiposConMasJugadores(manager);

            printJugadoresNoESPJueganUCL(manager);
        }
    }

    private static void printQuery(TypedQuery<?> query) {
        System.out.println(ConsoleColors.YELLOW);
        query.getHints().forEach((key, value) -> System.out.println(value));
        System.out.println(ConsoleColors.RESET);
    }

    /**
     * Obtener todos los equipos
     */
    private static void printAllEquipos(EntityManager manager) {

        manager.clear();

        String jpql = "SELECT e FROM Equipo e";
        //Mas eficiente con EAGER fetch
        //String jpql = "SELECT e FROM Equipo e JOIN FETCH e.pais JOIN FETCH e.entrenador JOIN FETCH e.entrenador.pais";

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);

        printQuery(query);

        Stream<Equipo> equipos = query.getResultStream();

        Printers.getPrinter(Equipo.class).printHeader(true);

        equipos.forEach(equipo -> Printers
                .getPrinter(Equipo.class)
                .printRow(equipo));
    }

    /**
     * Obtener la información del equipo Levante U.D.
     */
    private static void printDatosEquipoLevante(EntityManager manager) {

        manager.clear();

        String jpql = """
                SELECT e
                FROM Equipo e
                WHERE e.nombre LIKE 'Levante%'
                """;

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);

        printQuery(query);

        Equipo equipo = query.getSingleResult();

        System.out.println("DATOS DEL EQUIPO");
        System.out.println(("-".repeat(50)));

        System.out.println("ID:\t" + equipo.getId());
        System.out.println("Nombre:\t" + equipo.getNombre());
        System.out.println("Pais:\t" + equipo.getPais().getNombre());
        System.out.println("Entrenador:\t" + equipo.getEntrenador().getNombre());
    }

    /**
     * Obtener los jugadores del Barcelona
     */
    private static void printJugadoresBarcelona(EntityManager manager) {
        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.equipo.nombre LIKE '%Barcelona%'
                """;

        TypedQuery<Jugador> query = manager.createQuery(jpql, Jugador.class);

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener los jugadores del Real Madrid que superan el 1.80 de altura
     */
    private static void printJugadoresRealMadridAlturaSup180(EntityManager manager) {
        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.altura > 1.80 AND j.equipo.nombre LIKE 'Real Madrid%'
                ORDER BY j.altura DESC
                """;

        TypedQuery<Jugador> query = manager.createQuery(jpql, Jugador.class);

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener datos de un equipo
     *
     * @param nombreEquipo nombre del equipo del que se quiere obtener la info
     */
    private static void printDatosEquipo(EntityManager manager, String nombreEquipo) {
        String jpql = """
                SELECT e
                FROM Equipo e
                WHERE e.nombre LIKE :nombre
                """;

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);

        query.setParameter("nombre", String.format("%%%s%%", nombreEquipo));

        printQuery(query);

        Equipo equipo = query.getSingleResult();

        System.out.println("DATOS DEL EQUIPO");
        System.out.println(("-".repeat(50)));

        System.out.println("ID:\t" + equipo.getId());
        System.out.println("Nombre:\t" + equipo.getNombre());
        System.out.println("Pais:\t" + equipo.getPais().getNombre());
        System.out.println("Entrenador:\t" + equipo.getEntrenador().getNombre());
    }

    /**
     * Obtener los jugadores del equipo aportado como parámetro
     * y con altura superior a la aportada como parámetro
     *
     * @param nombreEquipo nombre del equipo
     * @param alturaMinima altura a superar
     */
    private static void printJugadoresEquipoAlturaSuperiorPositionParams(
            EntityManager manager, String nombreEquipo, float alturaMinima) {

        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.altura >= ?1 AND j.equipo.nombre LIKE ?2
                ORDER BY j.altura DESC
                """;

        TypedQuery<Jugador> query = manager.createQuery(jpql, Jugador.class);
        query.setParameter(1, alturaMinima);
        query.setParameter(2, String.format("%%%s%%", nombreEquipo));

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener los jugadores del equipo aportado como parámetro
     * y con altura superior a la aportada como parámetro
     *
     * @param nombreEquipo nombre del equipo
     * @param alturaMinima altura a superar
     */
    private static void printJugadoresEquipoAlturaSuperiorNamedParams(
            EntityManager manager, String nombreEquipo, float alturaMinima) {

        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.altura >= :altura AND j.equipo.nombre LIKE :nombre
                ORDER BY j.altura DESC
                """;

        TypedQuery<Jugador> query = manager.createQuery(jpql, Jugador.class);
        query.setParameter("altura", alturaMinima);
        query.setParameter("nombre", String.format("%%%s%%", nombreEquipo));

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener el jugador más veterano registrado en la BD
     */
    private static void printJugadorMasVeterano(EntityManager manager) {

        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.nacimiento = (
                    SELECT MIN(j.nacimiento)
                    FROM Jugador j)
                """;

        TypedQuery<Jugador> query = manager.createQuery(jpql, Jugador.class);

        printQuery(query);

        Jugador jugador = query.getSingleResult();

        Printers.getPrinter(Jugador.class).printHeader();
        Printers.getPrinter(Jugador.class).printRow(jugador);
        Printers.getAsRow(jugador);

        System.out.println("El jugador mas veterano es:" +
                           in(CYAN).text(jugador.getNombre()));

        System.out.println("Con una edad de: " +
                           in(CYAN).text(ChronoUnit.YEARS
                                   .between(jugador.getNacimiento(), LocalDate.now())) +
                           " años");
    }


    /**
     * Obtener los compañeros de equipo del jugador más veterano de la BD
     */
    private static void printJugadorMasVeteranoYCompañeros(EntityManager manager) {

        String jpqlJugadorMasVeterano = """
                SELECT j
                FROM Jugador j
                WHERE j.nacimiento IN (
                    SELECT MIN(j.nacimiento)
                    FROM Jugador j)
                """;

        TypedQuery<Jugador> query = manager.createQuery(jpqlJugadorMasVeterano, Jugador.class);

        printQuery(query);

        Jugador masVeterano = query.getSingleResult();

        System.out.println("El jugador mas veterano es " +
                           in(CYAN).text(masVeterano.getNombre()));

        System.out.println("Con una edad de: " +
                           in(CYAN).text(
                                   ChronoUnit.YEARS.between(masVeterano.getNacimiento(), LocalDate.now())) +
                           " años");
        System.out.println("Y juega en el " +
                           in(GREEN).text(masVeterano.getEquipo().getNombre()));

        String jpqlCompañeros = """
                SELECT j
                FROM Jugador j
                WHERE j.equipo = :equipo AND j NOT IN :mas_veterano
                """;

        TypedQuery<Jugador> queryCompañeros = manager.createQuery(jpqlCompañeros, Jugador.class);
        queryCompañeros.setParameter("equipo", masVeterano.getEquipo());
        queryCompañeros.setParameter("mas_veterano", masVeterano);

        printQuery(queryCompañeros);

        System.out.println("COMPAÑEROS DE EQUIPO");

        Printers.print(queryCompañeros.getResultStream());
    }

    /**
     * Obtener los equipos del pais pasado como parámetro
     * @param iso3Pais código ISO3 del pais
     */
    private static void printEquiposDePais(EntityManager manager, String iso3Pais) {

        String jpql = """
                SELECT e
                FROM Equipo e JOIN e.pais p WHERE p.iso3 = :iso3
                """;

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);
        query.setParameter("iso3", iso3Pais);

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener los equipos del pais pasado como parámetro
     *
     * @param iso3Pais código ISO3 del pais
     */
    private static void printEquiposDePais2(EntityManager manager, String iso3Pais) {

        String jpqlQueryPais = "SELECT p FROM Pais p WHERE p.iso3 = :iso3";

        TypedQuery<Pais> queryPais = manager.createQuery(jpqlQueryPais, Pais.class);
        queryPais.setParameter("iso3", iso3Pais);

        Pais pais = queryPais.getSingleResult();

        String jpql = """
                SELECT e
                FROM Equipo e JOIN e.pais p WHERE p = :pais
                """;

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);

        query.setParameter("pais", pais);

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener la altura media de todos los jugadores de la BD
     */
    private static void printAlturaMediaJugadores(EntityManager manager) {
        String jpql = """
                SELECT AVG(j.altura)
                FROM Jugador j
                """;

        TypedQuery<Float> query = manager.createQuery(jpql, Float.class);

        printQuery(query);

        System.out.println("Altura media: " + in(CYAN).text(query.getSingleResult()));
    }

    /**
     * Obtener la altura media y el número de jugadores de la BD
     */
    private static void printAlturaMediaJugadoresYCantidad(EntityManager manager) {
        String jpql = """
                SELECT AVG(j.altura), COUNT(j)
                FROM Jugador j
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        Tuple result = query.getSingleResult();

        System.out.println("La altura media de los jugadores es: " + in(CYAN).text(result.get(0)));
        System.out.println("El numero total de jugadores es: " + in(GREEN_BRIGHT).text(result.get(1)));
    }


    /**
     * Obtener la altura media de los jugadores de cada equipo
     */
    private static void printAlturaMediaJugadoresPorEquipo(EntityManager manager) {
        String jpql = """
                SELECT j.equipo.nombre, AVG(j.altura)
                FROM Jugador j
                GROUP BY j.equipo
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        query.getResultStream().forEach(tuple -> {
                    System.out.println("La altura media de los jugadores del " +
                                       in(GREEN).text(tuple.get(0)) +
                                       " es " + in(CYAN).text(tuple.get(1)) + " metros");
                }
        );

        GenericHeaderProvider headerProvider = new GenericHeaderProvider(
                new HeaderColumn("Equipo", 30),
                new HeaderColumn("Altura media", 12)
        );
        ObjectArrayRowConverter converter =
                new ObjectArrayRowConverter(30, 12);

        System.out.println(headerProvider.getHeader());
        query.getResultStream()
                .map(Tuple::toArray)
                .forEach(objects -> System.out.println(converter.getAsRow(objects)));
    }

    /**
     * Obtener el nombre y la cantidad de jugadores de cada equipo
     * en orden descendente por número de jugadores
     */
    private static void printCantidadJugadoresPorEquipo(EntityManager manager) {
        String jpql = """
                SELECT j.equipo.nombre, COUNT (j)
                FROM Jugador j
                GROUP BY j.equipo
                ORDER BY COUNT(j) DESC
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        query.getResultStream().forEach(tuple ->
                System.out.println("El " + in(GREEN).text(tuple.get(0)) +
                                   " tiene " + in(RED_BOLD).text(tuple.get(1)) +
                                   " jugadores en plantilla")
        );

        GenericHeaderProvider headerProvider = new GenericHeaderProvider(
                new HeaderColumn("Equipo", 30),
                new HeaderColumn("#Jugadores", 10)
        );
        ObjectArrayRowConverter converter =
                new ObjectArrayRowConverter(30, 10);

        ObjectArrayPrinter printer = new ObjectArrayPrinter(headerProvider, converter);

        printer.print(query.getResultStream().map(Tuple::toArray));
    }

    /**
     * Obtener el nombre, la cantidad de jugadores y altura media de los jugadores de cada equipo
     * en orden descendente por número de jugadores
     */
    private static void printAlturaMediaCantidadJugadoresPorEquipo(EntityManager manager) {
        String jpql = """
                SELECT j.equipo.nombre, COUNT(j), AVG(j.altura)
                FROM Jugador j
                GROUP BY j.equipo
                ORDER BY COUNT(j) DESC
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        query.getResultStream().forEach(tuple -> {
                    System.out.println("La altura media de los " +
                                       in(RED_BOLD).text(tuple.get(1)) +
                                       " jugadores del " +
                                       in(GREEN_BRIGHT).text(tuple.get(0)) +
                                       " es " + in(CYAN_BRIGHT).text(tuple.get(2)) + " metros");
                }
        );

        ObjectArrayPrinter printer = new ObjectArrayPrinter(
                "Equipo", 30,
                "#Jugadores", 10,
                "Altura Media", 12);

        printer.print(query.getResultStream().map(Tuple::toArray));
    }

    /**
     * Obtener los equipos españoles que juegan la Champions League
     */
    private static void printEquiposEspañolesJueganUCL(EntityManager manager) {
        String jpql = """
                SELECT e
                FROM Equipo e
                WHERE e.pais.iso3 = 'ESP' AND e IN (
                    SELECT e
                    FROM Competicion c JOIN c.equipos e
                    WHERE c.codigo = 'UCL')
                """;

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener los equipos españoles que juegan la Champions League
     */
    private static void printEquiposEspañolesJueganUCL2(EntityManager manager) {
        String jpql = """
                SELECT e
                FROM Competicion c INNER JOIN c.equipos e
                WHERE c.codigo = 'UCL' AND e.pais.iso3 = 'ESP'
                """;

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener los equipos españoles que juegan la Liga española pero no están en la Champions
     */
    private static void printEquiposEspañolesJueganLigaYNOUCL(EntityManager manager) {
        String jpql = """
                SELECT e
                FROM Competicion c INNER JOIN c.equipos e
                WHERE c.codigo = 'PDE' AND e.pais.iso3 = 'ESP' AND NOT e IN (
                    SELECT e
                    FROM Competicion c INNER JOIN c.equipos e
                    WHERE c.codigo = 'UCL')
                """;

        TypedQuery<Equipo> query = manager.createQuery(jpql, Equipo.class);

        printQuery(query);

        Printers.print(query.getResultStream());
    }

    /**
     * Obtener el equipo en el que juega el jugador más alto de la BD
     */
    private static void printEquipoConJugadorMasAlto(EntityManager manager) {
        String jpql = """
                SELECT e.nombre, MAX(j.altura)
                FROM Equipo e INNER JOIN e.jugadores j
                group by e
                HAVING MAX(j.altura) = (SELECT MAX(j.altura) FROM Jugador j)
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        ObjectArrayPrinter printer =
                new ObjectArrayPrinter("Equipo", 30, "Altura", 6);

        printer.print(query.getResultStream().map(Tuple::toArray));
    }

    /**
     * Obtener el equipo en el que juega el jugador más alto de la BD
     */
    private static void printEquipoConJugadorMasAlto2(EntityManager manager) {
        String jpql = """
                SELECT j.equipo.nombre, MAX(j.altura)
                FROM Equipo e INNER JOIN e.jugadores j
                WHERE j.altura = (SELECT MAX(j.altura) FROM Jugador j)
                GROUP BY j.equipo
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        ObjectArrayPrinter printer =
                new ObjectArrayPrinter("Equipo", 30, "Altura", 6);

        printer.print(query.getResultStream().map(Tuple::toArray));
    }


    /**
     * Obtener los equipos que tienen al menos dos jugadores en plantilla
     * ordenados de mayor a menor número de jugadores
     */
    private static void printEquipoConAlmenosDosJugadores(EntityManager manager) {
        String jpql = """
                SELECT e.nombre, COUNT (j)
                FROM Equipo e INNER JOIN e.jugadores j
                GROUP BY e
                HAVING COUNT (j) >= 2
                ORDER BY COUNT(j) DESC
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        ObjectArrayPrinter printer =
                new ObjectArrayPrinter("Equipo", 30, "#Jugadores", 10);

        printer.print(query.getResultStream().map(Tuple::toArray));
    }

    /**
     * Obtener el nombre del pais y la cantidad de jugadores que tengan esa nacionalidad
     */
    private static void printPaisCantidadJugadores(EntityManager manager) {
        String jpql = """
                SELECT n.nombre AS pais, COUNT (j) AS jugadores
                FROM Jugador j JOIN j.nacionalidades n
                GROUP BY n
                ORDER BY COUNT (j) DESC
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        ObjectArrayPrinter printer =
                new ObjectArrayPrinter("Pais", 32, "#Jugadores", 10);

        printer.print(query.getResultStream().map(Tuple::toArray));

        /*query.getResultStream().forEach(tuple ->
                        System.out.println(
                                tuple.getElements().get(1).getAlias() + " " + tuple.get(tuple.getElements().getFirst())));*/
    }

    /**
     * Obtener el nombre del pais que tiene la mayor cantidad de jugadores con esa nacionalidad
     */
    private static void printPaisConMasJugadores(EntityManager manager) {
        String jpql = """
                SELECT n.nombre, COUNT (j)
                FROM Jugador j JOIN j.nacionalidades n
                GROUP BY n                
                ORDER BY COUNT (j) DESC
                LIMIT 1           
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);
        //query.setMaxResults(1);
        printQuery(query);

        Printers.print(query.getResultStream().map(Tuple::toArray),
                "Pais",32,"#Jugadores",10);
    }

    /**
     * Obtener el nombre del pais que tiene la mayor cantidad de jugadores con esa nacionalidad
     */
    private static void printPaisConMasJugadores2(EntityManager manager) {
        String jpql = """
                SELECT p.nombre, COUNT (j)
                FROM Jugador j JOIN j.nacionalidades n JOIN Pais p ON p = n
                GROUP BY p 
                HAVING COUNT(j) >= ALL (
                    SELECT COUNT(j)                    
                    FROM Jugador j JOIN j.nacionalidades n 
                    GROUP BY n)          
                ORDER BY COUNT (j) DESC                      
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        Printers.print(query.getResultStream().map(Tuple::toArray),
                "Pais",32,"#Jugadores",10);
    }

    /**
     * Obtener el nombre del pais que tiene la mayor cantidad de jugadores con esa nacionalidad
     */
    private static void printPaisConMasJugadores3(EntityManager manager) {
        String jpql = """
                SELECT n.nombre, COUNT (j)
                FROM Jugador j JOIN j.nacionalidades n
                GROUP BY n     
                HAVING COUNT (j) = 
                    (SELECT MAX(m) FROM 
                        (SELECT COUNT (j) AS m 
                        FROM Jugador j JOIN j.nacionalidades n 
                        GROUP BY n))                            
                ORDER BY COUNT (j) DESC                       
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        Printers.print(query.getResultStream().map(Tuple::toArray),
                "Pais",32,"#Jugadores",10);
    }

    @RowConvertible
    public record EquipoCantidadJugadoresDTO(Equipo equipo, Long jugadores) { }

    /**
     * Obtener el nombre de los equipos con su cantidad de jugadores en plantilla
     */
    private static void printEquiposCantidadJugadores(EntityManager manager) {
        String jpql = """
                SELECT e.nombre, COUNT(j)
                FROM Equipo e JOIN e.jugadores j
                GROUP BY e
                ORDER BY COUNT(j) DESC
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        ObjectArrayPrinter printer =
                new ObjectArrayPrinter("Equipo", 32, "#Jugadores", 10);

        printer.print(query.getResultStream().map(Tuple::toArray));
    }

    /**
     * Obtener los nombres de los equipos con mayor número de jugadores
     */
    private static void printEquiposConMasJugadores(EntityManager manager) {
        String jpql = """
                SELECT e.nombre, COUNT(j)
                FROM Equipo e JOIN e.jugadores j
                GROUP BY e
                HAVING COUNT(j) >= ALL (
                    SELECT COUNT(j) 
                    FROM Equipo e JOIN e.jugadores j 
                    GROUP BY e)
                ORDER BY COUNT(j) DESC 
                """;

        TypedQuery<Tuple> query = manager.createQuery(jpql, Tuple.class);

        printQuery(query);

        ObjectArrayPrinter printer =
                new ObjectArrayPrinter("Equipo", 32, "#Jugadores", 10);

        printer.print(query.getResultStream().map(Tuple::toArray));
    }

    /**
     * Obtener los jugadores no españoles cuyos clubs juegan la Champions League
     */

    public static void printJugadoresNoESPJueganUCL(EntityManager manager) {
        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j IN (
                    SELECT DISTINCT j AS dj
                    FROM Competicion c JOIN c.equipos e JOIN e.jugadores j JOIN j.nacionalidades n
                    WHERE c.codigo = 'UCL' AND n NOT IN (
                        SELECT p
                        FROM Pais p
                        WHERE p.iso3 = 'ESP'))
                ORDER BY j.equipo.nombre, j.demarcacion
                """;

        TypedQuery<Jugador> query = manager.createQuery(jpql, Jugador.class);

        printQuery(query);

        Printers.print(query.getResultStream());

        //return Printers.getString(query.getResultStream());
    }


}
