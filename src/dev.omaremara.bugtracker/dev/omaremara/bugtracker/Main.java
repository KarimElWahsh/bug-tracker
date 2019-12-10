package dev.omaremara.bugtracker;

import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.UserRole;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.model.Project;
import dev.omaremara.bugtracker.model.Report;
import dev.omaremara.bugtracker.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  public static Stage primaryStage;

  @Override
  public void start(Stage stage) {
    Main.primaryStage = stage;
    Scene loginScene = new LoginView().getScene();
    stage.setScene(loginScene);
    stage.show();
  }
    public static void main (String[]args){
    launch();
//    trace code to kariem remove this commit
//      UserRole role = UserRole.TESTER;
//      User obj1 = new User("mohamel", "1245", role, "Mohamed");
//      ReportLevel level = ReportLevel.USER;
//      ReportPriority priority = ReportPriority.BLOCKER;
//      ReportType type = ReportType.BUG;
//      Project project = new Project("project");
//      Report obj = new Report(1, "title", "description", level, priority, type, project, obj1);
//      obj.submit();
    }
}
