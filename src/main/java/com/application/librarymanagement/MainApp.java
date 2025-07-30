package com.application.librarymanagement;

import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
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
    config = JsonUtils.loadLocalJson("config");
    assert config != null;
    applyStylesheet(config.get("theme").getAsString());
<<<<<<< Updated upstream
    setScene("SignIn");
  }

  /**
   * Updates a property in the configuration and writes the updated configuration
   * back to {@code config.json} in a pretty-printed JSON format.
   * @param name  the property name to update or add
   * @param value the property value to set
   */
  public void addPropertyToConfig(String name, String value) {
    try {
      config.addProperty(name, value);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Path path = Paths.get("json/config.json");
      Files.writeString(path, gson.toJson(config));
    } catch (Exception e) {
      System.err.println(e.getMessage());
=======
    if (config.has("currentSession")) {
      setScene("InApp");
    } else {
      setScene("SignIn");
>>>>>>> Stashed changes
    }
  }

  /**
   * Set/change the scene of the JavaFX application by loading a FXML file
   * from the classpath.
   * @param name the base name of the FXML file (without the {@code .fxml} extension)
   *             e.g. {@code name=SignIn} loads {@code scenes/SignIn.fxml}
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
   *             e.g. {@code name=dracula} loads {@code themes/dracula.css}
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
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}