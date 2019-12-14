package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.UserRole;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.model.exception.LoginException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                rs.getString("email"), rs.getString("password"),
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
    User resultUser = getFromLogin(email);
    if (password != resultUser.password) {
      throw new LoginException("PASSWORD NOT MATCH");
    }
    return resultUser;
  }

  public static User getFromLogin(String email)
      throws LoginException, DataBaseException {
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    User user = null;
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "SELECT * FROM users WHERE email = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()) {
          rs.next();
          user = new User(rs.getString("email"), rs.getString("password"),
                          UserRole.valueOf(rs.getString("role")),
                          rs.getString("name"));
        } catch (SQLException se) {
          throw new DataBaseException("INVALID EMAIL", se);
        }
      } catch (SQLException se) {
        throw new DataBaseException("INVALID EMAIL", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("INVALID EMAIL", se);
    }
    return user;
  }
  // remove user
  public void removeUser() throws DataBaseException {
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "DELETE FROM users WHERE email = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        System.out.println(this.email);
        stmt.setString(1, this.email);
        stmt.executeUpdate();
      } catch (SQLException se) {
        throw new DataBaseException("Cannot Delete users2", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("Cannot Delete users3", se);
    }
  }
  // update user
  public void updateUser(User updateUser, String email) throws DataBaseException {
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "UPDATE users SET email = ?, password = ?, role = ?, name = ? WHERE email = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, updateUser.email);
        stmt.setString(2, updateUser.password);
        stmt.setString(3, updateUser.userRole.name());
        stmt.setString(4, updateUser.name);
        stmt.setString(5, email);
        stmt.executeUpdate();
      } catch (SQLException se) {
        throw new DataBaseException("CAN NOT UPDATE USER", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("CAN NOT UPDATE USER", se);
    }
  }
}
