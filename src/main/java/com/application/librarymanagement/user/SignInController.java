package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SignInController extends MainAppController {
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorLabel;

  @FXML private void switchToSignUp() {
    try {
      MainApp.setScene("SignUp");
    } catch (IOException ignored) {}
  }

  @FXML private void trySignIn() {
    String username = usernameField.getText();
    String password = PasswordUtils.hashPassword(passwordField.getText());
    JsonArray users = JsonUtils.loadLocalJson("users").get("users").getAsJsonArray();
    for (JsonElement element : users) {
      JsonObject user = element.getAsJsonObject();
      String u = JsonUtils.getAsString(user, "username");
      String p = JsonUtils.getAsString(user, "password");
      assert u != null && p != null;
      if (u.equals(username) && p.equals(password)) {
        // TODO: Implement login successfully.
        errorLabel.setVisible(false);
        return;
      }
    }
    showErrorLabel("Wrong username or password. Please try again.");
  }

  private void showErrorLabel(String s) {
    errorLabel.setText(s);
    errorLabel.setVisible(true);
  }
}
