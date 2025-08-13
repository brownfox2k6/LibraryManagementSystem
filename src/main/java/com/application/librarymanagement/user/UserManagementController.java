package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        usernameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        roleColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().isAdmin() ? "Admin" : "Member"
        ));
        borrowedColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(
                data.getValue().isMember() ? data.getValue().getBorrows().size() : 0
        ));

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
        System.out.println("Search User clicked");
    }

    @FXML
    private void handleAddUser() {
        System.out.println("Add New User clicked");
    }

    @FXML
    private void handleDeleteUser() {
        System.out.println("Delete User clicked");
    }
}
