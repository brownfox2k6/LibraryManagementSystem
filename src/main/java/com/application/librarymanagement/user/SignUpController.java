package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.utils.ValidateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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
    if (!ValidateUtils.isValidUsername(username)) {
      MainApp.showPopupMessage("Invalid username. Allowed characters: letters (a–z, A-Z), "
          + "numbers (0–9), underscore (_), period (.), dash (-). Length: 3–20 characters", Color.DARKRED);
    } else if (!ValidateUtils.isValidEmail(email)) {
      MainApp.showPopupMessage("Invalid email.", Color.DARKRED);
    } else if (password.isEmpty() || !password.equals(password2)) {
      MainApp.showPopupMessage("Password fields are empty or mismatched.", Color.DARKRED);
    } else if (ValidateUtils.isEmailExists(email)) {
      MainApp.showPopupMessage("This email has been used by another user.", Color.DARKRED);
    } else if (ValidateUtils.isUsernameExists(username)) {
      MainApp.showPopupMessage("This username has been used by another user.", Color.DARKRED);
    } else {
      User member = new User(name, username, email, password, User.TYPE_MEMBER);
      member.saveToDatabase();
      MainApp.showPopupMessage("Registration complete! You can now sign in.", Color.DARKGREEN);
      switchToSignIn();
    }
  }
}
