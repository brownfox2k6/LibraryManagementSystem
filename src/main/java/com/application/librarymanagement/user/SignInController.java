package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;

  @FXML private void switchToSignUp() {
    MainApp.setScene("SignUp");
  }

  @FXML
  private void trySignIn() {
    String username = usernameField.getText();
    String password = PasswordUtils.hashPassword(passwordField.getText());
    JsonArray users = JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH);
    for (JsonElement element : users) {
      JsonObject user = element.getAsJsonObject();
      String u = JsonUtils.getAsString(user, "username", "");
      String p = JsonUtils.getAsString(user, "password", "");
      if (u.equals(username) && p.equals(password)) {
        JsonUtils.addProperty(MainApp.config, MainApp.CONFIG_PATH, "currentSession", u);
        MainApp.showPopupMessage(String.format("Welcome, %s.", u), Color.DARKGREEN);
        MainApp.setScene("InApp");
        return;
      }
    }
    MainApp.showPopupMessage("Wrong username or password. Please try again.", Color.DARKRED);
  }
}
