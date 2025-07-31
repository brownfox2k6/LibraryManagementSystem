package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.io.IOException;

public class SignInController extends MainAppController {
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorLabel;

  @FXML private void switchToSignUp() {
    MainApp.setScene("SignUp");
  }

  @FXML private void trySignIn() {
    String username = usernameField.getText();
    String password = PasswordUtils.hashPassword(passwordField.getText());
    JsonArray users = JsonUtils.loadLocalJsonAsArray(USERS_DB_PATH);
    for (JsonElement element : users) {
      JsonObject user = element.getAsJsonObject();
      String u = JsonUtils.getAsString(user, "username", "");
      String p = JsonUtils.getAsString(user, "password", "");
      assert u != null && p != null;
      if (u.equals(username) && p.equals(password)) {
        JsonUtils.addProperty(config, CONFIG_PATH, "currentSession", u);
        errorLabel.setTextFill(Paint.valueOf("GREEN"));
        showErrorLabel("Sign in successful. Redirecting to dashboard...");
        PauseTransition pause = new PauseTransition(Duration.millis(1000));
        pause.setOnFinished(event -> {
          setScene("InApp");
        });
        pause.play();
        return;
      }
    }
    showErrorLabel("Wrong username or password. Please try again.");
  }

  protected void showErrorLabel(String s) {
    errorLabel.setText(s);
    errorLabel.setVisible(true);
  }
}
