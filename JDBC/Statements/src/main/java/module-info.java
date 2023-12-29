module dam.ad.jdbc.statements {
    requires java.sql;
    requires dam.ad.jdbc.DAO;
    requires jdbc.utils;
    requires personas.model;

    exports dam.ad.jdbc.statements.personas;
    exports dam.ad.jdbc.statements.personas.consumers;
    exports dam.ad.jdbc.statements.personas.query;
    exports dam.ad.jdbc.statements.personas.query.stream;
}