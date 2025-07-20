module com.application.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
  requires com.google.gson;


  opens com.application.librarymanagement to javafx.fxml;
    exports com.application.librarymanagement;
}