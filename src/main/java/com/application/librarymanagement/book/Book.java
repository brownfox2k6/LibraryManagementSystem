package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.*;

import java.io.IOException;

/**
 * Holds all properties of a book.
 * <a href="https://www.googleapis.com/books/v1/volumes/RSMuEAAAQBAJ">
 * This is an example of what Google Books API returns when querying for a specific book.</a>"
 */
public class Book {
  private JsonObject data;

  public static Book fromGoogleBooksApi(String id) throws IOException {
    String urlString = "https://www.googleapis.com/books/v1/volumes/" + id;
    Book book = new Book();
    book.setData(JsonUtils.fetchJson(urlString));
    return book;
  }

  public static Book fromJsonObject(JsonObject obj) {
    Book book = new Book();
    book.setData(obj);
    return book;
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

  public void adjustQuantity(int amount) {
    data.addProperty("quantity", getQuantity() + amount);
  }

  public String getId() {
    return JsonUtils.getAsString(data, "id", "");
  }

  public String getTitle() {
    return JsonUtils.getAsString(getVolumeInfo(), "title", "");
  }

  public JsonArray getAuthorsArray() {
    return JsonUtils.getAsJsonArray(getVolumeInfo(), "authors");
  }

  public String getAuthorsString() {
    return JsonUtils.jsonArrayToString(getAuthorsArray());
  }

  public String getPublisher() {
    return JsonUtils.getAsString(getVolumeInfo(), "publisher", "");
  }

  public String getPublishedDate() {
    return JsonUtils.getAsString(getVolumeInfo(), "publishedDate", "");
  }

  public String getDescription() {
    return JsonUtils.getAsString(getVolumeInfo(), "description", "");
  }

  public JsonArray getIndustryIdentifiers() {
    return JsonUtils.getAsJsonArray(getVolumeInfo(), "industryIdentifiers");
  }

  public int getPageCount() {
    return JsonUtils.getAsInt(getVolumeInfo(), "pageCount", 0);
  }

  public JsonArray getCategoriesAsArray() {
    return JsonUtils.getAsJsonArray(getVolumeInfo(), "categories");
  }

  public String getCategoriesAsString() {
    return JsonUtils.jsonArrayToString(getCategoriesAsArray());
  }

  public double getAverageRating() {
    return JsonUtils.getAsDouble(getVolumeInfo(), "averageRating", 0);
  }

  public double getRatingsCount() {
    return JsonUtils.getAsDouble(getVolumeInfo(), "ratingsCount", 0);
  }

  public String getThumbnailLink() {
    JsonObject imageLinks = getVolumeInfo().getAsJsonObject("imageLinks");
    return JsonUtils.getAsString(imageLinks, "thumbnail", "");
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
    JsonObject price = getSaleInfo().get("listPrice").getAsJsonObject();
    String amount = JsonUtils.getAsString(price, "amount", "");
    String currencyCode = JsonUtils.getAsString(price, "currencyCode", "");
    return amount + " " + currencyCode;
  }

  public String getRetailPrice() {
    JsonObject price = getSaleInfo().get("retailPrice").getAsJsonObject();
    String amount = JsonUtils.getAsString(price, "amount", "");
    String currencyCode = JsonUtils.getAsString(price, "currencyCode", "");
    return amount + " " + currencyCode;
  }

  public String getBuyLink() {
    return JsonUtils.getAsString(getSaleInfo(), "buyLink", "");
  }

  public void updateToDatabase() throws IOException {
    JsonArray books = JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH);
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
  }
}