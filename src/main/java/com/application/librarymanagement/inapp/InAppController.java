package com.application.librarymanagement.inapp;

import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class InAppController extends MainAppController {
  @FXML private AnchorPane dashboardPane;
  @FXML private Label welcomeLabel;
  @FXML private ImageView dashboardIcon;
  @FXML private ImageView booksIcon;
  @FXML private Button dashboardButton;
  @FXML private Button booksButton;

  JsonObject currentUser;

  public void initialize() {
    super.initialize();
    String username = JsonUtils.getAsString(config, "currentSession", "");
    JsonArray users = JsonUtils.loadLocalJsonAsArray(USERS_DB_PATH);
    currentUser = JsonUtils.findJsonObjectByKeyValue(users, "username", username);
    assert currentUser != null;
    String name = currentUser.get("name").getAsString();
    welcomeLabel.setText(String.format("Welcome, %s [%s] 👋", name, username));
  }

  @Override
  protected void loadIcon() {
    String theme = config.get("theme").getAsString();
    String type = theme.contains("light") ? "Light" : "Dark";
    dashboardIcon.setImage(ImageUtils.getImage(type + "DashboardButton.png"));
    booksIcon.setImage(ImageUtils.getImage(type + "BooksButton.png"));
  }
}
