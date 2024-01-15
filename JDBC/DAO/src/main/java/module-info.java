module dam.ad.jdbc.DAO {
    requires java.sql;
    requires org.hsqldb;
    requires java.naming;
    requires org.apache.derby.tools;
    requires futbol.model;
    requires personas.model;
    requires jdbc.utils;
    requires entity.utils;

    exports dam.ad.futbol.file;
    exports dam.ad.file;

    opens dam.ad.futbol.db.hsqldb.version2;
    exports dam.ad.dao;
}