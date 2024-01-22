package dam.ad.jpa;

import dam.ad.converters.Converters;
import dam.ad.converters.DefaultDTORowConverter;
import dam.ad.converters.ObjectArrayRowConverter;
import dam.ad.jpa.dto.EquipoNumJugadoresDTO;
import dam.ad.jpa.entities.*;
import dam.ad.printers.DefaultDTOHeaderProvider;
import dam.ad.printers.DefaultDTOPrinter;
import dam.ad.printers.GenericEntityPrinter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class FutbolDemo {

    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence
                .createEntityManagerFactory(
                        "ORM-futbol",
                        Map.of("hibernate.show_sql", false));

        EntityManager manager = factory.createEntityManager();

        // -------- Consultas con resultado entidad

        //queryAllEntitiesEntrenador(manager);

        //querySimpleAttributeFromEntityEntrenador(manager);

        //queryEntityRelationshipAttribute(manager);

        //queryCollectionSimpleAttribute(manager);

        // ---------- Consultas con filtro WHERE

        //queryWhereExpresion(manager);
        //queryWhereExpresion2(manager);

        //queryFilterWhereAndSubquery(manager);

        // query1(manager);

        //-------- Consultas de proyección (devuelven DTOs, Object[], o Tuple)

        //queryProjectionObjectArray(manager);

        //queryProjectionTuple(manager);

        // ----------------- Join

        //queryJoin1(manager);

        //queryJoinProjection(manager);

        //*************************** Registrar los converters en un converter manager y asi hacerlo recursivamente

        //queryJoinProjection2(manager);

        //queryJoinProjectionFilter(manager);



        //-------- Agregados

        //queryAggregate(manager);



        //----------------- Parámetros

        //queryJoinProjectionFilterParameters(manager);

        //----------------- Proyección a DTO

        String jpql = """
                SELECT new dam.ad.jpa.dto.EquipoNumJugadoresDTO(e, COUNT(j))
                FROM Equipo e INNER JOIN e.jugadores j 
                GROUP BY e    
                """;

        TypedQuery<EquipoNumJugadoresDTO> getEquiposNumJugadores =
                manager.createQuery(jpql, EquipoNumJugadoresDTO.class);


        Converters.registerConverter(Equipo.class,
                new DefaultDTORowConverter<>(Equipo.class,2, 30, 20));

        Converters.registerConverter(Pais.class,
                new DefaultDTORowConverter<>(Pais.class,0, 20));

        DefaultDTOPrinter<EquipoNumJugadoresDTO> printer =
                new DefaultDTOPrinter<>(EquipoNumJugadoresDTO.class, 60, 10);

        printer.print(getEquiposNumJugadores.getResultStream());


       /* getJugadoresEquipos.getResultStream()
                .map(converter::getAsRow)
                .forEach(System.out::println);*/


        String jpql2 = """
                SELECT e.id, e.nombre, e.pais.nombre, e.entrenador.nombre, count(e.id)
                FROM Equipo e join e.jugadores 
                GROUP BY e.id, e.nombre, e.pais.nombre, e.entrenador.nombre          
                """;

        //testAllEntities(manager);
        //testAddEquipo(manager);
        //testAddEquipo2(manager);
        //testAddEquipo3(manager);
        //testAddJugador(manager);

        manager.close();
        factory.close();
    }

    private static void queryJoinProjectionFilterParameters(EntityManager manager) {
        String jpql = """
                SELECT j, e.nombre
                FROM Equipo e INNER JOIN e.jugadores j  
                WHERE e.nombre LIKE :nombre AND j.demarcacion = :demarcacion
                """;

        TypedQuery<Tuple> getJugadoresEquipos =
                manager.createQuery(jpql, Tuple.class);

        getJugadoresEquipos.setParameter("nombre", "Real%");
        getJugadoresEquipos.setParameter("demarcacion", Demarcacion.DELANTERO);

        DefaultDTORowConverter<Jugador> jugadorDefaultDTORowConverter =
                new DefaultDTORowConverter<>(Jugador.class, 2, 30, 11, 15);

        getJugadoresEquipos.getResultStream()
                .map(tuple -> jugadorDefaultDTORowConverter.getAsRow((Jugador) tuple.get(0)) + " " + tuple.get(1))
                .forEach(System.out::println);
    }

    private static void queryAggregate(EntityManager manager) {
        String jpql = """
                SELECT e.nombre, count(j), MIN(j.nacimiento), MAX(j.nacimiento)
                FROM Equipo e INNER JOIN e.jugadores j     
                GROUP BY e  
                HAVING COUNT(j) > 1  
                """;

        TypedQuery<Tuple> getJugadoresEquipos =
                manager.createQuery(jpql, Tuple.class);

        ObjectArrayRowConverter converter = new ObjectArrayRowConverter(30, 4, 11, 11);

        getJugadoresEquipos.getResultStream()
                .map(Tuple::toArray)
                .map(converter::getAsRow)
                .forEach(System.out::println);
    }

    private static void queryJoinProjectionFilter(EntityManager manager) {
        String jpql = """
                SELECT j, e.nombre
                FROM Equipo e INNER JOIN e.jugadores j  
                WHERE e.nombre LIKE 'Real%' AND j.demarcacion = dam.ad.jpa.entities.Demarcacion.CENTROCAMPISTA
                """;

        TypedQuery<Tuple> getJugadoresEquipos =
                manager.createQuery(jpql, Tuple.class);

        DefaultDTORowConverter<Jugador> jugadorDefaultDTORowConverter =
                new DefaultDTORowConverter<>(Jugador.class, 2, 30, 11, 15);

        getJugadoresEquipos.getResultStream()
                .map(tuple -> jugadorDefaultDTORowConverter.getAsRow((Jugador) tuple.get(0)) + " " + tuple.get(1))
                .forEach(System.out::println);
    }

    private static void queryJoinProjection2(EntityManager manager) {
        String jpql = """
                SELECT j, e.nombre
                FROM Equipo e INNER JOIN e.jugadores j  
                """;

        TypedQuery<Tuple> getJugadoresEquipos =
                manager.createQuery(jpql, Tuple.class);

        ObjectArrayRowConverter converter = new ObjectArrayRowConverter(30, 30);

        getJugadoresEquipos.getResultStream()
                .map(Tuple::toArray)
                .map(converter::getAsRow)
                .forEach(System.out::println);


        DefaultDTOPrinter<Jugador> jugadorDefaultDTOPrinter =
                new DefaultDTOPrinter<>(Jugador.class, 2, 30, 11, 15);

        jugadorDefaultDTOPrinter.print(
                getJugadoresEquipos.getResultStream()
                        .map(tuple -> (Jugador) tuple.get(0)));


        DefaultDTORowConverter<Jugador> jugadorDefaultDTORowConverter =
                new DefaultDTORowConverter<>(Jugador.class, 2, 30, 11, 15);

        getJugadoresEquipos.getResultStream()
                .map(tuple -> jugadorDefaultDTORowConverter.getAsRow((Jugador) tuple.get(0)) + " " + tuple.get(1))
                .forEach(System.out::println);
    }

    private static void queryJoinProjection(EntityManager manager) {
        String jpql = """
                SELECT j.nombre, e.nombre
                FROM Equipo e INNER JOIN e.jugadores j  
                """;

        TypedQuery<Tuple> getJugadoresEquipos =
                manager.createQuery(jpql, Tuple.class);

        ObjectArrayRowConverter converter = new ObjectArrayRowConverter(30, 30);

        getJugadoresEquipos.getResultStream()
                .map(Tuple::toArray)
                .map(converter::getAsRow)
                .forEach(System.out::println);


        DefaultDTOPrinter<Jugador> jugadorDefaultDTOPrinter =
                new DefaultDTOPrinter<>(Jugador.class, 2, 30, 11, 15);

        jugadorDefaultDTOPrinter.print(
                getJugadoresEquipos.getResultStream()
                        .map(tuple -> (Jugador) tuple.get(0)));
    }

    private static void queryJoin1(EntityManager manager) {
        String jpql = """
                SELECT j
                FROM Equipo e INNER JOIN e.jugadores j      
                """;

        TypedQuery<Tuple> getJugadoresEquipos =
                manager.createQuery(jpql, Tuple.class);

        DefaultDTOPrinter<Jugador> jugadorDefaultDTOPrinter =
                new DefaultDTOPrinter<>(Jugador.class, 2, 30, 11, 15);

        jugadorDefaultDTOPrinter.print(
                getJugadoresEquipos.getResultStream()
                        .map(tuple -> (Jugador) tuple.get(0)));
    }

    private static void queryCollectionSimpleAttribute(EntityManager manager) {

        String jpql = """
                SELECT e.jugadores
                FROM Equipo e                 
                """;

        TypedQuery<Tuple> getJugadoresEquipos =
                manager.createQuery(jpql, Tuple.class);


        DefaultDTORowConverter<Jugador> jugadorDefaultDTORowConverter =
                new DefaultDTORowConverter<>(Jugador.class, 2, 30, 11, 15);

        DefaultDTOHeaderProvider<Jugador> jugadorDefaultDTOHeaderProvider =
                new DefaultDTOHeaderProvider<>(Jugador.class, 2, 30, 11, 15);

        System.out.println(jugadorDefaultDTOHeaderProvider.getHeader());
        System.out.println(jugadorDefaultDTOHeaderProvider.getLine());

        getJugadoresEquipos.getResultStream()
                .map(tuple -> (Jugador) tuple.get(0))
                .map(jugadorDefaultDTORowConverter::getAsRow)
                .forEach(System.out::println);


        System.out.println("====================================================================");

        DefaultDTOPrinter<Jugador> jugadorDefaultDTOPrinter =
                new DefaultDTOPrinter<>(Jugador.class, 2, 30, 11, 15);

        jugadorDefaultDTOPrinter.printHeader();

        getJugadoresEquipos.getResultStream()
                .map(tuple -> tuple.get(0))
                .map(o -> (Jugador) o)
                .forEach(jugadorDefaultDTOPrinter::printRow);

        System.out.println("=====================================================================");

        jugadorDefaultDTOPrinter.print(
                getJugadoresEquipos.getResultStream()
                        .map(tuple -> tuple.get(0))
                        .map(o -> (Jugador) o)
        );
    }

    private static void queryProjectionTuple(EntityManager manager) {
        String jpql = """
                SELECT e.id, e.nombre, e.pais.nombre, e.entrenador.nombre
                FROM Equipo e                    
                """;

        TypedQuery<Tuple> getEquipoInfo =
                manager.createQuery(jpql, Tuple.class);

        getEquipoInfo.getResultStream()
                .forEach(tuple -> System.out.println(tuple.get(0) + " " + tuple.get(1) + " " + tuple.get(2)));


        ObjectArrayRowConverter converter = new ObjectArrayRowConverter(2, -25, -15, -30);

        getEquipoInfo.getResultStream()
                .map(Tuple::toArray)
                .map(converter::getAsRow)
                .forEach(System.out::println);
    }

    private static void queryProjectionObjectArray(EntityManager manager) {
        String jpql = """
                SELECT e.id, e.nombre, e.pais.nombre, e.entrenador.nombre
                FROM Equipo e                      
                """;

        TypedQuery<Object[]> getEquipoInfo =
                manager.createQuery(jpql, Object[].class);

        getEquipoInfo.getResultStream()
                .forEach(System.out::println);

        ObjectArrayRowConverter converter = new ObjectArrayRowConverter(2, -25, -15, -30);

        getEquipoInfo.getResultStream()
                .map(converter::getAsRow)
                .forEach(System.out::println);
    }

    private static void queryWhereExpresion(EntityManager manager) {
        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.equipo.nombre LIKE 'Real Madrid%'
                """;

        TypedQuery<Jugador> getJugadoresEquiposESP =
                manager.createQuery(jpql, Jugador.class);

        getJugadoresEquiposESP.getResultStream().forEach(System.out::println);
    }

    private static void queryWhereExpresion2(EntityManager manager) {
        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.equipo.pais.iso3 = 'ESP'
                """;

        TypedQuery<Jugador> getJugadoresEquiposESP =
                manager.createQuery(jpql, Jugador.class);

        getJugadoresEquiposESP.getResultStream().forEach(System.out::println);
    }

    private static void queryFilterWhereAndSubquery(EntityManager manager) {
        String jpql = """
                SELECT j
                FROM Jugador j
                WHERE j.equipo.nombre = 'Real Madrid C.F.' AND              
                    'BRA' IN (SELECT n.iso3 FROM j.nacionalidades n)
                """;

        TypedQuery<Jugador> getJugadoresBrasilRealMadrid =
                manager.createQuery(jpql, Jugador.class);

        getJugadoresBrasilRealMadrid.getResultStream().forEach(System.out::println);
    }

    private static void queryEntityRelationshipAttribute(EntityManager manager) {
        String jpql = """
                SELECT j.equipo
                FROM Jugador j
                """;

        TypedQuery<Equipo> getEquipoJugador =
                manager.createQuery(jpql, Equipo.class);

        getEquipoJugador.getResultStream().forEach(System.out::println);
    }

    private static void querySimpleAttributeFromEntityEntrenador(EntityManager manager) {
        String jpql = """
                SELECT e.nombre
                FROM Entrenador e
                """;

        TypedQuery<String> getNombresEntrenadores =
                manager.createQuery(jpql, String.class);

        getNombresEntrenadores.getResultStream().forEach(System.out::println);
    }

    private static void queryAllEntitiesEntrenador(EntityManager manager) {
        String jpql = """
                SELECT e
                FROM Entrenador e
                """;

        TypedQuery<Entrenador> getAllEntrenadores =
                manager.createQuery(jpql, Entrenador.class);

        getAllEntrenadores.getResultStream().forEach(System.out::println);

        GenericEntityPrinter.print(getAllEntrenadores.getResultList(), 2, 30, 3, 10);
    }

    private static void query1(EntityManager manager) {
        String jpql = """
                SELECT e.jugadores
                FROM Equipo e
                """;


        TypedQuery<Jugador> getJugadoresEquipos =
                manager.createQuery(jpql, Jugador.class);

        GenericEntityPrinter.print(
                getJugadoresEquipos.getResultList(), 2, 20, 10, 15, 20, 20, 6);
    }

    private static void testAllEntities(EntityManager manager) {
        TypedQuery<Pais> paises = manager
                .createQuery("select p from Pais p order by p.nombre", Pais.class);

        paises.getResultStream()
                .forEach(System.out::println);


        TypedQuery<Entrenador> entrenadores = manager
                .createQuery("select e from Entrenador e", Entrenador.class);

        entrenadores.getResultList() // Como cambia entre List y Stream
                .forEach(System.out::println);

        TypedQuery<Equipo> equipos = manager
                .createQuery("select e from Equipo e", Equipo.class);

        equipos.getResultList() // Como cambia entre List y Stream
                .forEach(System.out::println);

        TypedQuery<Jugador> jugadores = manager
                .createQuery("select j from Jugador j", Jugador.class);

        jugadores.getResultList() // Como cambia entre List y Stream
                .forEach(System.out::println);
    }

    private static void testAddJugador(EntityManager manager) {
        manager.getTransaction().begin();

        Pais pais = new Pais();
        pais.setIso3("HRV");
        pais.setNombre("Croacia");

        TypedQuery<Equipo> query =
                manager.createQuery(
                        "SELECT e FROM Equipo e WHERE e.nombre LIKE :nombre",
                        Equipo.class);

        query.setParameter("nombre", "Real Madrid%");
        Equipo equipo = query.getSingleResult();
        System.out.println(equipo);
        manager.merge(equipo);


        Jugador jugador = new Jugador();
        jugador.setNombre("Luka Modric");
        jugador.setNacionalidades(Set.of(pais));
        jugador.setNacimiento(
                LocalDate.of(1985, 9, 9));
        jugador.setDorsal(10);
        jugador.setDemarcacion(Demarcacion.CENTROCAMPISTA);
        jugador.setEquipo(equipo);
        equipo.getJugadores().add(jugador);

        manager.persist(jugador);
        manager.merge(pais);
        manager.merge(equipo);

        manager.getTransaction().commit();

        TypedQuery<Jugador> jugadores =
                manager.createQuery(
                        "SELECT j FROM Jugador j",
                        Jugador.class);

        jugadores.getResultStream()
                .map(Jugador::getEquipo)
                .forEach(equipo1 -> System.out.println(equipo1.getNombre()));


        testQueryAllEquipos(manager);
    }


    private static void testQueryAllEquipos(EntityManager manager) {
        TypedQuery<Equipo> query =
                manager.createQuery("SELECT e from Equipo e", Equipo.class);

        query.getResultStream().forEach(System.out::println);
    }


    private static void testAddEquipo(EntityManager manager) {
        manager.getTransaction().begin();

        Pais pais = new Pais();
        pais.setIso3("ESP");
        pais.setNombre("España");

        Equipo equipo = new Equipo();
        equipo.setNombre("Leñeros F.C.");
        equipo.setPais(pais);

        manager.merge(pais);
        manager.persist(equipo);

        manager.getTransaction().commit();
    }

    private static void testAddEquipo2(EntityManager manager) {
        manager.getTransaction().begin();

        Pais pais = new Pais();
        pais.setIso3("ESP");
        pais.setNombre("España");

        Equipo equipo = new Equipo();
        equipo.setNombre("F.C. Barcelona");
        equipo.setPais(pais);

        manager.merge(pais);
        manager.persist(equipo);

        manager.getTransaction().commit();
    }

    private static void testAddEquipo3(EntityManager manager) {
        Equipo equipo = new Equipo();
        equipo.setNombre("Atletico de Madrid");
        equipo.setPais(manager.find(Pais.class, "ESP"));
        addEquipo(manager, equipo);
    }

    static void addEquipo(EntityManager manager, Equipo equipo) {
        manager.getTransaction().begin();

        manager.merge(equipo);

        manager.getTransaction().commit();
    }

}
