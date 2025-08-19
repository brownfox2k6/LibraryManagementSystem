package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.InAppController;
import com.application.librarymanagement.utils.ValidateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public final class AddUserController {
  @FXML private TextField name;
  @FXML private TextField username;
  @FXML private TextField email;
  @FXML private PasswordField password;
  @FXML private PasswordField password2;
  @FXML private ChoiceBox<String> roleChoiceBox;

  public void initialize() {
    roleChoiceBox.setValue("Member");
  }

  @FXML
  private void save() {
    String name = this.name.getText().trim();
    String username = this.username.getText().trim();
    String email = this.email.getText().trim();
    String password = this.password.getText().trim();
    String password2 = this.password2.getText().trim();
    int type = roleChoiceBox.getSelectionModel().getSelectedItem().equals("Admin") ?
        User.TYPE_ADMIN : User.TYPE_MEMBER;
    if (name.isEmpty() || username.isEmpty() || email.isEmpty()
            || password.isEmpty() || password2.isEmpty()) {
      MainApp.showPopupMessage("Please fill in all fields!", Color.DARKRED);
      return;
    }
    if (!password.equals(password2)) {
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

  @FXML
  private void cancel() {
    InAppController.INSTANCE.setSubscene("UserManagement", "Users");
  }

  private void clearForm() {
    name.clear();
    username.clear();
    email.clear();
    password.clear();
    password2.clear();
  }
}
