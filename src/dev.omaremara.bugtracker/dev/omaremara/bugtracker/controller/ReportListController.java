package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.Main;
import dev.omaremara.bugtracker.view.LoginView;
import dev.omaremara.bugtracker.view.NewReportView;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label; //import Label Class

public class ReportListController {
  private Label errorlabel;

  public  static void newReport(ActionEvent e) {
    Stage stage = Main.primaryStage;
    Scene newReportScene = new NewReportView().getScene();
    stage.setScene(newReportScene);
  }
  public static void logOut() {
    Stage stage = Main.primaryStage;
    Scene loginScene = new LoginView().getScene();
    stage.setScene(loginScene);
  }
  public static ArrayList<Report> getAllReports(Label errorlabel){
  try {
    return Report.getAllReports();
  } catch (Exception exception){
    errorlabel.setText(exception.getMessage());
  }
  return new ArrayList<Report>();
  }
  public static void administer()
  {
    Stage stage = Main.primaryStage;
    Scene administerScene = new AdministrationView().getScene();
    stage.setScene(administerScene);
  }
  public static void insights()
  {
        Stage stage = Main.primaryStage;
    Scene insightsScene = new InsightsView().getScene();
    stage.setScene(insightsScene);

  }

}
