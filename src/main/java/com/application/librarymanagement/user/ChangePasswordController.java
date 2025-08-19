package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.InAppController;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.application.librarymanagement.utils.ValidateUtils;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public final class ChangePasswordController {
  @FXML private TextField name;
  @FXML private TextField email;
  @FXML private PasswordField oldPassword;
  @FXML private PasswordField newPassword;
  @FXML private PasswordField newPassword2;

  public void initialize() {
    name.setText(InAppController.CURRENT_USER.getName());
    email.setText(InAppController.CURRENT_USER.getEmail());
  }

  @FXML
  private void save() {
    String fullName = name.getText();
    String email = this.email.getText();
    String oldPass = oldPassword.getText();
    String newPass = newPassword.getText();
    String confirmPass = newPassword2.getText();
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
    if (!InAppController.CURRENT_USER.getEmail().equals(email) && ValidateUtils.isEmailExists(email)) {
      MainApp.showPopupMessage("This email has been used by another user.", Color.DARKRED);
      return;
    }
    boolean passwordChanged = false;
    if (!oldPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
      if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
        MainApp.showPopupMessage("Please fill in all required fields.", Color.DARKRED);
        return;
      }
      if (!newPass.equals(confirmPass)) {
        MainApp.showPopupMessage("New passwords do not match.", Color.DARKRED);
        return;
      }
      if (!InAppController.CURRENT_USER.getHashedPassword().equals(PasswordUtils.hashPassword(oldPass))) {
        MainApp.showPopupMessage("Incorrect old password.", Color.DARKRED);
        return;
      }
      InAppController.CURRENT_USER.setPassword(newPass);
      passwordChanged = true;
    }
    InAppController.CURRENT_USER.setName(fullName);
    InAppController.CURRENT_USER.setEmail(email);
    InAppController.CURRENT_USER.saveToDatabase();
    if (passwordChanged) {
      JsonUtils.addProperty(MainApp.CONFIG, MainApp.CONFIG_PATH, "currentSession", "");
      MainApp.setScene("SignIn");
      MainApp.showPopupMessage("Account info updated. Please sign in again.", Color.DARKGREEN);
    } else {
      InAppController.INSTANCE.setSubscene("Dashboard", "Dashboard");
      InAppController.INSTANCE.setWelcomeText();
      MainApp.showPopupMessage("Account info updated.", Color.DARKGREEN);
    }
  }

  @FXML
  private void cancel() {
    MainApp.setScene("InApp");
    MainApp.showPopupMessage("Canceled changing password.", Color.DARKBLUE);
  }
}
