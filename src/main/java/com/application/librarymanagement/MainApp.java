package com.application.librarymanagement;

import com.application.librarymanagement.utils.JsonUtils;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import com.google.gson.JsonObject;
import javafx.util.Duration;

public class MainApp extends Application {
  public static final Path CONFIG_PATH = Paths.get("json/config.json");
  public static final Path BOOKS_DB_PATH = Paths.get("json/books.json");
  public static final Path BORROWS_DB_PATH = Paths.get("json/borrows.json");
  public static final Path USERS_DB_PATH = Paths.get("json/users.json");
  public static Stage stage;
  public static JsonObject config;
  private static Node currentToast;
  private static Animation currentSlide;
  private static PauseTransition currentWait;

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
    applyStylesheet(JsonUtils.getAsString(config, "theme", ""));
    if (JsonUtils.getAsString(config, "currentSession", "").isEmpty()) {
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

  public static String getLightOrDark() {
    String theme = config.get("theme").getAsString();
    return theme.contains("light") ? "Light" : "Dark";
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

  public static void showPopupMessage(String message, Color bg) {
    Parent root = stage.getScene().getRoot();
    if (!(root instanceof StackPane)) {
      StackPane wrapper = new StackPane(root);
      stage.getScene().setRoot(wrapper);
      root = wrapper;
    }
    StackPane stack = (StackPane) root;
    if (currentSlide != null) {
      currentSlide.stop();
    }
    if (currentWait != null) {
      currentWait.stop();
    }
    if (currentToast != null) {
      stack.getChildren().remove(currentToast);
      currentToast = null;
    }
    Label label = new Label(message);
    label.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
    label.setWrapText(true);
    StackPane toast = new StackPane(label);
    toast.setBackground(new Background(new BackgroundFill(
        new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 0.75),
        new CornerRadii(8, 8, 0, 0, false), Insets.EMPTY)));
    toast.setPadding(new Insets(8));
    toast.setMaxHeight(Region.USE_PREF_SIZE);
    toast.setMinWidth(1280);
    StackPane.setAlignment(toast, Pos.BOTTOM_CENTER);
    toast.setTranslateY(toast.prefHeight(-1));
    stack.getChildren().add(toast);
    currentToast = toast;
    TranslateTransition slideIn = new TranslateTransition(Duration.millis(180), toast);
    slideIn.setFromY(toast.prefHeight(-1));
    slideIn.setToY(0);
    currentSlide = slideIn;
    slideIn.play();
    PauseTransition wait = new PauseTransition(Duration.seconds(3));
    currentWait = wait;
    wait.setOnFinished(e -> {
      if (currentToast == null) {
        return;
      }
      Node t = currentToast;
      TranslateTransition slideOut = new TranslateTransition(Duration.millis(250), toast);
      slideOut.setFromY(0);
      slideOut.setToY(toast.getHeight());
      currentSlide = slideOut;
      slideOut.setOnFinished(ev -> {
        stack.getChildren().remove(t);
        if (currentToast == t) {
          currentToast = null;
        }
        currentSlide = null;
        currentWait = null;
      });
      slideOut.play();
    });
    wait.play();
  }

  /**
   * The main entry point of the JavaFX application.
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}