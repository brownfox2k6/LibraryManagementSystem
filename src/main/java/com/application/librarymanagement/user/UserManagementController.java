package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.borrow.BorrowsController;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class UserManagementController {
  @FXML private TableView<User> userTable;
  @FXML private TableColumn<User, String> usernameColumn;
  @FXML private TableColumn<User, String> nameColumn;
  @FXML private TableColumn<User, String> emailColumn;
  @FXML private TableColumn<User, String> roleColumn;
  @FXML private TableColumn<User, Integer> borrowedColumn;
  @FXML private TableColumn<User, Void> borrowsActionColumn;
  @FXML private TextField searchField;
  @FXML private Button addUserBtn;
  @FXML private HBox searchBox;
  @FXML private Label resultLabel;

  private final ObservableList<User> userList    = FXCollections.observableArrayList();
  private final ObservableList<User> displayList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
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
    borrowedColumn.setCellValueFactory(cd ->
        new javafx.beans.property.SimpleObjectProperty<>(
          cd.getValue().isMember() ? cd.getValue().getBorrows().size() : null)
    );
    if (borrowsActionColumn != null) {
      borrowsActionColumn.setCellFactory(col -> new TableCell<>() {
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
    if (current.isMember()) {
      addUserBtn.setVisible(false);
      searchBox.setVisible(false);
      for (User u : userList) {
        if (u.isAdmin() || u.getUsername().equals(current.getUsername())) {
          displayList.add(u);
        }
      }
    } else {
      displayList.addAll(userList);
      addUserBtn.setVisible(true);
      if (searchBox != null) searchBox.setVisible(true);
      borrowsActionColumn.setVisible(true);
    }
    userTable.setItems(displayList);
    resultLabel.setText(String.format("Found %d records.", displayList.size()));
  }

  @FXML
  private void handleSearchUser() {
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
  private void handleAddUser() {
    assert InAppController.CURRENT_USER.isMember();
    InAppController.INSTANCE.setSubscene("AddUser", "Users");
  }
}
