package com.application.librarymanagement.user;

<<<<<<< Updated upstream
public class User {
=======
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class User {
  protected static final Path USERS_DB_PATH = Paths.get("json/users.json");
  protected static final Path BOOKS_DB_PATH = Paths.get("json/books.json");
  protected static final Path NOTIFICATIONS_DB_PATH = Paths.get("json/notifications.json");

  protected String name;
  protected String username;
  protected String email;
  protected String password;

  public User(String name, String username, String email, String password) {
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = PasswordUtils.hashPassword(password);
  }

  public abstract boolean saveToDatabase(boolean canOverwrite);

  public void changePassword(String newPassword) {
    JsonArray users = JsonUtils.loadLocalJsonAsArray(USERS_DB_PATH);

    for (var user : users) {
      JsonObject u = user.getAsJsonObject();
      if (u.get("username").getAsString().equals(this.username)) {
        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        u.addProperty("password", hashedPassword);
        this.password = hashedPassword;
        JsonUtils.saveLocalJson(USERS_DB_PATH, users);
        break;
      }
    }
  }

  public void viewAvailableBooks() {
    JsonArray books = JsonUtils.loadLocalJsonAsArray(BOOKS_DB_PATH);
    for (var book : books) {
      System.out.println(book.toString());
    }
  }

  public void viewBookDetail(String bookId) {
    JsonArray books = JsonUtils.loadLocalJsonAsArray(BOOKS_DB_PATH);
    for (var book : books) {
      if (book.getAsJsonObject().get("id").getAsString().equals(bookId)) {
        System.out.println(book.toString());
        return;
      }
    }
    System.out.println("Not found book.");
  }

  public void viewNotifications() {
    JsonArray notifications = JsonUtils.loadLocalJsonAsArray(NOTIFICATIONS_DB_PATH);
    for (var noti : notifications) {
      System.out.println(noti.toString());
    }
  }

  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
>>>>>>> Stashed changes
}
