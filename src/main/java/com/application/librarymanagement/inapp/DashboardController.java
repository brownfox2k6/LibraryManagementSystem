package com.application.librarymanagement.inapp;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.book.BookCaseController;
import com.application.librarymanagement.book.BookStats;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public final class DashboardController extends InAppController {
  @FXML private HBox recommendations;
  @FXML private TableView<BookStats> mostBorrowsTable;
  @FXML private TableColumn<BookStats, Number> rankColumn;
  @FXML private TableColumn<BookStats, String> titleColumn;
  @FXML private TableColumn<BookStats, Number> borrowsCountColumn;

  private JsonArray books;

  public void initialize() {
    books = JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH);
    showRecommendations();
    showMostBorrowsTable();
  }

  public void showRecommendations() {
    recommendations.getChildren().clear();
    for (JsonElement e : books) {
      JsonObject book = e.getAsJsonObject();
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(MainApp.class.getResource("scenes/BookCase1.fxml"));
      try {
        VBox bookCaseBox = fxmlLoader.load();
        BookCaseController bookCaseController = fxmlLoader.getController();
        bookCaseController.setData(Book.fromJsonObject(book));
        recommendations.getChildren().add(bookCaseBox);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  public void showMostBorrowsTable() {
    rankColumn.setCellValueFactory(c -> c.getValue().rankProperty());
    titleColumn.setCellValueFactory(c -> c.getValue().titleProperty());
    borrowsCountColumn.setCellValueFactory(c -> c.getValue().borrowsCountProperty());
    List<BookStats> stats = Book.toBookStatsList(books);
    ObservableList<BookStats> tableData = FXCollections.observableArrayList(stats);
    mostBorrowsTable.setItems(tableData);
  }
}
