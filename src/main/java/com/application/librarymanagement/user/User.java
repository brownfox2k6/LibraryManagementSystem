package com.application.librarymanagement.user;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.PasswordUtils;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class User {
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

  protected abstract JsonObject toJsonObject();

  /**
   * Saves the current user object to the local JSON database (users.json).
   * <p>
   * This method first converts the current object to a {@link JsonObject} and loads
   * the existing list of users. It searches for any user with the same username or email:
   * <ul>
   *   <li>If a match is found and {@code canOverwrite} is {@code true}, the existing entry is replaced.</li>
   *   <li>If a match is found and {@code canOverwrite} is {@code false}, the method returns {@code false} and does not save the user.</li>
   *   <li>If no match is found, the user is simply appended to the users list.</li>
   * </ul>
   * After updating, the method writes the updated user list back to {@code USERS_DB_PATH} in pretty-printed JSON format.
   *
   * @param canOverwrite if {@code true}, allows overwriting an existing user with the same username or email (this is used when updating user information);
   *                     if {@code false}, prevents overwriting and cancels the save (this is used when creating a new user).
   * @return {@code true} if the user was saved successfully (added or overwritten),
   *         {@code false} if a duplicate was found and {@code canOverwrite} is {@code false}.
//   * @throws IOException if writing to the file fails.
   */
  public boolean saveToDatabase(boolean canOverwrite) {
    JsonArray users = JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH);
    for (int i = 0; i < users.size(); ++i) {
      JsonObject user = users.get(i).getAsJsonObject();
      if (Objects.equals(JsonUtils.getAsString(user, "username"), this.username) ||
          Objects.equals(JsonUtils.getAsString(user, "email"), this.email)) {
        if (canOverwrite) {
          users.remove(user);
          break;
        }
        return false;
      }
    }
    users.add(this.toJsonObject());
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Files.writeString(MainApp.USERS_DB_PATH, gson.toJson(users));
    } catch (Exception ignored) {}
    return true;
  }
}
