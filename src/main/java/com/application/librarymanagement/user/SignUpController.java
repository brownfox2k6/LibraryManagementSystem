package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SignUpController extends MainAppController {
  @FXML private TextField usernameField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private PasswordField passwordField2;
  @FXML private Label errorLabel;

  @FXML private void switchToSignIn() {
    try {
      MainApp.setScene("SignIn");
    } catch (IOException ignored) {}
  }

  @FXML private void trySignUp() {
    String username = usernameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();
    String password2 = passwordField2.getText();
    if (!username.matches("^[a-zA-Z0-9._-]{3,20}$")) {
      showErrorLabel("Invalid username. Allowed characters: letters (a–z, A-Z), numbers (0–9), " +
                     "underscore (_), period (.), dash (-). Length: 3–20 characters");
    } else if (!email.matches("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$")) {
      showErrorLabel("Invalid email.");
    } else if (password.isEmpty() || !password.equals(password2)) {
      showErrorLabel("Password fields are empty or mismatched.");
    }
    /*
     * TODO: Add this user to json/users.json.
     * Implement this method in class User.
     *     public static void addUser(String name, String email, String password, String type) { ... }
     */
    // User.addUser(name, email, password, "member");
  }

  private void showErrorLabel(String s) {
    errorLabel.setText(s);
    errorLabel.setVisible(true);
  }
}
