package com.application.librarymanagement;

import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class MainAppController extends MainApp {
  @FXML public ImageView backgroundImage;
  @FXML public Label errorLabel;

  /**
   * Initializes the controller after the FXML components have been loaded.
   * Sets the initial background image based on the current theme stored in configuration.
   */
  @FXML public void initialize() {
    setBackgroundImage();
    loadIcons();
  }

  /**
   * Updates the background image according to the currently selected theme.
   * It reads the theme from the configuration file and sets the corresponding
   * light or dark background image.
   * <p>
   * The background image is expected to be located in the {@code images/} directory
   * following the pattern {@code LightSky.jpg} or {@code DarkSky.jpg}.
   * <p>
   * For example, if the theme is {@code primer-light}, it loads {@code LightSky.jpg}.
   * @throws AssertionError if the background image resource is not found
   */
  public void setBackgroundImage() {
    backgroundImage.setImage(ImageUtils.getImage(getLightOrDark() + "Sky.jpg"));
  }

  /**
   * Handles theme selection from the menu. Retrieves the clicked MenuItem's text
   * (which corresponds to a theme name) and applies the matching stylesheet
   * to the application.
   * @param event the ActionEvent triggered by selecting a theme MenuItem
   */
  @FXML public void applyStylesheet(ActionEvent event) {
    String theme = ((MenuItem) event.getSource()).getText();
    JsonUtils.addProperty(config, CONFIG_PATH,"theme", theme);
    setBackgroundImage();
    loadIcons();
    applyStylesheet(theme);
  }

  protected void loadIcons() {}

  /**
   * Displays a given message in the error label and makes it visible.
   * @param msg the message to display
   */
  protected void showErrorLabel(String msg) {
    errorLabel.setText(msg);
    errorLabel.setVisible(true);
  }
}