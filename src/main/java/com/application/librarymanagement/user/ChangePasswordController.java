package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

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
      MainApp.showPopupMessage("Please fill in all required fields.");
      return;
    }
    if (currentUser.getPassword().equals(PasswordUtils.hashPassword(oldPass))) {
      MainApp.showPopupMessage("Incorrect old password.");
      return;
    }
    if (!newPass.equals(confirmPass)) {
      MainApp.showPopupMessage("New passwords do not match.");
      return;
    }
    currentUser.setPassword(newPass);
    if (currentUser.saveToDatabase(true)) {
      MainApp.showPopupMessage("Password changed successfully. Please sign in again.");
      MainApp.setScene("SignIn");
    } else {
      MainApp.showPopupMessage("Unable to save the new password. Please try again.");
    }
  }

  @FXML
  private void handleCancel() {
    MainApp.showPopupMessage("Canceled changing password.");
    MainApp.setScene("InApp");
  }
}
