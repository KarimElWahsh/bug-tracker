package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.Main;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.view.ReportListView;
import dev.omaremara.bugtracker.model.Report;
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
  private int id = 0;

  public void submit(String title, String description, ReportLevel level,
                     ReportPriority priority, ReportType type, Project project,
                     User assigne, Label errorLabel) {
    try {
      id++;
      Report NewReport = new Report(id, title, description, level, priority,
                                    type, project, assigne) Stage stage =
          Main.primaryStage;
      Scene reportListScene = new ReportListView().getScene();
      stage.setScene(reportListScene);
    } catch (Exception e) {
      this.errorLabel.setTgit ext("Error Expected");
    }

    // if (isValidReport) {
    // stage.setScene(scene);
    // }
    // else {
    //
    // }
  }

  public void attach(ActionEvent e) {
    Stage stage = Main.primaryStage;
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Attach Screenshot");
    fileChooser.setInitialDirectory(
        new File(System.getProperty("user.home") + "/Pictures"));
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    File selectedFile = fileChooser.showOpenDialog(stage);
    if (selectedFile != null) {
      this.attachedLabel.setText(selectedFile.getName());
    }
  }

  public void cancel(ActionEvent e) {
    Stage stage = Main.primaryStage;
    Scene reportListScene = new ReportListView().getScene();
    stage.setScene(reportListScene);
  }
}
