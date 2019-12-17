package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.model.Report;
import dev.omaremara.bugtracker.model.Status;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.view.LoginView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReportControlleR {
  public void toggleStatus(Report report, Button toggleButton,
                           Label errorLabel) {
    try {
      if (report.status.equals(Status.OPENED)) {
        report.updateStatus(Status.CLOSED);
        toggleButton.setText("Reopen");
      } else {
        report.updateStatus(Status.OPENED);
        toggleButton.setText("Close");
      }
    } catch (DataBaseException exception) {
      errorLabel.setText(exception.getMessage());
    }
  }
}