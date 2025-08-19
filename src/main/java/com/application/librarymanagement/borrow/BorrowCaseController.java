package com.application.librarymanagement.borrow;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.InAppController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public final class BorrowCaseController {
  @FXML private ImageView thumbnail;
  @FXML private Label title;
  @FXML private Label borrowId;
  @FXML private Label bookId;
  @FXML private Label username;
  @FXML private Label status;
  @FXML private Label requestedTime;
  @FXML private Label borrowedTime;
  @FXML private Label returnedTime;
  @FXML private Label canceledTime;
  @FXML private Button markBorrowedButton;
  @FXML private Button markReturnedButton;
  @FXML private Button cancelBorrowButton;

  private Borrow borrow;
  private Book book;

  public void setData(Borrow borrow) {
    this.borrow = borrow;
    book = Book.findById(borrow.getBookId());
    assert book != null;
    setThumbnail();
    title.setText(book.getTitle());
    borrowId.setText(borrow.getBorrowId() + "");
    bookId.setText(book.getId());
    username.setText(borrow.getUsername());
    status.setText(borrow.getStatusText());
    requestedTime.setText(borrow.getRequestedTime());
    borrowedTime.setText(borrow.getBorrowedTime());
    returnedTime.setText(borrow.getReturnedTime());
    canceledTime.setText(borrow.getCanceledTime());
    initializeButtons();
  }

  private void initializeButtons() {
    if (InAppController.CURRENT_USER.isAdmin()) {
      if (borrow.getStatus() != Borrow.STATUS_REQUESTED) {
        makeNodeDisappear(cancelBorrowButton);
        makeNodeDisappear(markBorrowedButton);
      }
      if (borrow.getStatus() != Borrow.STATUS_BORROWED) {
        makeNodeDisappear(markReturnedButton);
      }
    } else {
      makeNodeDisappear(markBorrowedButton);
      makeNodeDisappear(markReturnedButton);
      if (borrow.getStatus() != Borrow.STATUS_REQUESTED) {
        makeNodeDisappear(cancelBorrowButton);
      }
    }
  }

  private void setThumbnail() {
    thumbnail.setImage(book.getThumbnail());
  }

  private void makeNodeDisappear(Node node) {
    node.setVisible(false);
    node.setManaged(false);
  }

  private void makeNodeAppear(Node node) {
    node.setManaged(true);
    node.setVisible(true);
  }

  @FXML
  private void markCanceled() {
    borrow.setCanceled();
    canceledTime.setText(borrow.getCanceledTime());
    status.setText("CANCELED");
    MainApp.showPopupMessage("Marked as CANCELED", Color.DARKGREEN);
    makeNodeDisappear(markBorrowedButton);
    makeNodeDisappear(markReturnedButton);
    makeNodeDisappear(cancelBorrowButton);
  }

  @FXML
  private void markBorrowed() {
    borrow.setBorrowed();
    borrowedTime.setText(borrow.getBorrowedTime());
    status.setText("BORROWED");
    MainApp.showPopupMessage("Marked as BORROWED", Color.DARKGREEN);
    makeNodeDisappear(markBorrowedButton);
    makeNodeDisappear(cancelBorrowButton);
    makeNodeAppear(markReturnedButton);
  }

  @FXML
  private void markReturned() {
    borrow.setReturned();
    status.setText("RETURNED");
    MainApp.showPopupMessage("Marked as RETURNED", Color.DARKGREEN);
    returnedTime.setText(borrow.getReturnedTime());
    makeNodeDisappear(markReturnedButton);
    makeNodeDisappear(cancelBorrowButton);
  }
}
