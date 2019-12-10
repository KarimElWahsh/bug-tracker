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
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    Connection conn = null;
    PreparedStatement stmt = null;
    Statement st1 = null;
    try {
      conn = DriverManager.getConnection(connectionUrl);
      String sql;
      sql = "INSERT INTO dbo.users(email, password, name, role) VALUES(?, ?, ?, ?)";
      stmt = conn.prepareStatement(sql);
      st1 = conn.createStatement();
      stmt.setString(1, this.email);
      stmt.setString(2, this.password);
      stmt.setString(3, this.name);
      stmt.setString(4, this.userRole.name());
      System.out.println("Inserted");
      sql = "SELECT * FROM dbo.users";
      ResultSet rs  = st1.executeQuery(sql);
      while (rs.next()){
        String e = rs.getString("email");
        String p = rs.getString("password");
        String n = rs.getString("name");
        String uR = rs.getString("role");
        System.out.print("ID: " + e);
        System.out.print(", Age: " + p);
        System.out.print(", First: " + n);
        System.out.println(", Last: " + uR);
      }
      stmt.close();
      st1.close();
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
        if (st1 != null)
          st1.close();
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
