package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.borrow.Borrow;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class UserManagementController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Integer> borrowedColumn;
    @FXML private TextField searchField;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        userTable.setPlaceholder(new Label(""));

        usernameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        roleColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().isAdmin() ? "Admin" : "Member"
        ));
        borrowedColumn.setCellValueFactory(data -> {
            if (data.getValue().isMember()) {
                return new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getBorrows().size());
            } else {
                return new javafx.beans.property.SimpleObjectProperty<>(null);
            }
        });

        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        JsonArray usersData = JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH);
        for (int i = 0; i < usersData.size(); i++) {
            JsonObject obj = usersData.get(i).getAsJsonObject();
            userList.add(new User(obj));
        }
        userTable.setItems(userList);
    }

    @FXML
    private void handleSearchUser() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadUsers();
            return;
        }
        ObservableList<User> filtered = FXCollections.observableArrayList();
        for (User u : userList) {
            if (u.getUsername().toLowerCase().contains(keyword) ||
                    u.getName().toLowerCase().contains(keyword) ||
                    u.getEmail().toLowerCase().contains(keyword)) {
                filtered.add(u);
            }
        }
        userTable.setItems(filtered);

        if (filtered.isEmpty()) {
            MainApp.showPopupMessage("No results found.", Color.DARKRED);
        }
    }

    @FXML
    private void handleAddUser() {
        System.out.println("Add New User clicked");
    }

    private boolean hasUnreturnedBooks(User user) {
        for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BORROWS_DB_PATH)) {
            Borrow borrow = new Borrow(e.getAsJsonObject());
            if (borrow.getUsername().equals(user.getUsername())
                    && borrow.getStatus() == Borrow.STATUS_BORROWED) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            MainApp.showPopupMessage("Select a user to delete.", Color.DARKRED);
            return;
        }

        if (selected.isMember() && hasUnreturnedBooks(selected)) {
            MainApp.showPopupMessage("Cannot delete: User has borrowed books.", Color.DARKRED);
            return;
        }

        JsonArray users = JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH);
        for (int i = 0; i < users.size(); i++) {
            JsonObject obj = users.get(i).getAsJsonObject();
            if (obj.get("username").getAsString().equals(selected.getUsername())) {
                users.remove(i);
                break;
            }
        }
        JsonUtils.saveToFile(users, MainApp.USERS_DB_PATH);
        loadUsers();

        MainApp.showPopupMessage(
                "User deleted successfully.",
                Color.DARKGREEN
        );
    }

}
