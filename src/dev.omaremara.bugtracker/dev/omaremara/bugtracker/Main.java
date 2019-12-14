package dev.omaremara.bugtracker;

import dev.omaremara.bugtracker.model.Project;
import dev.omaremara.bugtracker.model.Report;
import dev.omaremara.bugtracker.model.ReportLevel;
import dev.omaremara.bugtracker.model.ReportPriority;
import dev.omaremara.bugtracker.model.ReportType;
import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.UserRole;
import dev.omaremara.bugtracker.model.exception.InavliedReportException;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.model.exception.LoginException;
import dev.omaremara.bugtracker.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import dev.omaremara.bugtracker.model.Status;
import java.sql.Timestamp;
import java.util.Date;
import java.time.LocalDateTime;

public class Main extends Application {
    private User user;
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        Main.primaryStage = stage;
        Scene loginScene = new LoginView().getScene();
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) {
//    launch();
        UserRole role = UserRole.DEVELOPER;
        User obj1 = new User("q", "1245", role, "Mohamed");
//        obj1.submit();
//          obj1.getAllDevelopers();
        ReportLevel level = ReportLevel.USER;
        ReportPriority priority = ReportPriority.BLOCKER;
        ReportType type = ReportType.BUG;
        Project project = new Project("project");
        LocalDateTime now = LocalDateTime.now();
        Status status = Status.OFF;
        Report obj = new Report(1, "title", "project", level, priority,
                type, obj1, now, status);
//          try {
//            obj.submit();
//          } catch(InavliedReportException e){
//            System.out.println(e.getMessage());
//          }
          status = Status.OFF;
    List<Report> reports = new ArrayList<Report>();
        try {

            obj.returnAllReports(status);
        } catch (DataBaseException | LoginException e) {
            System.out.println(e.getMessage());
        }
//        User obj2 = new User("mohamed", "1245", role, "Mohamed");
//        try{
//            obj1.updateUser(new User("good", "1245", role, "Mohamed"));
//
//        } catch (DataBaseException exception) {
//            System.out.println(exception.getMessage());
//        }
//        Project project = new Project("project1");
//        try {
//            project.getAllProjects();
//        } catch (DataBaseException exception){
//            System.out.println(exception.getMessage());
//        }
    }
}