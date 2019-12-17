package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.Project;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.model.Status;
import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.model.exception.InavliedReportException;
import dev.omaremara.bugtracker.model.exception.LoginException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class Report {
  public int id;
  public String title;
  public String description;
  public ReportLevel level;
  public ReportPriority priority;
  public ReportType type;
  public Project project;
  public User assigne;
  public Status status;
  public LocalDateTime dateTime;
  public User author;

  public Report(int id, String title, String description, ReportLevel level,
                ReportPriority priority, ReportType type, User assigne,
                LocalDateTime dateTime, Status status, User author) {
    this.title = title;
    this.assigne = assigne;
    this.description = description;
    this.level = level;
    this.priority = priority;
    this.id = id;
    this.type = type;
    this.status = status;
    this.dateTime = dateTime;
    this.author = author;
  }

  public void validateReport(String blank) throws InavliedReportException {
    if (blank.isBlank()) {
      throw new InavliedReportException("Empty String");
    }
  }

  @Override
  public String toString() {
    return this.title;
  }

  public void submit() throws InavliedReportException {
    validateReport(this.title);
    validateReport(this.description);
    String connectionURL =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionURL)) {
      String sql =
          "INSERT INTO reports(id, title, type, priority, level, description, assignee, project, status, date, author) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(2, this.title);
        stmt.setString(6, this.description);
        stmt.setString(5, this.level.name());
        stmt.setString(4, this.priority.name());
        stmt.setString(3, this.type.name());
        stmt.setString(7, this.assigne.email);
        stmt.setInt(1, this.id);
        stmt.setString(8, "project1");
        stmt.setString(9, this.status.name());
        stmt.setObject(10, this.dateTime);
        stmt.setString(11, this.author.email);
        stmt.executeUpdate();
      } catch (SQLException se) {
        se.printStackTrace();
      }
    } catch (SQLException se) {
      se.printStackTrace();
    }
  }

  private static User getFromLogin(String uiName)
      throws LoginException, DataBaseException {
    String connectionURL =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    User user = null;
    try (Connection conn = DriverManager.getConnection(connectionURL)) {
      String sql = "SELECT * FROM users WHERE email = ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, uiName);
        try (ResultSet rs = stmt.executeQuery()) {
          if (!rs.isBeforeFirst()) {
            throw new LoginException("No user exist with this email!");
          }
          rs.next();
          user = new User(rs.getString("email"), rs.getString("password"),
                          UserRole.valueOf(rs.getString("role")),
                          rs.getString("name"));
        } catch (SQLException se) {
          throw new DataBaseException("Email Not Found In users", se);
        }

      } catch (SQLException se) {
        throw new DataBaseException("Email Not Found In users", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("Email Not Found In users", se);
    }
    return user;
  }

  public static List<Report> getAllReports(Status status, String assignee)
      throws LoginException, DataBaseException {
    List<Report> reports = new ArrayList<Report>();
    String connectionURL =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    StringBuilder sql = new StringBuilder("SELECT * FROM reports");
    try (Connection conn = DriverManager.getConnection(connectionURL)) {
      if (status != null && assignee == null) {
        sql.append(" WHERE status = ?");
        System.out.println(sql.toString());
      } else if (assignee != null && status == null) {
        sql.append(" WHERE assignee = ?");
        System.out.println(sql.toString());
      } else if (assignee != null && status != null) {
        sql.append(" WHERE assignee = ? AND status = ?");
        System.out.println(sql.toString());
      }
      try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
        if (status != null && assignee == null) {
          stmt.setString(1, status.name());
        } else if (assignee != null && status == null) {
          stmt.setString(1, status.name());
        } else if (assignee != null && status != null) {
          stmt.setString(1, assignee);
          stmt.setString(2, status.name());
        }
        try (ResultSet rs = stmt.executeQuery()) {
          while (rs.next()) {
            String uiEmail = rs.getString("assignee");
            User userInfoAss = getFromLogin(uiEmail);
            User userInfoAuth = getFromLogin(uiEmail);
            Report reportInfo = new Report(
                rs.getInt("id"), rs.getString("title"),
                rs.getString("description"),
                ReportLevel.valueOf(rs.getString("level")),
                ReportPriority.valueOf(rs.getString("priority")),
                ReportType.valueOf(rs.getString("type")), userInfoAss,
                rs.getObject("date", LocalDateTime.class),
                Status.valueOf(rs.getString("status")), userInfoAuth);
            reports.add(reportInfo);
          }
        } catch (SQLException se) {
          throw new DataBaseException("No Reports Found", se);
        }
      } catch (SQLException se) {
        throw new DataBaseException("No Reports Found", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("No Reports Found", se);
    }
    System.out.println(reports);
    return reports;
  }

  public static int getCountOfReport() throws DataBaseException {
    int count = 0;
    String connectionURL =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionURL)) {
      String sql = "SELECT COUNT(id) AS 'COUNT(id)' FROM reports";
      try (Statement stmt = conn.createStatement()) {
        try (ResultSet rs = stmt.executeQuery(sql)) {
          System.out.println("in count");
          rs.next();
          count = rs.getInt("COUNT(id)");
        } catch (SQLException se) {
          throw new DataBaseException("No Reports Found", se);
        }
      } catch (SQLException se) {
        throw new DataBaseException("No Reports Found", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("No Reports Found", se);
    }
    System.out.println(count);
    return count;
  }


}
