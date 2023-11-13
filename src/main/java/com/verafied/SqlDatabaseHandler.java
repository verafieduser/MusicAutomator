package com.verafied;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
//TODO: implement
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlDatabaseHandler {

  private Connection connection = null;

  public SqlDatabaseHandler(String url) {
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:" + url + "/demo.db");
      initialize();
    } catch (SQLException | IOException e) {
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

  public boolean initialize() throws IOException {
    try (Statement statement = connection.createStatement()) {
      statement.setQueryTimeout(30); // set timeout to 30 sec.
      statement.executeUpdate("DROP TABLE IF EXISTS artist");
      statement.executeUpdate("DROP TABLE IF EXISTS album");
      statement.executeUpdate("DROP TABLE IF EXISTS song");
      List<String> schemas = new ArrayList<>();
      //For every file in src/schema, import their schema to the database
      Arrays.asList(Paths.get("src/sql/schema").toFile().listFiles()).forEach(x -> {
        try {
          schemas.add(new String(Files.readAllBytes(x.toPath()), StandardCharsets.UTF_8));
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      for(String schema : schemas){
        statement.executeUpdate(schema);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean update(Song song) {
    try (Statement statement = connection.createStatement()) {
      statement.setQueryTimeout(30); // set timeout to 30 sec.
      statement.executeUpdate("insert into person values(1, 'leo')");
      statement.executeUpdate("insert into person values(2, 'yui')");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;

  }

  public String get(String search) {
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery("select * from person");
      while (rs.next()) {
        // read the result set
        System.out.println("name = " + rs.getString("name"));
        System.out.println("id = " + rs.getInt("id"));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "";
  }

}
