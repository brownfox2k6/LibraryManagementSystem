package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

/**
 * Controller class for handling the user sign-up process in the Library Management System.
 * <p>
 * This controller manages user input validation, displays error messages,
 * and handles scene transitions between sign-up and sign-in screens.
 */
public final class SignUpController extends MainAppController {
  @FXML private TextField nameField;
  @FXML private TextField usernameField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private PasswordField passwordField2;

  /**
   * Switches from the sign-in scene to the sign-up scene.
   * This method is triggered by a UI event (e.g., clicking a "Sign In" link or button).
   */
  @FXML
  private void switchToSignIn() {
    MainApp.setScene("SignIn");
  }

  /**
   * Handles the sign-up process when the user submits the sign-up form.
   * <p>
   * This method validates the following:
   * <ul>
   *   <li>Username format (allowed characters and length)</li>
   *   <li>Email format</li>
   *   <li>Passwords are non-empty and matching</li>
   * </ul>
   * <p>
   * If validation fails, an appropriate error message is displayed.
   * If successful, the user data is saved to the local JSON database. Upon successful sign-up,
   * the scene redirects to sign in after a short delay.
   */
  @FXML
  private void trySignUp() {
    String name = nameField.getText();
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
    } else {
      Member member = new Member(name, username, email, password);
      if (!member.saveToDatabase(false)) {
        showErrorLabel("Username or Email already exists.");
      } else {
        errorLabel.setTextFill(Paint.valueOf("GREEN"));
        showErrorLabel("Registration successful. Redirecting to sign-in...");
        PauseTransition pause = new PauseTransition(Duration.millis(1000));
        pause.setOnFinished(event -> {
          switchToSignIn();
        });
        pause.play();
      }
    }
  }
}
