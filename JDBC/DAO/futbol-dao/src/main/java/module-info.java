module futbol.jdbc.dao {
    requires dam.ad.jdbc.DAO;
    requires futbol.model;
    requires java.sql;
    requires org.apache.derby.tools;
    requires org.hsqldb;
    requires java.naming;
    requires jdbc.utils;
    requires entity.utils;

    opens futbol to dam.ad.jdbc.DAO;
}