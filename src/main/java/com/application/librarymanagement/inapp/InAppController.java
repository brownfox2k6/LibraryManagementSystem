package com.application.librarymanagement.inapp;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Objects;

public final class InAppController extends MainAppController {
  @FXML private Label titleLabel;
  @FXML private Label welcomeLabel;
  @FXML private ImageView dashboardIcon;
  @FXML private ImageView booksIcon;
  @FXML private ImageView borrowsIcon;
  @FXML private ImageView signOutIcon;
  @FXML private VBox subsceneContainer;
  @FXML private ImageView changePasswordIcon;

  public static InAppController INSTANCE;
  public static User CURRENT_USER;

  public void initialize() {
    INSTANCE = this;
    super.initialize();
    loadCurrentUser();
    welcomeLabel.setText(String.format("Welcome, %s [%s] 👋",
            CURRENT_USER.getName(), CURRENT_USER.getUsername()));
    setSubscene("Dashboard", "Dashboard");
  }

  public <T> T setSubscene(String name, String title) {
    try {
      subsceneContainer.getChildren().clear();
      FXMLLoader fxmlLoader = new FXMLLoader(
              MainApp.class.getResource(String.format("scenes/%s.fxml", name)));
      Node pane = fxmlLoader.load();
      subsceneContainer.getChildren().add(pane);
      titleLabel.setText(title);
      return fxmlLoader.getController();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void loadCurrentUser() {
    String username = JsonUtils.getAsString(config, "currentSession", "");
    JsonArray users = JsonUtils.loadLocalJsonAsArray(USERS_DB_PATH);
    JsonObject user = JsonUtils.findJsonObjectByKeyValue(users, "username", username);
    assert user != null;
    CURRENT_USER = new User(user);
  }

  @Override
  protected void loadIcons() {
    String type = getLightOrDark();
    dashboardIcon.setImage(ImageUtils.getImage(type + "DashboardButton.png"));
    booksIcon.setImage(ImageUtils.getImage(type + "BooksButton.png"));
    signOutIcon.setImage(ImageUtils.getImage(type + "LogOutButton.png"));
    borrowsIcon.setImage(ImageUtils.getImage(type + "BorrowsButton.png"));
    changePasswordIcon.setImage(ImageUtils.getImage(type + "ChangePasswordButton.png"));
  }

  @FXML
  private void signOut() {
    JsonUtils.addProperty(config, CONFIG_PATH, "currentSession", "");
    MainApp.showPopupMessage(String.format("Goodbye, %s. Looking forward to seeing you again.",
        CURRENT_USER.getUsername()), Color.DARKGREEN);
    setScene("SignIn");
  }

  @FXML private void gotoChangePassword() { setSubscene("ChangePassword", "Change Password"); }
  @FXML private void gotoDashboard() { setSubscene("Dashboard", "Dashboard"); }
  @FXML private void gotoBooks() { setSubscene("BookSearch", "Book Search"); }
  @FXML private void gotoBorrows() { setSubscene("Borrows", "Borrows"); }
}
