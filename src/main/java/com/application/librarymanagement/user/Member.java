package com.application.librarymanagement.user;

import com.application.librarymanagement.borrow.Borrow;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

public final class Member extends User {
  private List<Borrow> borrows;

  public Member(String name, String username, String email, String password) {
    super(name, username, email, password);
    borrows = new ArrayList<>();
  }

  @Override
  protected JsonObject toJsonObject() {
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
}
