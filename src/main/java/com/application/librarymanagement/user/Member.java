package com.application.librarymanagement.user;

import com.application.librarymanagement.borrow.Borrow;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Member extends User {
  protected List<Borrow> borrows;

  public Member(String name, String username, String email, String password) {
    super(name, username, email, password);
    borrows = new ArrayList<>();
  }

  private JsonObject toJsonObject() {
    JsonObject obj = new JsonObject();
    obj.addProperty("name", this.name);
    obj.addProperty("username", this.username);
    obj.addProperty("email", this.email);
    obj.addProperty("password", this.password);
    obj.addProperty("type", "member");
    JsonArray array = new JsonArray();
    for (Borrow borrow : borrows) {
      // TODO: implement Borrow class
      // array.add(borrow.toJsonObject());
      array.add(new JsonObject());
    }
    obj.add("borrows", array);
    return obj;
  }

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
   * After updating, the method writes the updated user list back to {@code json/users.json} in pretty-printed JSON format.
   *
   * @param canOverwrite if {@code true}, allows overwriting an existing user with the same username or email;
   *                     if {@code false}, prevents overwriting and cancels the save.
   * @return {@code true} if the user was saved successfully (added or overwritten),
   *         {@code false} if a duplicate was found and {@code canOverwrite} is {@code false}.
   * @throws RuntimeException if writing to the file fails due to an {@link IOException}.
   */
  @Override
  public boolean saveToDatabase(boolean canOverwrite) {
    JsonObject obj = toJsonObject();
    JsonArray users = JsonUtils.loadLocalJsonAsArray(USERS_DB_PATH);
    for (JsonElement user : users) {
      JsonObject userObj = user.getAsJsonObject();
      if (Objects.equals(JsonUtils.getAsString(userObj, "username"), this.username) ||
             Objects.equals(JsonUtils.getAsString(userObj, "email"), this.email)) {
        if (canOverwrite) {
          users.remove(user);
          break;
        }
        return false;
      }
    }
    users.add(obj);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Path path = Paths.get("json/users.json");
    try {
      Files.writeString(path, gson.toJson(users));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
}
