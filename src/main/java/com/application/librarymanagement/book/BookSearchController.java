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

  private ExecutorService executor = Executors.newSingleThreadExecutor();
  private Search search;
  private ArrayList<String> availableIds;

  public void initialize() {
    searchIcon.setImage(ImageUtils.getImage("SearchButton.png"));
    search = new Search();
    availableIds = new ArrayList<>();
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH)) {
      availableIds.add(Book.fromJsonObject(e.getAsJsonObject()).getId());
    }
  }

  @FXML
  public void doSearch() {
    MainApp.showPopupMessage("Searching... Please wait.", Color.DARKGREEN);
    search.setQ(q.getText());
    search.setIntitle(intitle.getText());
    search.setInauthor(inauthor.getText());
    search.setInpublisher(inpublisher.getText());
    search.setSubject(subject.getText());
    search.setIsbn(isbn.getText());
    search.setLccn(lccn.getText());
    search.setOclc(oclc.getText());

    Task<JsonArray> task = new Task<>() {
      @Override
      protected JsonArray call() {
        searchButton.setDisable(true);
        return search.getBooks();
      }
    };

    task.setOnSucceeded(event -> {
      searchButton.setDisable(false);
      JsonArray result = task.getValue();
      searchResults.getChildren().clear();
      if (result == null || result.isEmpty()) {
        MainApp.showPopupMessage("No books found.", Color.DARKRED);
        return;
      }
      MainApp.showPopupMessage(String.format("Found %d books.", result.size()), Color.DARKGREEN);
      try {
        for (JsonElement e : result) {
          Book book = Book.fromJsonObject(e.getAsJsonObject());
          if (InAppController.CURRENT_USER.getUserType() == User.TYPE_MEMBER
              && !availableIds.contains(book.getId())) {
            continue;
          }
          FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("scenes/BookCase2.fxml"));
          HBox bookCaseBox = fxmlLoader.load();
          BookCaseController bookCaseController = fxmlLoader.getController();
          bookCaseController.setData(book);
          searchResults.getChildren().add(bookCaseBox);
        }
      } catch (Exception ex) {
        MainApp.showPopupMessage("Failed to render search results.", Color.DARKRED);
      }
    });

    executor.execute(task);
  }
}
