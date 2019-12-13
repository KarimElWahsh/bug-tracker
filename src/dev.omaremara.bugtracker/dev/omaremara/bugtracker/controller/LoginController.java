package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.Main;
import dev.omaremara.bugtracker.view.ReportListView;
import dev.omaremara.bugtracker.model.User;
import javafx.event.ActionEvent;
import dev.omaremara.bugtracker.model.UserRole;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;

public class LoginController {
  private TextField emailField;
  private PasswordField passwordField;
  private UserRole userRole;
  private String name;
  private Label errorLabel;

 // name ? //userRole ?
  public void login(String email, String password, Label errorLabel) {
    try {
      Main.user = User.getFromLogin(email, password); //mohamed should make getFromLogin method
        if (User.isValidLogin(email)) {
              Stage stage = Main.primaryStage;
              Scene reportListScene = new ReportListView().getScene();
              stage.setScene(reportListScene);
             }
             else {
             errorLabel.setText("Invalid email or password!");
             }

    } catch (LoginException | DataBaseException exception) {
      errorLabel.setText(exception.getMessage()); //login & DatBase exception
    }
  }

  }

