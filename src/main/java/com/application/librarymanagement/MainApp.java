package com.application.librarymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainApp extends Application {
  private static Stage stage;

  /**
   * Initializes the main application interface.
   * @param primaryStage the primary JavaFX window (Stage)
   * @throws IOException ìf the FXML file cannot be loaded
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;
    stage.setTitle("Library Management System");
    stage.setResizable(false);
    applyStylesheet("dracula");
    setScene("user/Login");
  }

  /**
   * Set/change the scene of the JavaFX application by loading a FXML file
   * from the classpath.
   * @param name the base name of the FXML file (without the {@code .fxml} extension)
   *             e.g. {@code name=Start} will load {@code Login.fxml}
   * @throws IOException ìf the FXML file cannot be loaded
   */
  public static void setScene(String name) throws IOException {
    String path = String.format("scenes/%s.fxml", name);
    Parent root = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource(path)));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Applies a user stylesheet to the JavaFX application by loading a CSS file
   * from the {@code themes} directory on the classpath.
   * @param name the base name of the CSS file (without the {@code .css} extension)
   *             e.g. {@code name=dracula} will load {@code themes/dracula.css}
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
    launch(args);
  }
}