package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.UserRole;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dev.omaremara.bugtracker.model.exception.LoginException;
import dev.omaremara.bugtracker.model.exception.DataBaseException;

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
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "INSERT INTO users VALUES(?, ? , ?, ?)";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, this.email);
        stmt.setString(2, this.password);
        stmt.setString(3, this.name);
        stmt.setString(4, this.userRole.name());
        int rows = stmt.executeUpdate();
        System.out.println("End");
      } catch (SQLException se) {
        se.printStackTrace();
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return this.email;
  }

  public static List<User> getAllDevelopers() {
    List<User> Alldevelopers = new ArrayList<User>();
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "SELECT * FROM users WHERE role = 'DEVELOPER'";
      try (Statement stmt = conn.createStatement()) {
        try (ResultSet rs = stmt.executeQuery(sql)) {
          while (rs.next()) {
            User developer = new User(
                rs.getString("email"), rs.getString("passowrd"),
                UserRole.valueOf(rs.getString("role")), rs.getString("name"));
            Alldevelopers.add(developer);
          }
        } catch (SQLException se) {
          se.printStackTrace();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }
    return Alldevelopers;
  }

  public static User getFromLogin(String email, String password)
      throws LoginException, DataBaseException {
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    User user = null;
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "SELECT * FROM users WHERE email = ? AND passowrd = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, email);
        stmt.setString(2, password);
        boolean ret = stmt.execute();
        if (ret == false){
          throw new LoginException("USER NOT FOUND");
        }
        try (ResultSet rs = stmt.executeQuery()) {
          user = new User(rs.getString("email"), rs.getString("passowrd"),
                          UserRole.valueOf(rs.getString("role")),
                          rs.getString("name"));
        } catch (SQLException se) {
          throw new DataBaseException("USER NOT FOUND", se);
        }
      } catch (SQLException se) {
        throw new DataBaseException("USER NOT FOUND", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("USER NOT FOUND", se);
    }
    return user;
  }
  public static boolean isValidLogin(String email)
          throws LoginException, DataBaseException {
    String connectionUrl =
            "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "SELECT * FROM users WHERE email = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, email);
        boolean rs = stmt.execute();
        if(rs == false){
          throw new LoginException("USER NOT FOUND");
        }
      } catch (SQLException se) {
        throw new DataBaseException("USER NOT FOUND", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("USER NOT FOUND", se);
    }
    return true;
  }
}
