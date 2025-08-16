package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
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
import javafx.scene.layout.HBox;
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
  @FXML private Button addUserBtn;
  @FXML private HBox searchBox;

  private final ObservableList<User> userList    = FXCollections.observableArrayList();
  private final ObservableList<User> displayList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    userTable.setPlaceholder(new Label(""));

    usernameColumn.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getUsername())
    );
    nameColumn.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getName())
    );
    emailColumn.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getEmail())
    );
    roleColumn.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(cd.getValue().isAdmin() ? "Admin" : "Member")
    );
    borrowedColumn.setCellValueFactory(cd -> {
      if (cd.getValue().isMember()) {
        return new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getBorrows().size());
      } else {
        return new javafx.beans.property.SimpleObjectProperty<>(null);
      }
    });

    loadUsers();

    applyRoleView();

    if (InAppController.CURRENT_USER != null && InAppController.CURRENT_USER.isAdmin()) {
      searchField.textProperty().addListener((obs, o, n) -> filterByUsername(n));
    }
  }

  private void loadUsers() {
    userList.clear();
    JsonArray usersData = JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH);
    if (usersData != null) {
      for (int i = 0; i < usersData.size(); i++) {
        JsonObject obj = usersData.get(i).getAsJsonObject();
        userList.add(new User(obj));
      }
    }
  }

  private void applyRoleView() {
    User current = InAppController.CURRENT_USER;

    displayList.clear();
    if (current != null && current.isMember()) {
      addUserBtn.setVisible(false);
      if (searchBox != null) searchBox.setVisible(false);

      for (User u : userList) {
        if (u.isAdmin() || u.getUsername().equals(current.getUsername())) {
          displayList.add(u);
        }
      }
    } else {
      displayList.addAll(userList);
      addUserBtn.setVisible(true);
      if (searchBox != null) searchBox.setVisible(true);
    }

    userTable.setItems(displayList);
  }

  @FXML
  private void handleSearchUser() {
    if (InAppController.CURRENT_USER != null && InAppController.CURRENT_USER.isAdmin()) {
      filterByUsername(searchField.getText());
    }
  }

  private void filterByUsername(String keyword) {
    if (keyword == null) keyword = "";
    String q = keyword.trim().toLowerCase();

    displayList.clear();
    if (q.isEmpty()) {
      displayList.addAll(userList);
    } else {
      for (User u : userList) {
        if (u.getUsername().toLowerCase().contains(q)) {
          displayList.add(u);
        }
      }
    }
    userTable.setItems(displayList);
  }

  @FXML
  private void handleAddUser() {
    if (InAppController.CURRENT_USER != null && InAppController.CURRENT_USER.isMember()) return;

    try {
      FXMLLoader loader = new FXMLLoader(
              MainApp.class.getResource("scenes/AddUser.fxml")
      );
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle("Add New User");
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();

      loadUsers();
      applyRoleView();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
