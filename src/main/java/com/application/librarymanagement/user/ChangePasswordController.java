package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.application.librarymanagement.utils.ValidateUtils;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public final class ChangePasswordController {
  @FXML private TextField nameField;
  @FXML private TextField emailField;
  @FXML private PasswordField oldPasswordField;
  @FXML private PasswordField newPasswordField;
  @FXML private PasswordField confirmPasswordField;

  private User currentUser;

  public void initialize() {
    currentUser = InAppController.CURRENT_USER;
    nameField.setText(currentUser.getName());
    emailField.setText(currentUser.getEmail());
  }

  @FXML
  private void handleSave() {
    String fullName = nameField.getText();
    String email = emailField.getText();
    String oldPass = oldPasswordField.getText();
    String newPass = newPasswordField.getText();
    String confirmPass = confirmPasswordField.getText();
    if (fullName.isEmpty()) {
      MainApp.showPopupMessage("Full Name field is empty.", Color.DARKRED);
    }
    if (email.isEmpty()) {
      MainApp.showPopupMessage("Email field is empty.", Color.DARKRED);
    }
    if (!email.matches("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$")) {
      MainApp.showPopupMessage("Invalid email.", Color.DARKRED);
      return;
    }
    if (!currentUser.getEmail().equals(email) && ValidateUtils.isEmailExists(email)) {
      MainApp.showPopupMessage("This email has been used by another user.", Color.DARKRED);
      return;
    }
    if (!oldPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
      if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
        MainApp.showPopupMessage("Please fill in all required fields.", Color.DARKRED);
        return;
      }
      if (!newPass.equals(confirmPass)) {
        MainApp.showPopupMessage("New passwords do not match.", Color.DARKRED);
        return;
      }
      if (!currentUser.getHashedPassword().equals(PasswordUtils.hashPassword(oldPass))) {
        MainApp.showPopupMessage("Incorrect old password.", Color.DARKRED);
        return;
      }
      currentUser.setPassword(newPass);
    }
    currentUser.setName(fullName);
    currentUser.setEmail(email);
    currentUser.saveToDatabase();
    JsonUtils.addProperty(MainApp.config, MainApp.CONFIG_PATH, "currentSession", "");
    MainApp.setScene("SignIn");
    MainApp.showPopupMessage("Account info updated. Please sign in again.", Color.DARKGREEN);
  }

  @FXML
  private void handleCancel() {
    MainApp.setScene("InApp");
    MainApp.showPopupMessage("Canceled changing password.", Color.DARKBLUE);
  }
}
