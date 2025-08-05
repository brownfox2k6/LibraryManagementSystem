package com.application.librarymanagement.borrow;

import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonObject;

public class Borrow {
  public static final int STATUS_REQUESTED = 1;
  public static final int STATUS_ACCEPTED = 2;
  public static final int STATUS_BORROWED = 3;
  public static final int STATUS_RETURNED = 4;

  private JsonObject data;

  public Borrow(String username, String id, Timestamp requestedTime) {
    data = new JsonObject();
    data.addProperty("username", username);
    data.addProperty("id", id);
    data.addProperty("requestedTime", requestedTime.toString());
    data.addProperty("acceptedTime", "");
    data.addProperty("borrowedTime", "");
    data.addProperty("returnedTime", "");
    data.addProperty("status", STATUS_REQUESTED);
  }

  public Borrow(JsonObject data) {
    this.data = data;
  }

  public JsonObject getData() {
    return data;
  }

  // Getters & Setters

  public String getUsername() {
    return JsonUtils.getAsString(data, "username", null);
  }

  public void setUsername(String username) {
    JsonUtils.setString(data, "username", username);
  }

  public String getId() {
    return JsonUtils.getAsString(data, "id", null);
  }

  public void setId(String id) {
    JsonUtils.setString(data, "id", id);
  }

  public String getRequestedTime() {
    return JsonUtils.getAsString(data, "requestedTime", null);
  }

  public void setRequestedTime(String requestedTime) {
    JsonUtils.setString(data, "requestedTime", requestedTime);
  }

  public String getAcceptedTime() {
    return JsonUtils.getAsString(data, "acceptedTime", null);
  }

  public void setAcceptedTime(String acceptedTime) {
    JsonUtils.setString(data, "acceptedTime", acceptedTime);
  }

  public String getBorrowedTime() {
    return JsonUtils.getAsString(data, "borrowedTime",null);
  }

  public void setBorrowedTime(String borrowedTime) {
    JsonUtils.setString(data, "borrowedTime", borrowedTime);
  }

  public String getReturnedTime() {
    return JsonUtils.getAsString(data, "returnedTime", null);
  }

  public void setReturnedTime(String returnedTime) {
    JsonUtils.setString(data, "returnedTime", returnedTime);
  }

  public int getStatus() {
    return JsonUtils.getAsInt(data, "status", 0);
  }

  public void setStatus(int status) {
    JsonUtils.setInt(data, "status", status);
  }

  // Trạng thái tiện ích
  public boolean isRequested() {
    return getStatus() == STATUS_REQUESTED;
  }

  public boolean isAccepted() {
    return getStatus() == STATUS_ACCEPTED;
  }

  public boolean isBorrowed() {
    return getStatus() == STATUS_BORROWED;
  }

  public boolean isReturned() {
    return getStatus() == STATUS_RETURNED;
  }

  @Override
  public String toString() {
    return "Borrow{" +
            "username='" + getUsername() + '\'' +
            ", id='" + getId() + '\'' +
            ", requestedTime='" + getRequestedTime() + '\'' +
            ", acceptedTime='" + getAcceptedTime() + '\'' +
            ", borrowedTime='" + getBorrowedTime() + '\'' +
            ", returnedTime='" + getReturnedTime() + '\'' +
            ", status=" + getStatus() +
            '}';
  }
}
