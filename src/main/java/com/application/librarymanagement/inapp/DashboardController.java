package com.application.librarymanagement.inapp;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.book.BookCaseController;
import com.application.librarymanagement.book.BookStats;
import com.application.librarymanagement.borrow.Borrow;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class DashboardController {
  @FXML private HBox recommendations;
  @FXML private TableView<BookStats> mostBorrowsTable;
  @FXML private TableColumn<BookStats, Number> rankColumn;
  @FXML private TableColumn<BookStats, String> titleColumn;
  @FXML private TableColumn<BookStats, Number> borrowsCountColumn;
  @FXML private BarChart<String, Integer> borrowsChart;

  private ArrayList<Book> books;

  public void initialize() {
    loadBooks();
    showRecommendations();
    showMostBorrowsTable();
    showBorrowsChart();
  }

  private void loadBooks() {
    books = new ArrayList<>();
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH)) {
      books.add(Book.fromJsonObject(e.getAsJsonObject()));
    }
    books.sort(Comparator.comparing(Book::getBorrowsCount, Comparator.reverseOrder())
                         .thenComparing(Book::getTitle));
  }

  private void showRecommendations() {
    recommendations.getChildren().clear();
    for (Book book : books) {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(MainApp.class.getResource("scenes/BookCase1.fxml"));
      try {
        VBox bookCaseBox = fxmlLoader.load();
        BookCaseController bookCaseController = fxmlLoader.getController();
        bookCaseController.setData(book);
        recommendations.getChildren().add(bookCaseBox);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private void showMostBorrowsTable() {
    rankColumn.setCellValueFactory(c -> c.getValue().rankProperty());
    titleColumn.setCellValueFactory(c -> c.getValue().titleProperty());
    borrowsCountColumn.setCellValueFactory(c -> c.getValue().borrowsCountProperty());
    List<BookStats> stats = Book.toBookStatsList(books);
    ObservableList<BookStats> tableData = FXCollections.observableArrayList(stats);
    mostBorrowsTable.setItems(tableData);
  }

  private void showBorrowsChart() {
    XYChart.Series<String, Integer> series = new XYChart.Series<>();
    for (Pair<String, Integer> p : Borrow.getRecentBorrows(30)) {
      series.getData().add(new XYChart.Data<>(p.getKey(), p.getValue()));
    }
    borrowsChart.getData().add(series);
  }
}
