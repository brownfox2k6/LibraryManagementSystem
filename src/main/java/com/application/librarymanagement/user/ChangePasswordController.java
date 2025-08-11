package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ChangePasswordController {

    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label messageLabel;

    private User currentUser;
    private boolean resetMode = false; // true = reset pass, false = đổi pass thường

    public void setUser(User user) {
        // Lấy user mới nhất từ database để tránh dữ liệu cũ
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
        this.currentUser = user; // fallback
    }

    public void setResetMode(boolean resetMode) {
        this.resetMode = resetMode;
        if (resetMode) {
            oldPasswordField.setDisable(true); // Reset → không cần nhập pass cũ
        }
    }

    @FXML
    private void handleSave() {
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        System.out.println("DEBUG username: " + currentUser.getUsername());
        System.out.println("DEBUG email: " + currentUser.getEmail());

        // Kiểm tra nhập đủ
        if (newPass.isEmpty() || confirmPass.isEmpty() || (!resetMode && oldPass.isEmpty())) {
            showMessage("Vui lòng điền đầy đủ thông tin.", false);
            return;
        }

        // Nếu không reset → kiểm tra mật khẩu cũ
        if (!resetMode) {
            String hashedOldInput = PasswordUtils.hashPassword(oldPass);
            if (!hashedOldInput.equals(currentUser.getHashedPassword())) {
                showMessage("Mật khẩu cũ không đúng!", false);
                return;
            }
        }

        // Kiểm tra mật khẩu mới khớp
        if (!newPass.equals(confirmPass)) {
            showMessage("Mật khẩu mới không khớp!", false);
            return;
        }

        // Cập nhật mật khẩu (tự hash trong User.setPassword)
        currentUser.setPassword(newPass);
        boolean saved = currentUser.saveToDatabase(true);

        if (saved) {
            showMessage(resetMode ? "Reset mật khẩu thành công!" : "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.", true);
            closeWindow();
        } else {
            showMessage("Không thể lưu mật khẩu mới!", false);
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    private void closeWindow() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }
}
