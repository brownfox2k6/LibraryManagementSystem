package com.application.librarymanagement.user;

import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Admin extends User {
<<<<<<< Updated upstream
=======
  private static final Path BOOKS_DB_PATH = Paths.get("json/books.json");
  private static final Path REQUESTS_DB_PATH = Paths.get("json/requests.json");

  public Admin(String name, String username, String email, String password) {
    super(name, username, email, password);
  }

  @Override
  public boolean saveToDatabase(boolean canOverwrite) {
    JsonArray users = JsonUtils.loadLocalJsonAsArray(USERS_DB_PATH);

    for (int i = 0; i < users.size(); i++) {
      JsonObject u = users.get(i).getAsJsonObject();
      if (u.get("username").getAsString().equals(this.username)) {
        if (canOverwrite) {
          u.addProperty("name", this.name);
          u.addProperty("email", this.email);
          u.addProperty("password", this.password);
          u.addProperty("type", "admin");
          JsonUtils.saveLocalJson(USERS_DB_PATH, users);
          return true;
        } else {
          return false;
        }
      }
    }

    JsonObject newUser = new JsonObject();
    newUser.addProperty("name", this.name);
    newUser.addProperty("username", this.username);
    newUser.addProperty("email", this.email);
    newUser.addProperty("password", this.password);
    newUser.addProperty("type", "admin");

    users.add(newUser);
    JsonUtils.saveLocalJson(USERS_DB_PATH, users);
    return true;
  }

  public void approveBorrowRequests() {
    JsonArray requests = JsonUtils.loadLocalJsonAsArray(REQUESTS_DB_PATH);

    for (var requestElement : requests) {
      JsonObject request = requestElement.getAsJsonObject();
      if (request.get("status").getAsString().equals("pending")) {
        request.addProperty("status", "approved");
        System.out.println("Approved request of: " + request.get("username").getAsString());
      }
    }

    JsonUtils.saveLocalJson(REQUESTS_DB_PATH, requests);
  }

  public void addBook(String id, String title, String author, int year, int quantity) {
    JsonArray books = JsonUtils.loadLocalJsonAsArray(BOOKS_DB_PATH);

    for (var bookElement : books) {
      JsonObject book = bookElement.getAsJsonObject();
      if (book.get("id").getAsString().equals(id)) {
        System.out.println("Book already exists. Skipped.");
        return;
      }
    }

    JsonObject newBook = new JsonObject();
    newBook.addProperty("id", id);
    newBook.addProperty("title", title);
    newBook.addProperty("author", author);
    newBook.addProperty("year", year);
    newBook.addProperty("quantity", quantity);

    books.add(newBook);
    JsonUtils.saveLocalJson(BOOKS_DB_PATH, books);

    System.out.println("Added new book: " + title);
  }
>>>>>>> Stashed changes
}
