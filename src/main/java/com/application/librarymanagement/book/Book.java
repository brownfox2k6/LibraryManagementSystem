package com.application.librarymanagement.book;

import com.application.librarymanagement.utils.JsonFetcher;
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
    this(JsonFetcher.fetchJson("https://www.googleapis.com/books/v1/volumes/" + id));
  }

  /**
   * Constructs a new {@code Book} instance from a pre-fetched JSON object.
   * @param json a {@code JsonObject} representing the full volume data as returned
   *             by the Google Books API
   */
  public Book(JsonObject json) {
    this.id = JsonFetcher.getAsString(json, "id");
    if (json.has("volumeInfo")) {
      JsonObject volumeInfo = json.get("volumeInfo").getAsJsonObject();
      this.title = JsonFetcher.getAsString(volumeInfo, "title");
      this.authors = JsonFetcher.getAsList(volumeInfo, "authors");
      this.publisher = JsonFetcher.getAsString(volumeInfo, "publisher");
      this.publishedDate = JsonFetcher.getAsString(volumeInfo, "publishedDate");
      this.description = JsonFetcher.getAsString(volumeInfo, "description");
      this.industryIdentifiers = JsonFetcher.getAsMap(
          volumeInfo, "industryIdentifiers", "type", "identifier");
      this.pageCount = JsonFetcher.getAsString(volumeInfo, "pageCount");
      this.categories = JsonFetcher.getAsList(volumeInfo, "categories");
      this.averageRating = JsonFetcher.getAsString(volumeInfo, "averageRating");
      this.ratingsCount = JsonFetcher.getAsString(volumeInfo, "ratingsCount");
      if (volumeInfo.has("imageLinks")) {
        JsonObject imageLinks = volumeInfo.get("imageLinks").getAsJsonObject();
        this.thumbnailLink = JsonFetcher.getAsString(imageLinks, "thumbnail");
      }
      this.language = JsonFetcher.getAsString(volumeInfo, "language");
      this.previewLink = JsonFetcher.getAsString(volumeInfo, "previewLink");
      this.infoLink = JsonFetcher.getAsString(volumeInfo, "infoLink");
    }
    if (json.has("saleInfo")) {
      JsonObject saleInfo = json.get("saleInfo").getAsJsonObject();
      this.country = saleInfo.get("country").getAsString();
      this.saleability = saleInfo.get("saleability").getAsString();
      if (saleInfo.has("listPrice")) {
        JsonObject listPrice = saleInfo.get("listPrice").getAsJsonObject();
        String amount = JsonFetcher.getAsString(listPrice, "amount");
        String currencyCode = JsonFetcher.getAsString(listPrice, "currencyCode");
        this.price = amount + " " + currencyCode;
      }
      if (saleInfo.has("retailPrice")) {
        JsonObject retailPrice = saleInfo.get("retailPrice").getAsJsonObject();
        String amount = JsonFetcher.getAsString(retailPrice, "amount");
        String currencyCode = JsonFetcher.getAsString(retailPrice, "currencyCode");
        this.retailPrice = amount + " " + currencyCode;
      }
      this.buyLink = JsonFetcher.getAsString(saleInfo, "buyLink");
    }
  }
}