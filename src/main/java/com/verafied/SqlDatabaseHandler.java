package com.verafied;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sqlite.SQLiteConfig;

public class SqlDatabaseHandler {

  public static final String DB_URL = "jdbc:sqlite:";
  public static final String DRIVER = "org.sqlite.JDBC";
  private Connection connection = null;
  

  public SqlDatabaseHandler(SettingsHandler settings) {
    String url = settings.get("user.app.path");
    boolean databaseInitialized = Boolean.parseBoolean(settings.get("database.initialized"));

    try {
      Class.forName(DRIVER);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      connection = DriverManager.getConnection(DB_URL + url + "/demo.db", config.toProperties());

      if(!databaseInitialized){
        initialize();
        settings.set("database.initialized", "true");
      }
    } catch (SQLException e) {
      // if the error message is "out of memory",
      // it probably means no database file is found
      e.printStackTrace();
    } finally {
      try {
        if (connection != null)
          connection.close();
      } catch (SQLException e) {
        // connection close failed.
        e.printStackTrace();
      }
    }
  }

  public boolean initialize() {
    try (Statement statement = connection.createStatement()) {
      statement.setQueryTimeout(30); // set timeout to 30 sec.
      statement.executeUpdate("DROP TABLE IF EXISTS artist");
      statement.executeUpdate("DROP TABLE IF EXISTS album");
      statement.executeUpdate("DROP TABLE IF EXISTS song");
      List<String> schemas = new ArrayList<>();
      // For every file in src/schema, import their schema to the database
      Arrays.asList(Paths.get("src/sql/schema").toFile().listFiles()).forEach(x -> {
        try {
          schemas.add(new String(Files.readAllBytes(x.toPath()), StandardCharsets.UTF_8));
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      for (String schema : schemas) {
        statement.executeUpdate(schema);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Generic method for retrieving all data resulting in a String
   * @param sql
   * @return
   */
  public ResultSet get(String sql) {
    try (Statement statement = connection.createStatement()) {
      return statement.executeQuery(sql);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void update(String sql){
    try (Statement statement = connection.createStatement()){
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
