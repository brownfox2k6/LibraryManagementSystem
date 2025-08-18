package com.application.librarymanagement;

import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;

public class MainAppController extends MainApp {
  @FXML public ComboBox<String> themeComboBox;
  @FXML public ImageView backgroundImage;

  /**
   * Initializes the controller after the FXML components have been loaded.
   * Sets the initial background image based on the current theme stored in configuration.
   */
  @FXML
  public void initialize() {
    themeComboBox.setValue(JsonUtils.getAsString(config, "theme", ""));
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

  @FXML
  public void applyStylesheet() {
    JsonUtils.addProperty(config, CONFIG_PATH,"theme", themeComboBox.getValue());
    setBackgroundImage();
    loadIcons();
    applyStylesheet(themeComboBox.getValue());
  }

  protected void loadIcons() {}
}