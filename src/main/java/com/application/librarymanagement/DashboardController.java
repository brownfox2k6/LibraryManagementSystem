package com.application.librarymanagement;

import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.book.BookCaseController;
import com.application.librarymanagement.book.BookDetailsController;
import com.application.librarymanagement.book.BookStats;
import com.application.librarymanagement.borrow.Borrow;
import com.google.gson.JsonElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class DashboardController {
  @FXML private HBox recommendations;
  @FXML private TableView<BookStats> mostBorrowsTable;
  @FXML private TableColumn<BookStats, Number> rankCol;
  @FXML private TableColumn<BookStats, String> titleCol;
  @FXML private TableColumn<BookStats, Number> countCol;
  @FXML private BarChart<String, Number> borrowsChart;

  private List<Book> books;

  public void initialize() {
    loadBooks();
    showRecommendations();
    showMostBorrowsTable();
    showBorrowsChart();
  }

  private void loadBooks() {
    books = new ArrayList<>();
    for (JsonElement e : MainApp.BOOKS) {
      books.add(Book.fromJsonObject(e.getAsJsonObject()));
    }
    books.sort(Comparator.comparing(Book::getBorrowsCount, Comparator.reverseOrder()).thenComparing(Book::getTitle));
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
    rankCol.setCellValueFactory(c -> c.getValue().rankProperty());
    titleCol.setCellValueFactory(c -> c.getValue().titleProperty());
    countCol.setCellValueFactory(c -> c.getValue().borrowsCountProperty());
    List<BookStats> stats = Book.toBookStatsList(books);
    ObservableList<BookStats> tableData = FXCollections.observableArrayList(stats);
    mostBorrowsTable.setItems(tableData);
    Runnable openDetails = () -> {
      BookStats bs = mostBorrowsTable.getSelectionModel().getSelectedItem();
      BookDetailsController bdc = InAppController.INSTANCE.setSubscene("BookDetails", "Book details");
      bdc.setData(bs.getBook());
    };
    mostBorrowsTable.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        openDetails.run();
      }
    });
    mostBorrowsTable.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        openDetails.run();
      }
    });
  }

  private void showBorrowsChart() {
    NumberAxis yAxis = (NumberAxis) borrowsChart.getYAxis();
    yAxis.setMinorTickVisible(false);
    yAxis.tickLabelFormatterProperty().setValue(new StringConverter<>() {
      @Override
      public String toString(Number object) {
        if (object.intValue() != object.doubleValue()) {
          return "";
        }
        return "" + object.intValue();
      }
      @Override
      public Number fromString(String string) {
        Number val = Double.parseDouble(string);
        return val.intValue();
      }
    });
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    for (Pair<String, Integer> p : Borrow.getRecentBorrows(30)) {
      XYChart.Data<String, Number> data = new XYChart.Data<>(p.getKey(), p.getValue());
      data.nodeProperty().addListener((observableValue, oldNode, node) -> {
        if (node != null) {
          displayLabelForData(data);
        }
      });
      series.getData().add(data);
    }
    borrowsChart.getData().add(series);
  }

  private void displayLabelForData(XYChart.Data<String, Number> data) {
    final Node node = data.getNode();
    final Text dataText = new Text(data.getYValue() + "");
    node.parentProperty().addListener((observableValue, oldParent, parent) -> {
      Group parentGroup = (Group) parent;
      parentGroup.getChildren().add(dataText);
    });
    node.boundsInParentProperty().addListener((ov, oldBounds, bounds) -> {
      dataText.setLayoutX(Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
      dataText.setLayoutY(Math.round(bounds.getMinY() - dataText.prefHeight(-1) * 0.5));
    });
  }
}
