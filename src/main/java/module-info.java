module com.musicautomator {
    requires transitive javafx.controls;
    requires jaudiotagger;
    requires java.logging;
    requires transitive java.sql;
    requires org.xerial.sqlitejdbc;
    exports com.verafied;
}
