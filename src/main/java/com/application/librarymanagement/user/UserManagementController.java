package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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

    usernameColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername())
    );
    nameColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getName())
    );
    emailColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail())
    );
    roleColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                    data.getValue().isAdmin() ? "Admin" : "Member"
            )
    );
    borrowedColumn.setCellValueFactory(data -> {
      if (data.getValue().isMember()) {
        return new javafx.beans.property.SimpleObjectProperty<>(
                data.getValue().getBorrows().size()
        );
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
      if (u.getUsername().toLowerCase().contains(keyword)) {
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
    try {
      FXMLLoader loader = new FXMLLoader(
              getClass().getResource("/com/application/librarymanagement/scenes/AddUser.fxml")
      );
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle("Add New User");
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();

      loadUsers();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
