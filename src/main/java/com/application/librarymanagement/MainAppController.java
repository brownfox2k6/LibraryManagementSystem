package com.application.librarymanagement;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;

public class MainAppController {
  /**
   * Handles theme selection from the menu. Retrieves the clicked MenuItem's text
   * (which corresponds to a theme name) and applies the matching stylesheet
   * to the application.
   * @param event the ActionEvent triggered by selecting a theme MenuItem
   */
  @FXML
  protected void applyStylesheet(ActionEvent event) {
    MenuItem mi = (MenuItem) event.getSource();
    MainApp.applyStylesheet(mi.getText());
  }
}