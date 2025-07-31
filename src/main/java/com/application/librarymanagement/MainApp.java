package com.application.librarymanagement;

import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import com.google.gson.JsonObject;

public class MainApp extends Application {
  public static final Path CONFIG_PATH = Paths.get("json/config.json");
  public static final Path BOOKS_DB_PATH = Paths.get("json/books.json");
  public static final Path USERS_DB_PATH = Paths.get("json/users.json");
  public static Stage stage;
  public static JsonObject config;

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
    config = JsonUtils.loadLocalJsonAsObject(CONFIG_PATH);
    assert config != null;
    applyStylesheet(config.get("theme").getAsString());
    if (config.get("currentSession").getAsString().isEmpty()) {
      setScene("SignIn");
    } else {
      setScene("InApp");
    }
  }

  /**
   * Set/change the scene of the JavaFX application by loading an FXML file
   * from the classpath.
   * @param name the base name of the FXML file (without the {@code .fxml} extension)
   *             e.g. {@code name=SignIn} loads {@code scenes/SignIn.fxml}
   */
  public static void setScene(String name) {
    try {
      String path = String.format("scenes/%s.fxml", name);
      Parent root = FXMLLoader.load(Objects.requireNonNull(MainApp.class.getResource(path)));
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Applies a user stylesheet to the JavaFX application by loading a CSS file
   * from the {@code themes} directory on the classpath.
   * @param name the base name of the CSS file (without the {@code .css} extension)
   *             e.g. {@code name=dracula} loads {@code themes/dracula.css}
   */
  public static void applyStylesheet(String name) {
    String path = String.format("themes/%s.css", name);
    URL url = MainApp.class.getResource(path);
    assert url != null;
    MainApp.setUserAgentStylesheet(url.toString());
  }

  /**
   * The main entry point of the JavaFX application.
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}