package com.application.librarymanagement.book;

import com.application.librarymanagement.utils.ImageUtils;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public final class Thumbnail {
  private static final Map<String, Image> thumbnails = new HashMap<>();
  private static final Image defaultThumbnail = ImageUtils.getImage("DefaultBookCover.jpg");

  public static Image getThumbnail(Book book) {
    String id = book.getId();
    String thumbnailLink = book.getThumbnailLink();
    if (thumbnailLink.isEmpty()) {
      return defaultThumbnail;
    }
    if (thumbnails.containsKey(id)) {
      return thumbnails.get(id);
    }
    Image image = new Image(thumbnailLink, 0, 0, true, true, true);
    thumbnails.put(id, image);
    return image;
  }
}
