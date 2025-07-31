package com.application.librarymanagement.inapp;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InAppController extends MainAppController {
  @FXML private Label titleLabel;
  @FXML private Label welcomeLabel;
  @FXML private ImageView dashboardIcon;
  @FXML private ImageView booksIcon;
  @FXML private Button dashboardButton;
  @FXML private Button booksButton;
  @FXML private ImageView signOutButton;
  @FXML private VBox subsceneContainer;

  JsonObject currentUser;

  public void initialize() {
    super.initialize();
    String username = JsonUtils.getAsString(config, "currentSession", "");
    JsonArray users = JsonUtils.loadLocalJsonAsArray(USERS_DB_PATH);
    currentUser = JsonUtils.findJsonObjectByKeyValue(users, "username", username);
    assert currentUser != null;
    String name = currentUser.get("name").getAsString();
    welcomeLabel.setText(String.format("Welcome, %s [%s] 👋", name, username));
    setSubscene("Dashboard");
  }

  protected void setSubscene(String name) {
    try {
      subsceneContainer.getChildren().clear();
      FXMLLoader fxmlLoader = new FXMLLoader();
      String path = String.format("scenes/%s.fxml", name);
      fxmlLoader.setLocation(MainApp.class.getResource(path));
      AnchorPane pane = fxmlLoader.load();
      subsceneContainer.getChildren().add(pane);
      titleLabel.setText(name);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  protected void loadIcon() {
    String theme = config.get("theme").getAsString();
    String type = theme.contains("light") ? "Light" : "Dark";
    dashboardIcon.setImage(ImageUtils.getImage(type + "DashboardButton.png"));
    booksIcon.setImage(ImageUtils.getImage(type + "BooksButton.png"));
    signOutButton.setImage(ImageUtils.getImage(type + "LogOutButton.png"));
  }

  @FXML
  protected void logOut() {
    JsonUtils.addProperty(config, CONFIG_PATH, "currentSession", "");
    setScene("SignIn");
  }
}
