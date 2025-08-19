package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class User {
  public static final int TYPE_ADMIN = 1;
  public static final int TYPE_MEMBER = 2;
  private final JsonObject data;

  public User(JsonObject data) {
    this.data = data;
  }

  public User(String name, String username, String email, String password, int type) {
    data = new JsonObject();
    setName(name);
    setUsername(username);
    setEmail(email);
    setPassword(password);
    data.addProperty("type", type);
    if (type == TYPE_MEMBER) {
      data.add("borrows", new JsonArray());
    }
  }

  public JsonObject getData() {
    return data;
  }

  public String getUsername() {
    return JsonUtils.getAsString(data, "username", "");
  }

  public void setUsername(String username) {
    data.addProperty("username", username);
  }

  public String getName() {
    return JsonUtils.getAsString(data, "name", "");
  }

  public void setName(String name) {
    data.addProperty("name", name);
  }

  public String getEmail() {
    return JsonUtils.getAsString(data, "email", "");
  }

  public void setEmail(String email) {
    data.addProperty("email", email);
  }

  public String getHashedPassword() {
    return JsonUtils.getAsString(data, "password", "");
  }

  public void setPassword(String password) {
    String hashedPassword = PasswordUtils.hashPassword(password);
    data.addProperty("password", hashedPassword);
  }

  public boolean isAdmin() {
    return JsonUtils.getAsInt(data, "type", 0) == TYPE_ADMIN;
  }

  public boolean isMember() {
    return JsonUtils.getAsInt(data, "type", 0) == TYPE_MEMBER;
  }

  public JsonArray getBorrows() {
    assert isMember();
    return JsonUtils.getAsJsonArray(data, "borrows");
  }

  public void addBorrowId(int id) {
    assert isMember();
    JsonArray borrows = getBorrows();
    borrows.add(id);
    saveToDatabase();
  }

  public void saveToDatabase() {
    JsonArray users = MainApp.USERS;
    for (int i = 0; i < users.size(); ++i) {
      JsonObject user = users.get(i).getAsJsonObject();
      if (JsonUtils.getAsString(user, "username", "").equals(getUsername())) {
        users.set(i, data);
        JsonUtils.saveToFile(users, MainApp.USERS_DB_PATH);
        return;
      }
    }
    users.add(data);
    JsonUtils.saveToFile(users, MainApp.USERS_DB_PATH);
  }
}
