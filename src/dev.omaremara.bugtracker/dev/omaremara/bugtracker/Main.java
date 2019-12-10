package dev.omaremara.bugtracker;

import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.UserRole;
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

  public static void main(String[] args) {
//    launch();
    UserRole role = UserRole.TESTER;
    User obj = new User("mohamel", "1245", role, "Mohamed");
  }
}
