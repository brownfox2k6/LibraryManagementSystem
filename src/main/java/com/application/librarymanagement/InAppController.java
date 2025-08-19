package com.application.librarymanagement;

import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonElement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public final class InAppController extends MainAppController {
  @FXML private Label titleLabel;
  @FXML private Label welcomeLabel;
  @FXML private ImageView dashboardIcon;
  @FXML private ImageView booksIcon;
  @FXML private ImageView borrowsIcon;
  @FXML private ImageView usersIcon;
  @FXML private ImageView accountSettingsIcon;
  @FXML private ImageView signOutIcon;
  @FXML private VBox subsceneContainer;

  public static InAppController INSTANCE;
  public static User CURRENT_USER;

  public void initialize() {
    INSTANCE = this;
    super.initialize();
    loadCurrentUser();
    setWelcomeText();
    setSubscene("Dashboard", "Dashboard");
  }

  private void loadCurrentUser() {
    String username = JsonUtils.getAsString(MainApp.CONFIG, "currentSession", "");
    for (JsonElement e : MainApp.USERS) {
      User user = new User(e.getAsJsonObject());
      if (user.getUsername().equals(username)) {
        CURRENT_USER = user;
        return;
      }
    }
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

  public void setWelcomeText() {
    welcomeLabel.setText(String.format("Welcome, %s [%s]",
        CURRENT_USER.getName(), CURRENT_USER.getUsername()));
  }

  @Override
  public void loadIcons() {
    String type = getLightOrDark();
    dashboardIcon.setImage(ImageUtils.getImage(type + "DashboardButton.png"));
    booksIcon.setImage(ImageUtils.getImage(type + "BooksButton.png"));
    signOutIcon.setImage(ImageUtils.getImage(type + "LogOutButton.png"));
    borrowsIcon.setImage(ImageUtils.getImage(type + "BorrowsButton.png"));
    usersIcon.setImage(ImageUtils.getImage(type + "UsersButton.png"));
    accountSettingsIcon.setImage(ImageUtils.getImage(type + "SettingsButton.png"));
  }

  @FXML
  private void signOut() {
    JsonUtils.addProperty(MainApp.CONFIG, MainApp.CONFIG_PATH, "currentSession", "");
    MainApp.setScene("SignIn");
    MainApp.showPopupMessage(String.format("Goodbye, %s. Looking forward to seeing you again.",
        CURRENT_USER.getUsername()), Color.DARKBLUE);
  }

  @FXML private void gotoChangePassword() { setSubscene("ChangePassword", "Account"); }
  @FXML private void gotoDashboard() { setSubscene("Dashboard", "Dashboard"); }
  @FXML private void gotoBooks() { setSubscene("BookSearch", "Book Search"); }
  @FXML private void gotoBorrows() { setSubscene("Borrows", "Borrows"); }
  @FXML private void gotoUsers() { setSubscene("UserManagement", "Users"); }
}
