package com.application.librarymanagement;

import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonElement;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Controller class for handling user sign-in functionality in the Library Management System.
 * <p>
 * This class manages input validation, authentication of users against local JSON data,
 * user feedback via the error label, and scene transitions between sign-in and sign-up views.
 */
public final class SignInController extends MainAppController {
  @FXML private TextField username;
  @FXML private PasswordField password;

  @FXML
  private void gotoSignUp() {
    MainApp.setScene("SignUp");
  }

  @FXML
  private void trySignIn() {
    String username = this.username.getText();
    String password = this.password.getText();
    if (username.isEmpty() && password.isEmpty()) {
      MainApp.showPopupMessage("Both username and password are required.", Color.DARKRED);
      return;
    }
    if (username.isEmpty()) {
      MainApp.showPopupMessage("Username is empty.", Color.DARKRED);
      return;
    }
    if (password.isEmpty()) {
      MainApp.showPopupMessage("Password is empty.", Color.DARKRED);
      return;
    }
    password = PasswordUtils.hashPassword(password);
    for (JsonElement e : MainApp.USERS) {
      User user = new User(e.getAsJsonObject());
      if (username.equals(user.getUsername()) && password.equals(user.getHashedPassword())) {
        JsonUtils.addProperty(MainApp.CONFIG, MainApp.CONFIG_PATH, "currentSession", user.getUsername());
        MainApp.setScene("InApp");
        MainApp.showPopupMessage(String.format("Welcome, %s.", user.getUsername()), Color.DARKGREEN);
        return;
      }
    }
    MainApp.showPopupMessage("Wrong username or password. Please try again.", Color.DARKRED);
  }
}
