module com.musicautomator {
    requires transitive javafx.controls;
    requires jaudiotagger;
    requires java.logging;
    requires transitive java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    exports com.verafied;
}
