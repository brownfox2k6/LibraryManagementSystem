package com.application.librarymanagement.book;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookCaseController {
  @FXML ImageView imageCase;
  @FXML Label authorCase;
  @FXML Label nameCase;

  private Book book;

  public void setData(Book book) {
    this.book = book;
    nameCase.setText(this.book.title);
    authorCase.setText(this.book.getAuthorsString());
    imageCase.setImage(new Image(this.book.thumbnailLink));
  }

  public void setData(JsonObject book) {
    this.book = Book.fromLocalJsonObject(book);
    nameCase.setText(this.book.title);
    authorCase.setText(this.book.getAuthorsString());
    imageCase.setImage(new Image(this.book.thumbnailLink));
  }
}
