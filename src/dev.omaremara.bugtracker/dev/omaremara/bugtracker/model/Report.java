package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.Project;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.model.Status;
import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.Project;
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
  public String screenshotpath;

  public Report(int id, String title, String description, ReportLevel level,
                ReportPriority priority, ReportType type, User assigne,
                LocalDateTime dateTime, Status status, User author, Project project, String screenshotpath) {
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
    this.project = project;
    this.screenshotpath = screenshotpath;
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

  public void submit() throws InavliedReportException{
    validateReport(this.title);
    validateReport(this.description);
    String connectionURL =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionURL)) {
      String sql =
          "INSERT INTO reports(id, title, type, priority, level, description, assignee, project, status, date, author, screenshotpath) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(2, this.title);
        stmt.setString(6, this.description);
        stmt.setString(5, this.level.name());
        stmt.setString(4, this.priority.name());
        stmt.setString(3, this.type.name());
        stmt.setString(7, this.assigne.email);
        stmt.setInt(1, this.id);
        stmt.setString(8, this.project.name);
        stmt.setString(9, this.status.name());
        stmt.setObject(10, this.dateTime);
        stmt.setString(11, this.author.email);
        stmt.setString(12, this.screenshotpath);
        stmt.executeUpdate();
      } catch (SQLException se) {
        throw new InavliedReportException("InavliedReportException");
      }
    } catch (SQLException se) {
      throw new InavliedReportException("InavliedReportException");
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
      } else if (assignee != null && status == null) {
        sql.append(" WHERE assignee = ?");
      } else if (assignee != null && status != null) {
        sql.append(" WHERE assignee = ? AND status = ?");
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
            Project project1 = new Project(rs.getString("project"));
            Report reportInfo = new Report(
                rs.getInt("id"), rs.getString("title"),
                rs.getString("description"),
                ReportLevel.valueOf(rs.getString("level")),
                ReportPriority.valueOf(rs.getString("priority")),
                ReportType.valueOf(rs.getString("type")), userInfoAss,
                rs.getObject("date", LocalDateTime.class),
                Status.valueOf(rs.getString("status")), userInfoAuth, project1, rs.getString("screenshotpath"));
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
    return count;
  }

  public void updateStatus(Status newStatus) throws DataBaseException {
    if (newStatus.equals(this.status)) {
      return;
    }
    String connectionURL =
            "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection connection = DriverManager.getConnection(connectionURL)) {
      String sql = "UPDATE reports SET status = ? WHERE id = ?";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, newStatus.name());
        statement.setInt(2, this.id);
        statement.executeUpdate();
      } catch (SQLException exception) {
        throw new DataBaseException("Could not query report from database!",
                exception);
      }
    } catch (SQLException exception) {
      throw new DataBaseException("Could not establish connection to database!",
              exception);
    }
    this.status = newStatus;
  }
}
