package com.application.librarymanagement.user;

<<<<<<< Updated upstream
public class Member extends User {
=======
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.borrow.Borrow;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Member extends User {
  private List<Borrow> borrows;

  public Member(String name, String username, String email, String password) {
    super(name, username, email, password);
    this.borrows = new ArrayList<>();
  }

  // Đăng ký tài khoản
  @Override
  public boolean saveToDatabase(boolean canOverwrite) {
    // Đọc dữ liệu từ file users.json
    JsonObject root = JsonUtils.loadLocalJsonAsObject(Paths.get("data/users.json"));
    JsonArray users = root.getAsJsonArray("users");

    for (var u : users) {
      JsonObject obj = u.getAsJsonObject();
      String uname = obj.get("username").getAsString();
      if (uname.equals(this.username)) {
        if (!canOverwrite) return false;
        obj.addProperty("name", this.name);
        obj.addProperty("email", this.email);
        obj.addProperty("password", this.password);
        obj.addProperty("type", "member");
        JsonUtils.saveLocalJson(Paths.get("data/users.json"), users);
        return true;
      }
    }

    JsonObject newUser = new JsonObject();
    newUser.addProperty("name", name);
    newUser.addProperty("username", username);
    newUser.addProperty("email", email);
    newUser.addProperty("password", password);
    newUser.addProperty("type", "member");

    users.add(newUser);
    JsonUtils.saveLocalJson(Paths.get("data/users.json"), users);
    return true;
  }

  public List<Book> searchBooks(String keyword) {
    List<Book> results = new ArrayList<>();
    JsonArray books = JsonUtils.loadLocalJsonAsArray(Paths.get("data/books.json"));

    for (var b : books) {
      JsonObject obj = b.getAsJsonObject();
      if (obj.has("title")) {
        String title = obj.get("title").getAsString().toLowerCase();
        if (title.contains(keyword.toLowerCase())) {
          Book book = Book.fromJson(obj);
          results.add(book);
        }
      }
    }
    return results;
  }

  // Yêu cầu mượn sách
  public boolean requestBorrow(String bookId) {
    JsonArray books = JsonUtils.loadLocalJsonAsArray(Paths.get("data/books.json"));
    boolean found = false;

    for (var book : books) {
      JsonObject b = book.getAsJsonObject();
      if (b.has("id") && b.get("id").getAsString().equals(bookId)) {
        found = true;

        Borrow borrow = new Borrow(this.username, bookId);
        this.borrows.add(borrow);

        JsonObject borrowRoot = JsonUtils.loadLocalJsonAsObject(Paths.get("data/borrows.json"));
        JsonArray borrowList = borrowRoot.getAsJsonArray("borrows");
        borrowList.add(borrow.toJson());
        JsonUtils.saveLocalJson(Paths.get("data/borrows.json"), borrowList);
        break;
      }
    }

    return found;
  }

  public List<Borrow> getBorrows() {
    return borrows;
  }
>>>>>>> Stashed changes
}
