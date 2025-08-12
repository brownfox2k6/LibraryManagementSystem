package com.application.librarymanagement.borrow;

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
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public final class BorrowsController {
  @FXML private VBox borrows;
  @FXML private Text usernameText;
  @FXML private TextField usernameField;

  private User user;
  private ArrayList<Borrow> borrowList;

  public void initialize() {
    user = InAppController.CURRENT_USER;
    if (user.getUserType() == User.TYPE_MEMBER) {
      usernameText.setVisible(false);
      usernameText.setManaged(false);
      usernameField.setVisible(false);
      usernameField.setManaged(false);
    }
    loadBorrows();
    displayBorrows(borrowList);
  }

  private void loadBorrows() {
    borrowList = new ArrayList<>();
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BORROWS_DB_PATH)) {
      Borrow borrow = new Borrow(e.getAsJsonObject());
      if (user.getUserType() == User.TYPE_ADMIN
          || borrow.getUsername().equals(user.getUsername())) {
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
  private void filterByStatusReturned() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_RETURNED);
  }

  @FXML
  private void filterByStatusCanceled() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_CANCELED);
  }

  @FXML
  private void filterByUsername() {
    displayBorrowsByUsernameAndStatus(0);
  }
}
