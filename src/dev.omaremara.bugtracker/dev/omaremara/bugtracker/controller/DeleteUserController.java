package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.model.User;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.util.ViewUtil;
import dev.omaremara.bugtracker.view.AdministrationView;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;

public class DeleteUserController {

  public static void deleteUser(User user, label errorLabel) {
    try {
      user = User.removeUser();
      ViewUtil.setSceneRoot(new AdministrationView());
    } catch (DataBaseException exception) {
      errorLabel.settext(exception.getMessage());
    }
  }
  public static List<User> getAllUsers(Label errorLabel) {
    try {
      return User.getAllDevelopers(null);
    } catch (DataBaseException exception) {
      errorLabel.setText(exception.getMessage());
    }
    return new ArrayList<User>();
  }
}
}