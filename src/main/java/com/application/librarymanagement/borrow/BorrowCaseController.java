package com.application.librarymanagement.borrow;

import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class BorrowCaseController {
  @FXML private ImageView thumbnail;
  @FXML private Label titleLabel;
  @FXML private Label borrowIdLabel;
  @FXML private Label usernameLabel;
  @FXML private Label statusLabel;
  @FXML private Label requestedTimeLabel;
  @FXML private Label borrowedTimeLabel;
  @FXML private Label returnedTimeLabel;
  @FXML private Label canceledTimeLabel;
  @FXML private Button cancelBorrowButton;
  @FXML private Button markBorrowedButton;
  @FXML private Button markReturnedButton;

  private Borrow borrow;
  private Book book;

  public void setData(Borrow b) {
    borrow = b;
    book = Book.findById(borrow.getBookId());
    assert book != null;
    setThumbnail();
    titleLabel.setText(book.getTitle());
    borrowIdLabel.setText(borrow.getBorrowId() + "");
    usernameLabel.setText(borrow.getUsername());
    statusLabel.setText(switch (borrow.getStatus()) {
      case Borrow.STATUS_REQUESTED -> "REQUESTED";
      case Borrow.STATUS_RETURNED -> "RETURNED";
      case Borrow.STATUS_CANCELED -> "CANCELED";
      case Borrow.STATUS_BORROWED -> "BORROWED";
      default -> throw new IllegalStateException("Unexpected value: " + borrow.getStatus());
    });
    requestedTimeLabel.setText(borrow.getRequestedTime());
    borrowedTimeLabel.setText(borrow.getBorrowedTime());
    returnedTimeLabel.setText(borrow.getReturnedTime());
    canceledTimeLabel.setText(borrow.getCanceledTime());
    initializeButtons();
  }

  private void initializeButtons() {
    if (InAppController.CURRENT_USER.getUserType() == User.TYPE_ADMIN) {
      makeNodeDisappear(cancelBorrowButton);
      if (borrow.getStatus() != Borrow.STATUS_REQUESTED) {
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
    String thumbnailLink = book.getThumbnailLink();
    if (thumbnailLink.isEmpty()) {
      thumbnail.setImage(ImageUtils.getImage("DefaultBookCover.jpg"));
    } else {
      thumbnail.setImage(new Image(thumbnailLink, 0, 0, true, true, true));
    }
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
  private void cancelBorrow() {
    borrow.setCanceled();
    borrow.saveToDatabase();
    canceledTimeLabel.setText(borrow.getCanceledTime());
    makeNodeDisappear(cancelBorrowButton);
  }

  @FXML
  public void markBorrowed() {
    borrow.setBorrowed();
    borrow.saveToDatabase();
    borrowedTimeLabel.setText(borrow.getBorrowedTime());
    makeNodeDisappear(markBorrowedButton);
    makeNodeAppear(markReturnedButton);
  }

  @FXML
  public void markReturned() {
    borrow.setReturned();
    borrow.saveToDatabase();
    returnedTimeLabel.setText(borrow.getReturnedTime());
    makeNodeDisappear(markReturnedButton);
  }
}
