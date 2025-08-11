package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePasswordController {
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    private User currentUser;
    private boolean resetMode = false;

    public void setUser(User user) {
        if (user != null) {
            JsonArray users = JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH);
            for (JsonElement e : users) {
                JsonObject u = e.getAsJsonObject();
                if (u.get("username").getAsString().equalsIgnoreCase(user.getUsername()) ||
                        u.get("email").getAsString().equalsIgnoreCase(user.getEmail())) {
                    this.currentUser = new User(u);
                    return;
                }
            }
        }
        this.currentUser = user;
    }

    public void setResetMode(boolean resetMode) {
        this.resetMode = resetMode;
        if (resetMode) {
            oldPasswordField.setDisable(true);
        }
    }

    @FXML
    private void handleSave() {
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (newPass.isEmpty() || confirmPass.isEmpty() || (!resetMode && oldPass.isEmpty())) {
            MainApp.showPopupMessage("Please fill in all required fields.");
            return;
        }

        if (!resetMode) {
            String hashedOldInput = PasswordUtils.hashPassword(oldPass);
            if (!hashedOldInput.equals(currentUser.getPassword())) {
                MainApp.showPopupMessage("Incorrect old password!");
                return;
            }
        }

        if (!newPass.equals(confirmPass)) {
            MainApp.showPopupMessage("New passwords do not match!");
            return;
        }

        currentUser.setPassword(newPass);
        boolean saved = currentUser.saveToDatabase(true);

        if (saved) {
            MainApp.showPopupMessage(resetMode ?
                    "Password reset successfully!" :
                    "Password changed successfully! Please log in again.");
            closeWindow();
        } else {
            MainApp.showPopupMessage("Unable to save the new password!");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }
}
