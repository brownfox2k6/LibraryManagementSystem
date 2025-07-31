package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
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
  JsonArray authors;
  String publisher;
  String publishedDate;
  String description;
  JsonArray industryIdentifiers;
  int pageCount;
  JsonArray categories;
  double averageRating;
  int ratingsCount;
  String thumbnailLink;
  String language;
  String previewLink;
  String infoLink;
  String country;
  String saleability;
  String price;
  String retailPrice;
  String buyLink;
  int quantity = 0;
  int borrowsCount = 0;

  public Book() {}

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
      this.title = volumeInfo.get("title").getAsString();
      this.authors = volumeInfo.get("authors").getAsJsonArray();
      this.publisher = volumeInfo.get("publisher").getAsString();
      this.publishedDate = volumeInfo.get("publishedDate").getAsString();
      this.description = volumeInfo.get("description").getAsString();
      this.industryIdentifiers = volumeInfo.get("industryIdentifiers").getAsJsonArray();
      this.pageCount = volumeInfo.get("pageCount").getAsInt();
      this.categories = volumeInfo.get("categories").getAsJsonArray();
      this.averageRating = volumeInfo.has("averageRating")? volumeInfo.get("averageRating").getAsDouble() : 0;
      this.ratingsCount = volumeInfo.has("ratingsCount")? volumeInfo.get("ratingsCount").getAsInt() : 0;
      if (volumeInfo.has("imageLinks")) {
        JsonObject imageLinks = volumeInfo.get("imageLinks").getAsJsonObject();
        this.thumbnailLink = imageLinks.get("thumbnail").getAsString();
      }
      this.language = volumeInfo.get("language").getAsString();
      this.previewLink = volumeInfo.get("previewLink").getAsString();
      this.infoLink = volumeInfo.get("infoLink").getAsString();
    }
    if (json.has("saleInfo")) {
      JsonObject saleInfo = json.get("saleInfo").getAsJsonObject();
      this.country = saleInfo.get("country").getAsString();
      this.saleability = saleInfo.get("saleability").getAsString();
      if (saleInfo.has("listPrice")) {
        JsonObject listPrice = saleInfo.get("listPrice").getAsJsonObject();
        String amount = listPrice.get("amount").getAsString();
        String currencyCode = listPrice.get("currencyCode").getAsString();
        this.price = amount + " " + currencyCode;
      }
      if (saleInfo.has("retailPrice")) {
        JsonObject retailPrice = saleInfo.get("retailPrice").getAsJsonObject();
        String amount = retailPrice.get("amount").getAsString();
        String currencyCode = retailPrice.get("currencyCode").getAsString();
        this.retailPrice = amount + " " + currencyCode;
      }
      this.buyLink = saleInfo.get("buyLink").getAsString();
    }
  }

  private JsonObject toJsonObject() {
    JsonObject book = new JsonObject();
    book.addProperty("id", id);
    book.addProperty("title", title);
    book.add("authors", authors);
    book.addProperty("publisher", publisher);
    book.addProperty("publishedDate", publishedDate);
    book.addProperty("description", description);
    book.add("industryIdentifiers", industryIdentifiers);
    book.addProperty("pageCount", pageCount);
    book.add("categories", categories);
    book.addProperty("averageRating", averageRating);
    book.addProperty("ratingsCount", ratingsCount);
    book.addProperty("thumbnailLink", thumbnailLink);
    book.addProperty("language", language);
    book.addProperty("previewLink", previewLink);
    book.addProperty("infoLink", infoLink);
    book.addProperty("country", country);
    book.addProperty("saleability", saleability);
    book.addProperty("price", price);
    book.addProperty("retailPrice", retailPrice);
    book.addProperty("buyLink", buyLink);
    book.addProperty("quantity", quantity);
    book.addProperty("borrowsCount", borrowsCount);
    return book;
  }

  public static Book fromLocalJsonObject(JsonObject obj) {
    Book book = new Book();
    book.id = obj.get("id").getAsString();
    book.title = obj.get("title").getAsString();
    book.authors = obj.get("authors").getAsJsonArray();
    book.publisher = obj.get("publisher").getAsString();
    book.publishedDate = obj.get("publishedDate").getAsString();
    book.description = obj.get("description").getAsString();
    book.industryIdentifiers = obj.get("industryIdentifiers").getAsJsonArray();
    book.pageCount = obj.get("pageCount").getAsInt();
    book.categories = obj.get("categories").getAsJsonArray();
    book.averageRating = obj.get("averageRating").getAsDouble();
    book.ratingsCount = obj.get("ratingsCount").getAsInt();
    book.thumbnailLink = obj.get("thumbnailLink").getAsString();
    book.language = obj.get("language").getAsString();
    book.previewLink = obj.get("previewLink").getAsString();
    book.infoLink = obj.get("infoLink").getAsString();
    book.country = obj.get("country").getAsString();
    book.saleability = obj.get("saleability").getAsString();
    book.price = obj.get("price").getAsString();
    book.retailPrice = obj.get("retailPrice").getAsString();
    book.buyLink = obj.get("buyLink").getAsString();
    book.quantity = obj.get("quantity").getAsInt();
    book.borrowsCount = obj.get("borrowsCount").getAsInt();
    return book;
  }

  public String getAuthorsString() {
    StringBuilder ret = new StringBuilder();
    for (int i = 0; i < authors.size(); ++i) {
      ret.append(authors.get(i).getAsString());
      if (i == authors.size() - 1) {
        ret.append(", ");
      }
    }
    return ret.toString();
  }

  public void addToDatabase(int delta) throws IOException {
    quantity += delta;
    JsonArray books = JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH);
    boolean updated = false;
    for (JsonElement e : books) {
      JsonObject currentBook = e.getAsJsonObject();
      if (id.equals(currentBook.get("id").getAsString())) {
        currentBook.addProperty("quantity", quantity);
        updated = true;
        break;
      }
    }
    if (!updated) {
      books.add(toJsonObject());
    }
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Files.writeString(MainApp.BOOKS_DB_PATH, gson.toJson(books));
  }
}