package com.application.librarymanagement.user;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class AddUserController {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private RadioButton radioAdmin;
    @FXML
    private RadioButton radioMember;
    @FXML
    private ToggleGroup userTypeGroup;

    @FXML
    private void handleAddUser() {
        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields!");
            return;
        }

        int type = radioAdmin.isSelected() ? User.TYPE_ADMIN : User.TYPE_MEMBER;
        
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
