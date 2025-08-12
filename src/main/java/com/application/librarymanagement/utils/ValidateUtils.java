package com.application.librarymanagement.utils;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.user.User;
import com.google.gson.JsonElement;

public final class ValidateUtils {
  public static boolean isValidEmail(String email) {
    return email.matches("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
  }

  public static boolean isValidUsername(String username) {
    return username.matches("^[a-zA-Z0-9._-]{3,20}$");
  }

  public static boolean isEmailExists(String email) {
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH)) {
      User user = new User(e.getAsJsonObject());
      if (user.getEmail().equals(email)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isUsernameExists(String username) {
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH)) {
      User user = new User(e.getAsJsonObject());
      if (user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }
}
