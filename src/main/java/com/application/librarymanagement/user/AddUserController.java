package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.MainAppController;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.utils.ValidateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class AddUserController {
  @FXML private TextField txtName;
  @FXML private TextField txtUsername;
  @FXML private TextField txtEmail;
  @FXML private PasswordField txtPassword;
  @FXML private PasswordField txtConfirmPassword;
  @FXML private ChoiceBox<String> roleChoiceBox;

  public void initialize() {
    roleChoiceBox.setValue("Member");
  }

  @FXML
  private void handleAddUser() {
    String name = txtName.getText().trim();
    String username = txtUsername.getText().trim();
    String email = txtEmail.getText().trim();
    String password = txtPassword.getText().trim();
    String confirmPassword = txtConfirmPassword.getText().trim();
    int type = roleChoiceBox.getSelectionModel().getSelectedItem().equals("Admin") ?
        User.TYPE_ADMIN : User.TYPE_MEMBER;
    if (name.isEmpty() || username.isEmpty() || email.isEmpty()
            || password.isEmpty() || confirmPassword.isEmpty()) {
      MainApp.showPopupMessage("Please fill in all fields!", Color.DARKRED);
      return;
    }
    if (!password.equals(confirmPassword)) {
      MainApp.showPopupMessage("Passwords do not match!", Color.DARKRED);
      return;
    }
    if (!ValidateUtils.isValidEmail(email)) {
      MainApp.showPopupMessage("Invalid email format!", Color.DARKRED);
      return;
    }
    if (!ValidateUtils.isValidUsername(username)) {
      MainApp.showPopupMessage("Invalid username format!", Color.DARKRED);
      return;
    }
    if (ValidateUtils.isEmailExists(email)) {
      MainApp.showPopupMessage("This email already exists in the system!", Color.DARKRED);
      return;
    }
    if (ValidateUtils.isUsernameExists(username)) {
      MainApp.showPopupMessage("This username already exists in the system!", Color.DARKRED);
      return;
    }
    User newUser = new User(name, username, email, password, type);
    newUser.saveToDatabase();
    MainApp.showPopupMessage("User added successfully!", Color.DARKGREEN);
    clearForm();
  }

  private void clearForm() {
    txtName.clear();
    txtUsername.clear();
    txtEmail.clear();
    txtPassword.clear();
    txtConfirmPassword.clear();
  }

  @FXML
  private void handleCancel() {
    InAppController.INSTANCE.setSubscene("UserManagement", "Users");
  }
}
