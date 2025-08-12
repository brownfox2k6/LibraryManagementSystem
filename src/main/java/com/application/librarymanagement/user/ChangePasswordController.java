package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

public final class ChangePasswordController {
  @FXML private PasswordField oldPasswordField;
  @FXML private PasswordField newPasswordField;
  @FXML private PasswordField confirmPasswordField;

  private User currentUser;

  public void initialize() {
    currentUser = InAppController.CURRENT_USER;
  }

  @FXML
  private void handleSave() {
    String oldPass = oldPasswordField.getText();
    String newPass = newPasswordField.getText();
    String confirmPass = confirmPasswordField.getText();
    if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
      MainApp.showPopupMessage("Please fill in all required fields.", Color.DARKRED);
      return;
    }
    if (!currentUser.getHashedPassword().equals(PasswordUtils.hashPassword(oldPass))) {
      MainApp.showPopupMessage("Incorrect old password.", Color.DARKRED);
      return;
    }
    if (!newPass.equals(confirmPass)) {
      MainApp.showPopupMessage("New passwords do not match.", Color.DARKRED);
      return;
    }
    currentUser.setPassword(newPass);
    if (currentUser.saveToDatabase(true)) {
      MainApp.showPopupMessage("Password changed successfully. Please sign in again.", Color.DARKGREEN);
      MainApp.setScene("SignIn");
    } else {
      MainApp.showPopupMessage("Unable to save the new password. Please try again.", Color.DARKRED);
    }
  }

  @FXML
  private void handleCancel() {
    MainApp.showPopupMessage("Canceled changing password.", Color.DARKGREEN);
    MainApp.setScene("InApp");
  }
}
