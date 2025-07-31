package com.application.librarymanagement.inapp;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.book.BookCaseController;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public final class DashboardController extends InAppController {
  @FXML private GridPane recommendationsPane;
  @FXML private TableView mostBorrowsTable;

  public void initialize() {
    super.initialize();
    showRecommendations();
  }

  public void showRecommendations() {
    recommendationsPane.getChildren().clear();
    int cell = 0;
    JsonArray books = JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH);
    for (JsonElement e : books) {
      JsonObject book = e.getAsJsonObject();
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(MainApp.class.getResource("scenes/BookCase.fxml"));
      try {
        VBox bookCaseBox = fxmlLoader.load();
        BookCaseController bookCaseController = fxmlLoader.getController();
        bookCaseController.setData(Book.fromJsonObject(book));
        int row = cell / recommendationsPane.getColumnCount();
        int col = cell % recommendationsPane.getColumnCount();
        recommendationsPane.add(bookCaseBox, col, row);
        ++cell;
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }
}
