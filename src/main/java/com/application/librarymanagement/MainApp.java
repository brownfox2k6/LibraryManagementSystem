package com.application.librarymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {
  /**
   * Initializes the main application interface.
   * @param primaryStage the primary JavaFX window (Stage)
   * @throws IOException ìf the FXML file cannot be loaded
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("Start.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
    applyStylesheet("dracula");
    primaryStage.setTitle("Library Management System");
    primaryStage.setResizable(false);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Applies a user stylesheet to the JavaFX application by loading a CSS file
   * from the `themes` directory on the classpath.
   * @param name the base name of the CSS file (without the .css extension)
   *             e.g. "dracula" will load `themes/dracula.css`
   */
  public static void applyStylesheet(String name) {
    String path = String.format("themes/%s.css", name);
    URL url = MainApp.class.getResource(path);
    if (url != null) {
      MainApp.setUserAgentStylesheet(url.toString());
    }
  }

  /**
   * The main entry point of the JavaFX application.
   * @param args the command-line arguments (not used)
   */
  public static void main(String[] args) {
    launch();
  }
}