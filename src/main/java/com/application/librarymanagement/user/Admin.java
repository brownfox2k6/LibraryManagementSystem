package com.application.librarymanagement.user;

import com.google.gson.JsonObject;

public final class Admin extends User {
public class Admin extends User {
  public Admin(String name, String username, String email, String password) {
    super(name, username, email, password);
  }

  @Override
  protected JsonObject toJsonObject() {
    return new JsonObject();
  }
}
