package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.Main;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.model.Status;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.model.exception.InavliedReportException;
import dev.omaremara.bugtracker.model.Report;
import dev.omaremara.bugtracker.model.Project;
import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.UserRole;
import dev.omaremara.bugtracker.view.ReportListView;
import dev.omaremara.bugtracker.controller.Email;
import dev.omaremara.bugtracker.view.ReportListView;
import dev.omaremara.bugtracker.util.ViewUtil;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class NewReportController {

  public void submit(String title, String description, ReportType type,
                     ReportPriority priority, ReportLevel level,
                     Project project, User assigne, Label errorLabel) {
    try {

      Report NewReport =
          new Report(Report.getCountOfReport(), title, description, level,
                     priority, type, project, assigne, LocalDateTime.now(),
                     ReportStatus.OPENED, Main.user); // not finished
      Email newEmail = new Email(assigne.email, title, description);
      newEmail.SendEmail();
      report.submit();
      ViewUtil.setSceneRoot(new ReportListView());
    } catch (DataBaseException | InavliedReportException exception) {
      errorLabel.setText(exception.getMessage());
    }
  }

  public void attach(Label attachedLabel) {
    Stage stage = Main.primaryStage;
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Attach Screenshot");
    fileChooser.setInitialDirectory(
        new File(System.getProperty("user.home") + "/Pictures"));
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    File selectedFile = fileChooser.showOpenDialog(stage);
    if (selectedFile != null) {
      this.screenshotPath = selectedFile.getPath();
      attachedLabel.setText(selectedFile.getName());
    }
  }

  public void cancel() { ViewUtil.setSceneRoot(new ReportListView()); }

  public static ArrayList<Project> getAllProjects(label errorLabel) {
    // return AllProject
    try {
      return Project.getAllProjects();
    } catch (DataBaseException exception) {
      errorLabel.setText(exception.getMessage());
    }
    return new ArrayList<Project>();
  }

  public static ArrayList<User> getAllDevelopers(  UserRole DEVELOPER) {
    // return AllDevelopers
    try {
      return User.getAllDevelopers();
    } catch (DataBaseException exception) {
      errorLabel.setText(exception.getMessage());
    }
    return new ArrayList<User>();
  }
}
