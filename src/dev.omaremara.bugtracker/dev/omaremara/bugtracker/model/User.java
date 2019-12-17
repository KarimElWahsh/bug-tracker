package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.UserRole;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.model.exception.LoginException;
import java.sql.*;
import java.util.*;


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

  public void submit() throws DataBaseException{
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
        throw new DataBaseException("Cannot INSERT into users", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("Cannot INSERT into users", se);
    }
  }

  @Override
  public String toString() {
    return this.email;
  }

  public static List<User> getAllDevelopers(UserRole uRole) throws DataBaseException{
    List<User> Alldevelopers = new ArrayList<User>();
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    StringBuilder  sql = new StringBuilder ("SELECT * FROM users");
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      try (Statement stmt = conn.createStatement()) {
        UserRole userRole1 = UserRole.DEVELOPER;
          if (userRole1 == uRole){
            sql.append(" WHERE role = 'DEVELOPER'");
          }
        try (ResultSet rs = stmt.executeQuery(sql.toString())) {
          while (rs.next()) {
            User developer = new User(
                rs.getString("email"), rs.getString("password"),
                UserRole.valueOf(rs.getString("role")), rs.getString("name"));
            Alldevelopers.add(developer);
          }
        } catch (SQLException se) {
        throw new DataBaseException("Cannot get users", se);
      }
      }  catch (SQLException se) {
        throw new DataBaseException("Cannot get users", se);
      }
    }  catch (SQLException se) {
        throw new DataBaseException("Cannot get users", se);
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
        throw new DataBaseException("Cannot Delete users", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("Cannot Delete users", se);
    }
  }
  // update user
  public void updateUser(User updateUser) throws DataBaseException {
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "UPDATE users SET email = ?, password = ?, role = ?, name = ? WHERE email = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, updateUser.email);
        stmt.setString(2, updateUser.password);
        stmt.setString(3, updateUser.userRole.name());
        stmt.setString(4, updateUser.name);
        stmt.setString(5, this.email);
        stmt.executeUpdate();
      } catch (SQLException se) {
        throw new DataBaseException("CAN NOT UPDATE USER", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("CAN NOT UPDATE USER", se);
    }
  }
  public static HashMap<User, Integer> getUsersStats()
          throws DataBaseException {
    String connectionURL =
            "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    HashMap<User, Integer> hm = new HashMap<User, Integer>();
    try (Connection conn = DriverManager.getConnection(connectionURL)) {
      String sql =
              "SELECT email, password, role, name,  COUNT(*) AS COUNT   FROM users INNER JOIN reports on email = author OR (email = assignee and status = 'CLOSED') GROUP BY email, password, role, name";
      try (Statement stmt = conn.createStatement()) {
        try (ResultSet rs = stmt.executeQuery(sql)) {
          while (rs.next()) {
            User user = new User(
                    rs.getString("email"), rs.getString("password"),
                    UserRole.valueOf(rs.getString("role")), rs.getString("name"));
            hm.put(user, rs.getInt("COUNT"));
          }
        } catch (SQLException se) {
          throw new DataBaseException("Cannot get user stats", se);
        }
      } catch (SQLException se) {
        throw new DataBaseException("Cannot get user stats", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("Cannot get user stats", se);
    }
    return hm;
  }
}
