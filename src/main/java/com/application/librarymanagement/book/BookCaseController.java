package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public final class BookCaseController {
  @FXML private Label title;
  @FXML private Label author;
  @FXML private Label publisher;
  @FXML private Label description;
  @FXML private ImageView thumbnail;
  @FXML private Pane container;

  private Book book;

  public void initialize() {
    setContainerEffects();
  }

  public void setData(Book book) {
    this.book = book;
    setTitle();
    setAuthorsAndPublishedDate();
    setPublisher();
    setDescription();
    setThumbnail();
  }

  private void setTitle() {
    if (title != null) {
      title.setText(book.getTitle());
    }
  }

  private void setAuthorsAndPublishedDate() {
    if (author != null) {
      author.setText(book.getAuthorsString() + " · " + book.getPublishedDate());
    }
  }

  private void setPublisher() {
    if (publisher != null) {
      publisher.setText(book.getPublisher());
    }
  }

  private void setDescription() {
    if (description != null) {
      description.setText(book.getDescription());
    }
  }

  private void setThumbnail() {
    String thumbnailLink = book.getThumbnailLink();
    if (thumbnailLink.isEmpty()) {
      thumbnail.setImage(ImageUtils.getImage("DefaultBookCover.jpg"));
    } else {
      thumbnail.setImage(new Image(thumbnailLink, 0, 0, true, true, true));
    }
  }

  private void setContainerEffects() {
    DropShadow dropShadow = new DropShadow(50, 0, 0, Color.LIGHTGREEN);
    dropShadow.setSpread(0.2);
    container.setOnMouseEntered(e -> {
      container.setEffect(dropShadow);
    });
    container.setOnMouseExited(e -> {
      container.setEffect(null);
    });
  }

  @FXML
  private void gotoDetails() {
    InAppController inAppController = InAppController.INSTANCE;
    BookDetailsController booksDetailsController = inAppController.setSubscene("BookDetails", "Book details");
    booksDetailsController.setData(book);
  }

}
