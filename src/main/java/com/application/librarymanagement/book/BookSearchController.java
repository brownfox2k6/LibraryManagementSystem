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
    MainApp.showPopupMessage("Searching... Please wait.", Color.DARKBLUE);
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
      searchResults.getChildren().clear();
      int count = 0;
      for (JsonElement e : task.getValue()) {
        Book book = Book.fromJsonObject(e.getAsJsonObject());
        if (InAppController.CURRENT_USER.getUserType() == User.TYPE_MEMBER
            && !availableIds.contains(book.getId())) {
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
      if (count == 0) {
        MainApp.showPopupMessage("No results found.", Color.DARKRED);
      } else if (count == 1) {
        MainApp.showPopupMessage("Found 1 result.", Color.DARKGREEN);
      } else {
        MainApp.showPopupMessage(String.format("Found %d results.", count), Color.DARKGREEN);
      }
    });
    executor.execute(task);
  }
}
