package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.Project;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.exception.InavliedReportException;
import java.sql.*;

public class Report {
  public int id;
  public String title;
  public String description;
  public ReportLevel level;
  public ReportPriority priority;
  public ReportType type;
  public Project project;
  public User assigne;

  public Report(int id, String title, String description, ReportLevel level,
                ReportPriority priority, ReportType type, Project project,
                User assigne) {
    this.title = title;
    this.assigne = assigne;
    this.description = description;
    this.level = level;
    this.priority = priority;
    this.project = project;
    this.id = id;
    this.type = type;
  }

  public void validateReport(String blank) throws InavliedReportException{
    if (blank.isBlank()){
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
          "INSERT INTO reports(id, title, type, priority, level, description, assignee) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(2, this.title);
        stmt.setString(6, this.description);
        stmt.setString(5, this.level.name());
        stmt.setString(4, this.priority.name());
        stmt.setString(3, this.type.name());
        stmt.setString(7, this.assigne.email);
        stmt.setInt(1, this.id);
        stmt.executeUpdate();
      }

    } catch (SQLException se) {
      se.printStackTrace();
    }
  }

  // gitReportsCounet : throw database exception

  // git from resulset : throw database exception login exceptionn :

  // gitAllReports : retreive array list of report t: throw databaase exception and login exception


}
