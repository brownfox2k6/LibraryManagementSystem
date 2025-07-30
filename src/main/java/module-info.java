module com.application.librarymanagement {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.google.gson;

  opens com.application.librarymanagement to javafx.fxml;
  opens com.application.librarymanagement.user to javafx.fxml;
  opens com.application.librarymanagement.images to javafx.fxml;
  opens com.application.librarymanagement.inapp to javafx.fxml;

  exports com.application.librarymanagement;
}