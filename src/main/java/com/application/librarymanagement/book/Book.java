package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.*;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds all properties of a book.
 * <p><a href="https://www.googleapis.com/books/v1/volumes/RSMuEAAAQBAJ">This </a>
 * is an example of what Google Books API returns when querying for a specific book.
 */
public final class Book {
  private static final Map<String, Image> thumbnails = new HashMap<>();
  private static final Image DEFAULT_THUMBNAIL = ImageUtils.getImage("DefaultBookCover.jpg");
  private JsonObject data;

  public static Book fromJsonObject(JsonObject obj) {
    Book book = new Book();
    book.setData(obj);
    return book;
  }

  public static Book findById(String id) {
    for (JsonElement e : MainApp.BOOKS) {
      Book book = Book.fromJsonObject(e.getAsJsonObject());
      if (book.getId().equals(id)) {
        return book;
      }
    }
    return null;
  }

  private JsonObject getVolumeInfo() {
    return data.getAsJsonObject("volumeInfo");
  }

  private JsonObject getSaleInfo() {
    return data.getAsJsonObject("saleInfo");
  }

  private JsonObject getAccessInfo() {
    return data.getAsJsonObject("accessInfo");
  }

  public void setData(JsonObject data) {
    this.data = data;
  }

  public JsonObject getData() {
    return data;
  }

  public int getQuantity() {
    return JsonUtils.getAsInt(data, "quantity", 0);
  }

  public void setQuantity(int quantity) {
    data.addProperty("quantity", quantity);
  }

  public void adjustQuantity(int delta) {
    data.addProperty("quantity", getQuantity() + delta);
  }

  public int getBorrowsCount() {
    return JsonUtils.getAsInt(data, "borrowsCount", 0);
  }

  public void adjustBorrowsCount(int delta) {
    data.addProperty("borrowsCount", getBorrowsCount() + delta);
  }

  public String getId() {
    return JsonUtils.getAsString(data, "id", "");
  }

  public String getTitle() {
    return JsonUtils.getAsString(getVolumeInfo(), "title", "");
  }

  public String getAuthors() {
    return JsonUtils.jsonArrayToString(JsonUtils.getAsJsonArray(getVolumeInfo(), "authors"));
  }

  public String getPublisher() {
    return JsonUtils.getAsString(getVolumeInfo(), "publisher", "");
  }

  public String getPublishedDate() {
    return JsonUtils.getAsString(getVolumeInfo(), "publishedDate", "");
  }

  public String getDescription() {
    return JsonUtils.getAsString(getVolumeInfo(), "description", "").replaceAll("<[^>]*>", " ");
  }

  private JsonArray getIndustryIdentifiers() {
    return JsonUtils.getAsJsonArray(getVolumeInfo(), "industryIdentifiers");
  }

  private String getSpecificIndustryIdentifier(String type) {
    for (JsonElement e : getIndustryIdentifiers()) {
      JsonObject obj = e.getAsJsonObject();
      if (obj.get("type").getAsString().equals(type)) {
        return obj.get("identifier").getAsString();
      }
    }
    return "";
  }

  public String getIsbn10() {
    return getSpecificIndustryIdentifier("ISBN_10");
  }

  public String getIsbn13() {
    return getSpecificIndustryIdentifier("ISBN_13");
  }

  public String getOtherIndustryIdentifiers() {
    return getSpecificIndustryIdentifier("OTHER");
  }

  public String getPageCount() {
    return JsonUtils.getAsString(getVolumeInfo(), "pageCount", "");
  }

  public String getCategories() {
    return JsonUtils.jsonArrayToString(JsonUtils.getAsJsonArray(getVolumeInfo(), "categories"));
  }

  public String getMaturityRating() {
    return JsonUtils.getAsString(getVolumeInfo(), "maturityRating", "");
  }

  public String getAverageRating() {
    return JsonUtils.getAsString(getVolumeInfo(), "averageRating", "");
  }

  public String getRatingsCount() {
    return JsonUtils.getAsString(getVolumeInfo(), "ratingsCount", "");
  }

  public Image getThumbnail() {
    JsonObject o = getVolumeInfo();
    if (o == null) {
      return DEFAULT_THUMBNAIL;
    }
    JsonObject imageLinks = o.getAsJsonObject("imageLinks");
    String thumbnailLink = JsonUtils.getAsString(imageLinks, "thumbnail", "");
    if (thumbnailLink.isEmpty()) {
      return DEFAULT_THUMBNAIL;
    }
    if (thumbnails.containsKey(getId())) {
      return thumbnails.get(getId());
    }
    Image image = new Image(thumbnailLink, 0, 0, true, true, true);
    thumbnails.put(getId(), image);
    return image;
  }

  public String getLanguage() {
    return JsonUtils.getAsString(getVolumeInfo(), "language", "");
  }

  public String getPreviewLink() {
    return JsonUtils.getAsString(getVolumeInfo(), "previewLink", "");
  }

  public String getInfoLink() {
    return JsonUtils.getAsString(getVolumeInfo(), "infoLink", "");
  }

  public String getCountry() {
    return JsonUtils.getAsString(getSaleInfo(), "country", "");
  }

  public String getSaleability() {
    return JsonUtils.getAsString(getSaleInfo(), "saleability", "");
  }

  public String getPrice() {
    JsonObject o = getSaleInfo();
    if (o == null) {
      return "";
    }
    JsonObject price = o.get("listPrice").getAsJsonObject();
    String amount = JsonUtils.getAsString(price, "amount", "");
    String currencyCode = JsonUtils.getAsString(price, "currencyCode", "");
    return amount + " " + currencyCode;
  }

  public String getRetailPrice() {
    JsonObject o = getSaleInfo();
    if (o == null) {
      return "";
    }
    JsonObject price = o.get("retailPrice").getAsJsonObject();
    String amount = JsonUtils.getAsString(price, "amount", "");
    String currencyCode = JsonUtils.getAsString(price, "currencyCode", "");
    return amount + " " + currencyCode;
  }

  public String getBuyLink() {
    return JsonUtils.getAsString(getSaleInfo(), "buyLink", "");
  }

  public void updateToDatabase() {
    try {
      JsonArray books = MainApp.BOOKS;
      boolean updated = false;
      for (int i = 0; i < books.size(); ++i) {
        JsonObject currentBook = books.get(i).getAsJsonObject();
        if (getId().equals(currentBook.get("id").getAsString())) {
          books.set(i, getData());
          updated = true;
          break;
        }
      }
      if (!updated) {
        books.add(getData());
      }
      JsonUtils.saveToFile(books, MainApp.BOOKS_DB_PATH);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public BookStats toBookStats() {
    return new BookStats(this);
  }

  public static List<BookStats> toBookStatsList(List<Book> books) {
    List<BookStats> stats = new ArrayList<>();
    for (Book book : books) {
      stats.add(book.toBookStats());
    }
    for (int i = 0; i < stats.size(); ++i) {
      BookStats s = stats.get(i);
      s.rankProperty().set(i + 1);
    }
    return stats;
  }
}