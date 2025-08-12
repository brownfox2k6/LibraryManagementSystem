package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class BookSearchController {
  @FXML private Button searchButton;
  @FXML private ImageView searchIcon;
  @FXML private TextField q;
  @FXML private TextField intitle;
  @FXML private TextField inauthor;
  @FXML private TextField inpublisher;
  @FXML private TextField subject;
  @FXML private TextField isbn;
  @FXML private TextField lccn;
  @FXML private TextField oclc;
  @FXML private VBox searchResults;

  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private Search search;
  private final ArrayList<String> availableIds = new ArrayList<>();
  private static JsonArray results = new JsonArray();
  private static final String[] queries = {"", "", "", "", "", "", "", ""};

  public void initialize() {
    searchIcon.setImage(ImageUtils.getImage("SearchButton.png"));
    search = new Search();
    if (InAppController.CURRENT_USER.isMember()) {
      for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH)) {
        availableIds.add(Book.fromJsonObject(e.getAsJsonObject()).getId());
      }
    }
    showOldSearchQueries();
    showResults(false);
  }

  @FXML
  private void doSearch() {
    MainApp.showPopupMessage("Searching... Please wait.", Color.DARKBLUE);
    saveSearchQueries();
    search.setQ(q.getText());
    search.setIntitle(intitle.getText());
    search.setInauthor(inauthor.getText());
    search.setInpublisher(inpublisher.getText());
    search.setSubject(subject.getText());
    search.setIsbn(isbn.getText());
    search.setLccn(lccn.getText());
    search.setOclc(oclc.getText());
    Task<Void> task = new Task<>() {
      @Override
      protected Void call() {
        searchButton.setDisable(true);
        results = search.getBooks();
        return null;
      };
    };
    task.setOnSucceeded(e -> showResults(true));
    executor.execute(task);
  }

  private void showResults(boolean showPopup) {
    searchButton.setDisable(false);
    searchResults.getChildren().clear();
    int count = 0;
    for (JsonElement e : results) {
      Book book = Book.fromJsonObject(e.getAsJsonObject());
      if (InAppController.CURRENT_USER.isMember() && !availableIds.contains(book.getId())) {
        continue;
      }
      FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("scenes/BookCase2.fxml"));
      HBox bookCaseBox = null;
      try {
        bookCaseBox = fxmlLoader.load();
      } catch (IOException ex) {
        MainApp.showPopupMessage("Failed to render search results.", Color.DARKRED);
      }
      BookCaseController bookCaseController = fxmlLoader.getController();
      bookCaseController.setData(book);
      searchResults.getChildren().add(bookCaseBox);
      ++count;
    }
    if (!showPopup) {
      return;
    }
    if (count == 0) {
      MainApp.showPopupMessage("No results found.", Color.DARKRED);
    } else if (count == 1) {
      MainApp.showPopupMessage("Found 1 result.", Color.DARKGREEN);
    } else {
      MainApp.showPopupMessage(String.format("Found %d results.", count), Color.DARKGREEN);
    }
  }

  private void saveSearchQueries() {
    queries[0] = q.getText();
    queries[1] = intitle.getText();
    queries[2] = inauthor.getText();
    queries[3] = inpublisher.getText();
    queries[4] = subject.getText();
    queries[5] = isbn.getText();
    queries[6] = lccn.getText();
    queries[7] = oclc.getText();
  }

  private void showOldSearchQueries() {
    q.setText(queries[0]);
    intitle.setText(queries[1]);
    inauthor.setText(queries[2]);
    inpublisher.setText(queries[3]);
    subject.setText(queries[4]);
    isbn.setText(queries[5]);
    lccn.setText(queries[6]);
    oclc.setText(queries[7]);
  }
}
