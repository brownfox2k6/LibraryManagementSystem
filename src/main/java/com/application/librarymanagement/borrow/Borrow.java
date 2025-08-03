package com.application.librarymanagement.borrow;

import com.google.gson.JsonObject;

public class Borrow {
  public static final int STATUS_REQUESTED = 1;
  public static final int STATUS_ACCEPTED = 2;
  public static final int STATUS_BORROWED = 3;
  public static final int STATUS_RETURNED = 4;
  private JsonObject data;

  // This will be stored in a JsonObject, for example:
  //  {
  //    "username": "handsomeboy",
  //    "id": "RSMuEAAAQBAJ",
  //    "requestedTime": "19:57 03/08/2025",
  //    "acceptedTime": "20:01 03/08/2025",
  //    "borrowedTime": "",
  //    "returnedTime": "",
  //    "status": 1
  //  }
  // Properties of a Borrow object (all wrapped in JsonObject data):
  // - username: String (who make this borrow?)
  // - id: String (the Google Books ID)
  // - requestedTime: String (the time member made this borrow request)
  // - acceptedTime: String (the time an admin accept this borrow request)
  // - borrowedTime: String (the time member come to library to take the book)
  // - returnedTime: String (the time member come to library to return the book)
  // - status: int (can be STATUS_REQUESTED, STATUS_ACCEPTED, STATUS_BORROWED or STATUS_RETURNED)
  // Note:
  // - For returnedTime, if the user has not returned the book yet, set it to be an empty string (""),
  // the same logic applied for acceptedTime and borrowedTime;
  // - For times, use a Time object
  // - All the properties listed above may be not enough, you can add more properties if you think it's necessary;
  // - For each property, write a getter and a setter for it;
  // - Use JsonUtils class' methods to retrieve data from JsonObject or JsonArray;
  // - See my code in User and Book class to know how to implement these.
  // - When you're done, delete this note.

  public Borrow(String username, String id, Time requestedTime) {
    data = new JsonObject();
    data.addProperty("username", username);
    data.addProperty("id", id);
    data.addProperty("requestedTime", requestedTime.toString());
    data.addProperty("status", STATUS_REQUESTED);
  }

  public Borrow(JsonObject data) {
    this.data = data;
  }

  public JsonObject getData() {
    return data;
  }
}
