package com.application.librarymanagement.user;

public class Admin extends User {
  public Admin(String name, String username, String email, String password) {
    super(name, username, email, password);
  }

  @Override
  public boolean saveToDatabase(boolean canOverwrite) {
    return false;
  }
}
