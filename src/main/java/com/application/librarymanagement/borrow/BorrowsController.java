package com.application.librarymanagement.borrow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonElement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public final class BorrowsController {
  @FXML private VBox borrows;
  @FXML private Text usernameText;
  @FXML private TextField usernameField;

  private User user;
  private ArrayList<Borrow> borrowList;

  public void initialize() {
    user = InAppController.CURRENT_USER;
    if (user.isMember()) {
      usernameText.setVisible(false);
      usernameText.setManaged(false);
      usernameField.setVisible(false);
      usernameField.setManaged(false);
    }
    loadBorrows();
    displayBorrowsByUsernameAndStatus(0);
  }

  private void loadBorrows() {
    borrowList = new ArrayList<>();

    HashSet<String> existingUsernames = new HashSet<>();
    for (JsonElement ue : JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH)) {
      existingUsernames.add(ue.getAsJsonObject().get("username").getAsString());
    }

    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BORROWS_DB_PATH)) {
      Borrow borrow = new Borrow(e.getAsJsonObject());

      if (user.isAdmin()) {
        if (existingUsernames.contains(borrow.getUsername())) {
          borrowList.add(borrow);
        }
      } else if (borrow.getUsername().equals(user.getUsername())) {
        borrowList.add(borrow);
      }
    }

    borrowList.sort(Comparator.comparing(Borrow::getLatestTimestamp, Comparator.reverseOrder()));
  }


  private void displayBorrows(ArrayList<Borrow> borrowList) {
    borrows.getChildren().clear();
    for (Borrow borrow : borrowList) {
      FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("scenes/BorrowCase.fxml"));
      HBox borrowCaseBox;
      try {
        borrowCaseBox = fxmlLoader.load();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      BorrowCaseController bcc = fxmlLoader.getController();
      bcc.setData(borrow);
      borrows.getChildren().add(borrowCaseBox);
    }
  }

  private void displayBorrowsByUsernameAndStatus(int status) {
    ArrayList<Borrow> list = new ArrayList<>();
    for (Borrow borrow : borrowList) {
      if (borrow.getUsername().contains(usernameField.getText())
          && (status == 0 || borrow.getStatus() == status)) {
        list.add(borrow);
      }
    }
    displayBorrows(list);
    StringBuilder sb = new StringBuilder("Filtered by: ");
    if (!usernameField.getText().isEmpty()) {
      sb.append("username contains ");
      sb.append('"');
      sb.append(usernameField.getText());
      sb.append('"');
      sb.append(status == 0? ". " : ", ");
    }
    if (status != 0) {
      sb.append("status=");
      sb.append(Borrow.getStatusText(status));
      sb.append(". ");
    }
    if (usernameField.getText().isEmpty() && status == 0) {
      sb.setLength(0);
    }
    if (list.isEmpty()) {
      sb.append("No records found.");
      MainApp.showPopupMessage(sb.toString(), Color.DARKRED);
    } else if (list.size() == 1) {
      sb.append("Found 1 record.");
      MainApp.showPopupMessage(sb.toString(), Color.DARKGREEN);
    } else {
      sb.append(String.format("Found %d records.", list.size()));
      MainApp.showPopupMessage(sb.toString(), Color.DARKGREEN);
    }
  }

  @FXML
  private void filterByStatusRequested() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_REQUESTED);
  }

  @FXML
  private void filterByStatusBorrowed() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_BORROWED);
  }

  @FXML
  private void filterByStatusReturned() { displayBorrowsByUsernameAndStatus(Borrow.STATUS_RETURNED); }

  @FXML
  private void filterByStatusCanceled() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_CANCELED);
  }

  @FXML
  private void filterByUsername() {
    displayBorrowsByUsernameAndStatus(0);
  }
}
