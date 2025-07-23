// File: Borrow.java
package com.application.librarymanagement.borrow;

import com.google.gson.JsonObject;

public class Borrow {
    private String username;
    private String bookId;

    public Borrow(String username, String bookId) {
        this.username = username;
        this.bookId = bookId;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);
        obj.addProperty("bookId", bookId);
        return obj;
    }
}
