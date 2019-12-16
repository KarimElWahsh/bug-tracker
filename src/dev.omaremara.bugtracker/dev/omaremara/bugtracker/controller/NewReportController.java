package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.Main;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.view.ReportListView;
import dev.omaremara.bugtracker.model.Report;
import dev.omaremara.bugtracker.model.Project;
import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.controller.Email;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class NewReportController {
  private Label errorLabel;
  private Label attachedLabel;
  private int id;
  public void submit(String title, String description,ReportType type ,
                     ReportPriority priority, ReportLevel level, Project project,
                     User assigne, Label errorLabel) {
    try {
        id = Report.getId(); // get last Report ID
        id++;
      Report NewReport = new Report(id,title, description, level, priority, type, project, assigne);
      Email NE = new Email( gfhhh, title , description);
      Email.SendEmail();
      Stage stage =Main.primaryStage;
      Scene reportListScene = new ReportListView().getScene();
      stage.setScene(reportListScene);
    } catch (DataBase exception) {
      this.errorLabel.setText(exception.getMessage());
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
      attachedLabel.setText(selectedFile.getName()); // method getName ???
    }
  }

  public void cancel() {
    Stage stage = Main.primaryStage;
    Scene reportListScene = new ReportListView().getScene();
    stage.setScene(reportListScene);
  }
  public static ArrayList<Project> getAllProjects(label errorLabel){
    // return AllProject
    try{
      return Project.getAllProjects();
    }catch (DataBaseException exception){
      errorLabel.setText(exception.getMessage());
    } return new ArrayList<Project>();
  }
  public static List<User> getAllDevelopers(Label errorLabel){
    // return AllDevelopers
    try{
      return User.getAllDevelopers();
    }catch (DataBaseException exception){
      errorLabel.setText(exception.getMessage());
    }return new ArrayList<User>();
  }
}

