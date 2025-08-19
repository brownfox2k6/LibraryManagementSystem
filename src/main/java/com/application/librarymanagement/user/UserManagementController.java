package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.InAppController;
import com.application.librarymanagement.borrow.BorrowsController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public final class UserManagementController {
  @FXML private TableView<User> userTable;
  @FXML private TableColumn<User, String> usernameCol;
  @FXML private TableColumn<User, String> nameCol;
  @FXML private TableColumn<User, String> emailCol;
  @FXML private TableColumn<User, String> roleCol;
  @FXML private TableColumn<User, Integer> borrowedCol;
  @FXML private TableColumn<User, Void> borrowsActionCol;
  @FXML private TextField searchField;
  @FXML private Button addUserButton;
  @FXML private HBox searchBox;
  @FXML private Label resultLabel;

  private final ObservableList<User> userList    = FXCollections.observableArrayList();
  private final ObservableList<User> displayList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    usernameCol.setCellValueFactory(cd ->
        new javafx.beans.property.SimpleStringProperty(cd.getValue().getUsername())
    );
    nameCol.setCellValueFactory(cd ->
        new javafx.beans.property.SimpleStringProperty(cd.getValue().getName())
    );
    emailCol.setCellValueFactory(cd ->
        new javafx.beans.property.SimpleStringProperty(cd.getValue().getEmail())
    );
    roleCol.setCellValueFactory(cd ->
        new javafx.beans.property.SimpleStringProperty(cd.getValue().isAdmin() ? "Admin" : "Member")
    );
    borrowedCol.setCellValueFactory(cd ->
        new javafx.beans.property.SimpleObjectProperty<>(
          cd.getValue().isMember() ? cd.getValue().getBorrows().size() : null)
    );
    if (borrowsActionCol != null) {
      borrowsActionCol.setCellFactory(col -> new TableCell<>() {
        private final Button viewBtn = new Button("View");
        {
          viewBtn.setOnAction(evt -> {
            User rowUser = getTableView().getItems().get(getIndex());
            BorrowsController bc = InAppController.INSTANCE.setSubscene("Borrows", "Borrows");
              bc.filterByUsernameExternal(rowUser.getUsername());
          });
          viewBtn.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-background-radius: 6;");
        }
        @Override
        protected void updateItem(Void item, boolean empty) {
          super.updateItem(item, empty);
          if (empty) {
            setGraphic(null);
          } else {
            User rowUser = getTableView().getItems().get(getIndex());
            setGraphic(rowUser.isMember() ? viewBtn : null);
          }
        }
      });
    }
    loadUsers();
    applyRoleView();
    searchField.textProperty().addListener((obs, o, n) -> filterByUsername(n));
  }

  private void loadUsers() {
    userList.clear();
    JsonArray usersData = MainApp.USERS;
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
    if (current.isMember()) {
      addUserButton.setVisible(false);
      searchBox.setVisible(false);
      for (User u : userList) {
        if (u.isAdmin() || u.getUsername().equals(current.getUsername())) {
          displayList.add(u);
        }
      }
    } else {
      displayList.addAll(userList);
      addUserButton.setVisible(true);
      if (searchBox != null) searchBox.setVisible(true);
      borrowsActionCol.setVisible(true);
    }
    userTable.setItems(displayList);
    resultLabel.setText(String.format("Found %d records.", displayList.size()));
  }

  @FXML
  private void searchUser() {
    assert InAppController.CURRENT_USER.isMember();
    filterByUsername(searchField.getText());
  }

  private void filterByUsername(String keyword) {
    String q = keyword.trim().toLowerCase();
    displayList.clear();
    for (User u : userList) {
      if (u.getUsername().toLowerCase().contains(q)) {
        displayList.add(u);
      }
    }
    userTable.setItems(displayList);
    resultLabel.setText(String.format("Found %d records.", displayList.size()));
  }

  @FXML
  private void gotoAddUser() {
    assert InAppController.CURRENT_USER.isMember();
    InAppController.INSTANCE.setSubscene("AddUser", "Users");
  }
}
