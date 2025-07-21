package com.application.librarymanagement.book;

import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;;

/**
 * Holds all properties of a book.
 * <a href="https://www.googleapis.com/books/v1/volumes/RSMuEAAAQBAJ">
 * This is an example of what Google Books API returns when querying for a specific book.</a>"
 */
public class Book {
  String id;
  String title;
  List<String> authors;
  String publisher;
  String publishedDate;
  String description;
  Map<String, String> industryIdentifiers;
  String pageCount;
  List<String> categories;
  String averageRating;
  String ratingsCount;
  String thumbnailLink;
  String language;
  String previewLink;
  String infoLink;
  String country;
  String saleability;
  String price;
  String retailPrice;
  String buyLink;

  /**
   * Constructs a new {@code Book} instance by loading its metadata from the Google Books API.
   * @param id the Google Books volume ID (e.g. "RSMuEAAAQBAJ")
   * @throws IOException if a network or I/O error occurs during the HTTP request
   */
  public Book(String id) throws IOException {
    this(JsonUtils.fetchJson("https://www.googleapis.com/books/v1/volumes/" + id));
  }

  /**
   * Constructs a new {@code Book} instance from a pre-fetched JSON object.
   * @param json a {@code JsonObject} representing the full volume data as returned
   *             by the Google Books API
   */
  public Book(JsonObject json) {
    this.id = JsonUtils.getAsString(json, "id");
    if (json.has("volumeInfo")) {
      JsonObject volumeInfo = json.get("volumeInfo").getAsJsonObject();
      this.title = JsonUtils.getAsString(volumeInfo, "title");
      this.authors = JsonUtils.getAsList(volumeInfo, "authors");
      this.publisher = JsonUtils.getAsString(volumeInfo, "publisher");
      this.publishedDate = JsonUtils.getAsString(volumeInfo, "publishedDate");
      this.description = JsonUtils.getAsString(volumeInfo, "description");
      this.industryIdentifiers = JsonUtils.getAsMap(
          volumeInfo, "industryIdentifiers", "type", "identifier");
      this.pageCount = JsonUtils.getAsString(volumeInfo, "pageCount");
      this.categories = JsonUtils.getAsList(volumeInfo, "categories");
      this.averageRating = JsonUtils.getAsString(volumeInfo, "averageRating");
      this.ratingsCount = JsonUtils.getAsString(volumeInfo, "ratingsCount");
      if (volumeInfo.has("imageLinks")) {
        JsonObject imageLinks = volumeInfo.get("imageLinks").getAsJsonObject();
        this.thumbnailLink = JsonUtils.getAsString(imageLinks, "thumbnail");
      }
      this.language = JsonUtils.getAsString(volumeInfo, "language");
      this.previewLink = JsonUtils.getAsString(volumeInfo, "previewLink");
      this.infoLink = JsonUtils.getAsString(volumeInfo, "infoLink");
    }
    if (json.has("saleInfo")) {
      JsonObject saleInfo = json.get("saleInfo").getAsJsonObject();
      this.country = saleInfo.get("country").getAsString();
      this.saleability = saleInfo.get("saleability").getAsString();
      if (saleInfo.has("listPrice")) {
        JsonObject listPrice = saleInfo.get("listPrice").getAsJsonObject();
        String amount = JsonUtils.getAsString(listPrice, "amount");
        String currencyCode = JsonUtils.getAsString(listPrice, "currencyCode");
        this.price = amount + " " + currencyCode;
      }
      if (saleInfo.has("retailPrice")) {
        JsonObject retailPrice = saleInfo.get("retailPrice").getAsJsonObject();
        String amount = JsonUtils.getAsString(retailPrice, "amount");
        String currencyCode = JsonUtils.getAsString(retailPrice, "currencyCode");
        this.retailPrice = amount + " " + currencyCode;
      }
      this.buyLink = JsonUtils.getAsString(saleInfo, "buyLink");
    }
  }
}