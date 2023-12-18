module dam.ad.jdbc.DAO {
    requires java.sql;
    requires org.hsqldb;
    requires java.naming;
    requires org.apache.derby.tools;

    exports dam.ad.futbol.model;
    exports dam.ad.futbol.file;
    exports dam.ad.file;
}