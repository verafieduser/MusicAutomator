module com.musicautomator {
    requires transitive javafx.controls;
    requires jaudiotagger;
    requires java.logging;
    requires java.naming;
    requires transitive java.sql;
    requires org.hibernate.orm.core;
    requires mysql.connector.j;
    requires jakarta.persistence;
    opens com.verafied to org.hibernate.orm.core;
    exports com.verafied;
}
