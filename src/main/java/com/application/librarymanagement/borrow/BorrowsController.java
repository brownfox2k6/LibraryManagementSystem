package com.application.librarymanagement.borrow;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public final class BorrowsController {
  @FXML private VBox borrows;

  private User user;
  JsonArray borrowList;

  public void initialize() {
    user = InAppController.CURRENT_USER;
    loadBorrows();
    displayBorrows();
  }

  private void loadBorrows() {
    borrowList = new JsonArray();
    JsonArray arr = JsonUtils.loadLocalJsonAsArray(MainApp.BORROWS_DB_PATH);
    for (JsonElement e : arr) {
      Borrow borrow = new Borrow(e.getAsJsonObject());
      if (user.getUserType() == User.TYPE_ADMIN
          || borrow.getUsername().equals(user.getUsername())) {
        borrowList.add(borrow.getData());
      }
    }
  }

  private void displayBorrows() {
    borrows.getChildren().clear();
    for (JsonElement e : borrowList) {
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("scenes/BorrowCase.fxml"));
        HBox borrowCaseBox = fxmlLoader.load();
        BorrowCaseController controller = fxmlLoader.getController();
        controller.setData(new Borrow(e.getAsJsonObject()));
        borrows.getChildren().add(borrowCaseBox);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }
}
