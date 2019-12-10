package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.UserRole;
import java.sql.*;

public class User {
  public String email;
  public String password;
  public UserRole userRole;
  public String name;

  public User(String email, String password, UserRole userRole, String name) {
    this.email = email;
    this.password = password;
    this.userRole = userRole;
    this.name = name;
  }
  public void submit() {
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    Connection conn = null;
    PreparedStatement stmt = null;
    Statement stmt1 = null;
    try {
      conn = DriverManager.getConnection(connectionUrl);
      String sql = "INSERT INTO users VALUES(?, ? , ?, ?)";
      stmt = conn.prepareStatement(sql);
      stmt.setString(1, this.email);
      stmt.setString(2, this.password);
      stmt.setString(3, this.name);
      stmt.setString(4, this.userRole.name());
      stmt.executeUpdate(sql);
      System.out.println("End");
      stmt.close();
      conn.close();
    } catch (SQLException se) {
      se.printStackTrace();
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
      try {
        if (conn != null)
          conn.close();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
