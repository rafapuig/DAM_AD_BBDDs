module dam.ad.jdbc.DAO {
    requires java.sql;
    requires org.hsqldb;
    requires java.naming;
    requires org.apache.derby.tools;

    requires jdbc.utils;


    exports dam.ad.file;
    exports dam.ad.dao;
    exports dam.ad.dao.jdbc;
}