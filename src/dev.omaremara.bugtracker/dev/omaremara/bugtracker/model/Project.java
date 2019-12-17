package dev.omaremara.bugtracker.model;

import dev.omaremara.bugtracker.model.exception.DataBaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Project {

  public String name;

  public Project(String name) { this.name = name; }

  public String toString() { return this.name; }
  public static List<Project> getAllProjects() throws DataBaseException {
    List<Project> projectList = new ArrayList<Project>();
    String connectionUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=master;integratedSecurity=true";
    try (Connection conn = DriverManager.getConnection(connectionUrl)) {
      String sql = "SELECT * FROM projects";
      try (Statement stmt = conn.createStatement()) {
        try (ResultSet rs = stmt.executeQuery(sql)) {
          while (rs.next()) {
            Project project = new Project(rs.getString("name"));
            projectList.add(project);
          }
        } catch (SQLException se) {
          throw new DataBaseException("NO PROJECTS FOUND", se);
        }
      } catch (SQLException se) {
        throw new DataBaseException("NO PROJECTS FOUND", se);
      }
    } catch (SQLException se) {
      throw new DataBaseException("NO PROJECTS FOUND", se);
    }
    return projectList;
  }
}
