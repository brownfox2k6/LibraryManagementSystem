package com.application.librarymanagement;

import com.application.librarymanagement.utils.JsonUtils;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
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
  public static Popup currentPopup;

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

  public static void showPopupMessage(String message, Color backgroundColor) {
    if (currentPopup != null && currentPopup.isShowing()) {
      currentPopup.hide();
    }
    backgroundColor = new Color(backgroundColor.getRed(),
        backgroundColor.getGreen(), backgroundColor.getBlue(), 0.75);
    Label label = new Label(message);
    label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
    StackPane pane = new StackPane(label);
    pane.setBackground(new Background(new BackgroundFill(
        backgroundColor, new CornerRadii(5), Insets.EMPTY)));
    pane.setPadding(new Insets(10));
    pane.setOpacity(0.9);
    currentPopup = new Popup();
    currentPopup.getContent().add(pane);
    currentPopup.setAutoFix(true);
    currentPopup.setAutoHide(true);
    currentPopup.setHideOnEscape(true);
    Parent root = stage.getScene().getRoot();
    Bounds rootBounds = root.localToScreen(root.getBoundsInLocal());
    pane.applyCss();
    pane.layout();
    double bottomMargin = 8;
    double centerX = rootBounds.getMinX()
        + (rootBounds.getWidth() - pane.prefWidth(-1)) / 2.0;
    double targetY = rootBounds.getMaxY()
        - pane.prefHeight(-1) - bottomMargin;
    currentPopup.show(stage.getScene().getWindow(), centerX, targetY);
    double slideOffset = 16;
    pane.setTranslateY(slideOffset);
    TranslateTransition slideUp = new TranslateTransition(Duration.millis(220), pane);
    slideUp.setFromY(slideOffset);
    slideUp.setToY(0);
    slideUp.play();
    PauseTransition wait = new PauseTransition(Duration.seconds(5));
    wait.setOnFinished(e -> currentPopup.hide());
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