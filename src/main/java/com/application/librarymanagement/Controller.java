package com.application.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    // these are samples, will delete it later
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    // ...
}