package com.application.librarymanagement.user;

import com.application.librarymanagement.utils.PasswordUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

abstract class User {
  protected static final Path USERS_DB_PATH = Paths.get("json/users.json");
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

  abstract public boolean saveToDatabase(boolean canOverwrite);
}
