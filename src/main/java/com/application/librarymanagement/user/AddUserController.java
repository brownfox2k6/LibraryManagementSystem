package com.application.librarymanagement.user;

import com.application.librarymanagement.utils.ValidateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class AddUserController {

    @FXML private TextField txtName;
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private RadioButton radioAdmin;
    @FXML private RadioButton radioMember;
    @FXML private ToggleGroup userTypeGroup;

    @FXML private void handleAddUser() {
        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        int type = radioAdmin.isSelected() ? 1 : 2;

        if (name.isEmpty() || username.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in all fields!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        if (!ValidateUtils.isValidEmail(email)) {
            showAlert("Error", "Invalid email format!");
            return;
        }

        if (!ValidateUtils.isValidUsername(username)) {
            showAlert("Error", "Invalid username format!");
            return;
        }

        if (ValidateUtils.isEmailExists(email)) {
            showAlert("Error", "This email already exists in the system!");
            return;
        }

        if (ValidateUtils.isUsernameExists(username)) {
            showAlert("Error", "This username already exists in the system!");
            return;
        }

        User newUser = new User(name, username, email, password, type);
        newUser.saveToDatabase();

        showAlert("Success", "User added successfully!");
        clearForm();
    }

    private void clearForm() {
        txtName.clear();
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        userTypeGroup.selectToggle(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
