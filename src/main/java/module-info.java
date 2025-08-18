module com.application.librarymanagement {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.google.gson;
  requires jdk.compiler;
  requires com.google.zxing;
  requires java.desktop;
  requires java.sql;
  requires jcommander;

  opens com.application.librarymanagement to javafx.fxml;
  opens com.application.librarymanagement.scenes to javafx.fxml;
  opens com.application.librarymanagement.user to javafx.fxml;
  opens com.application.librarymanagement.images to javafx.fxml;
  opens com.application.librarymanagement.inapp to javafx.fxml;
  opens com.application.librarymanagement.book to javafx.fxml;
  opens com.application.librarymanagement.borrow to javafx.fxml;

  exports com.application.librarymanagement;
}