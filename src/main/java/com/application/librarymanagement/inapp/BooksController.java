package com.application.librarymanagement.inapp;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.book.BookCaseController;
import com.application.librarymanagement.book.Search;
import com.application.librarymanagement.utils.ImageUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public final class BooksController extends InAppController {
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

  private Search search;

  public void initialize() {
    searchIcon.setImage(ImageUtils.getImage("SearchButton.png"));
    search = new Search();
  }

  @FXML
  public void doSearch() {
    search.setQ(q.getText());
    search.setIntitle(intitle.getText());
    search.setInauthor(inauthor.getText());
    search.setInpublisher(inpublisher.getText());
    search.setSubject(subject.getText());
    search.setIsbn(isbn.getText());
    search.setLccn(lccn.getText());
    search.setOclc(oclc.getText());
    try {
      JsonArray result = search.getBooks();
      searchResults.getChildren().clear();
      for (JsonElement e : result) {
        JsonObject book = e.getAsJsonObject();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("scenes/BookCase2.fxml"));
        HBox bookCaseBox = fxmlLoader.load();
        BookCaseController bookCaseController = fxmlLoader.getController();
        bookCaseController.setData(Book.fromJsonObject(book));
        searchResults.getChildren().add(bookCaseBox);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
