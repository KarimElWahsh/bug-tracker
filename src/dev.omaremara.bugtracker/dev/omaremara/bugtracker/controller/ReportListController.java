package dev.omaremara.bugtracker.controller;

import dev.omaremara.bugtracker.Main;
import dev.omaremara.bugtracker.model.Report;
import dev.omaremara.bugtracker.model.Status;
import dev.omaremara.bugtracker.model.exception.DataBaseException;
import dev.omaremara.bugtracker.model.exception.LoginException;
import dev.omaremara.bugtracker.view.AdministrationView;
import dev.omaremara.bugtracker.view.InsightsView;
import dev.omaremara.bugtracker.view.LoginView;
import dev.omaremara.bugtracker.view.NewReportView;
import dev.omaremara.bugtracker.view.ReportView;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ReportListController {
  public static List<Report> getAllReports(boolean getOpenedReports,
                                           boolean getClosedReports,
                                           boolean getMyReportsOnly,
                                           Label errorLabel) {
    try {
      if (getClosedReports ^ getOpenedReports) {
        if (getMyReportsOnly) {
          if (getClosedReports) {
            return Report.getAllReports(Status.CLOSED, Main.user.email);
          } else {
            return Report.getAllReports(Status.OPENED, Main.user.email);
          }
        } else {
          if (getClosedReports) {
            return Report.getAllReports(Status.CLOSED, null);
          } else {
            return Report.getAllReports(Status.OPENED, null);
          }
        }
      } else {
        if (getMyReportsOnly) {
          return Report.getAllReports(null,Main.user.email);
        } else {
          return Report.getAllReports(null, null);
        }
      }
    } catch (DataBaseException | LoginException exception) {
      errorLabel.setText(exception.getMessage());
    }
    return new ArrayList<Report>();
  }
}
