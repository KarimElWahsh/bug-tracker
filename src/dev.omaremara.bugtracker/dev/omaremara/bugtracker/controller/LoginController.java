package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.Main;
import dev.omaremara.bugtracker.view.ReportListView;
import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.UserRole;
import dev.omaremara.bugtracker.util.ViewUtil;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.model.exception.LoginException;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginController {
  private TextField emailField;
  private PasswordField passwordField;
  private Label errorLabel;

  public void login(String email, String password, Label errorLabel) {
    try {
      Main.user = User.getFromLogin(email, password);

      ViewUtil.setSceneRoot(new ReportListView());
    } catch (LoginException | DataBaseException exception) {
      errorLabel.setText(exception.getMessage()); // login & DatBase exception
    }
  }
}
