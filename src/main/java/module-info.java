module com.application.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.application.librarymanagement to javafx.fxml;
    exports com.application.librarymanagement;
}