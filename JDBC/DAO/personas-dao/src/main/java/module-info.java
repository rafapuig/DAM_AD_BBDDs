module personas.dao {
    requires java.sql;
    requires personas.model;
    requires dam.ad.jdbc.DAO;
    requires org.hsqldb;
    requires org.apache.derby.tools;
    requires jdbc.utils;

    opens personas to dam.ad.jdbc.DAO;
}