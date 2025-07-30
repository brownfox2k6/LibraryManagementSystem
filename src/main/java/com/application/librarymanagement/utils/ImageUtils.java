package com.application.librarymanagement.utils;

import com.application.librarymanagement.MainAppController;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.net.URL;

public class ImageUtils {

  /**
   * Utility for loading JavaFX images from the application’s classpath.
   * <p>
   * Given a simple image name (e.g. {@code "icon.png"}), this method will look
   * under the {@code images/} directory on the classpath and return a corresponding
   * {@link javafx.scene.image.Image} instance.
   * <p>
   * Example usage:
   * <pre>{@code
   * Image logo = ImageUtils.getImage("logo.png");
   * }</pre>
   *
   * @param name the filename of the image resource located in {@code images/} (e.g. {@code "logo.png"})
   * @return a JavaFX {@code Image} loaded from {@code images/<name>} on the classpath
   * @throws AssertionError if the resource cannot be found at {@code images/<name>}
   */
  public static Image getImage(String name) {
    String path = String.format("images/%s", name);
    URL url = MainAppController.class.getResource(path);
    assert url != null;
    return new Image(url.toExternalForm());
  }

  public static void invertColor(ImageView imageView) {
    double w = imageView.getBoundsInLocal().getWidth();
    double h = imageView.getBoundsInLocal().getHeight();
    ColorInput whiteOverlay = new ColorInput(0, 0, w, h, Color.WHITE);
    Blend invert = new Blend(BlendMode.DIFFERENCE, whiteOverlay, null);
    imageView.setEffect(invert);
  }
}
