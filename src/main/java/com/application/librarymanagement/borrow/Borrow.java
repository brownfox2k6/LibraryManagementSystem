package com.application.librarymanagement.borrow;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public final class Borrow {
  public static final int STATUS_REQUESTED = 1;
  public static final int STATUS_BORROWED = 2;
  public static final int STATUS_RETURNED = 3;
  public static final int STATUS_CANCELED = 4;

  private final JsonObject data;

  public Borrow(int borrowId, String username, String bookId) {
    data = new JsonObject();
    data.addProperty("borrowId", borrowId);
    data.addProperty("username", username);
    data.addProperty("bookId", bookId);
    data.addProperty("requestedTime", Timestamp.now().toString());
    data.addProperty("status", STATUS_REQUESTED);
    changeQuantity(-1, 1);
  }

  public static int addNewBorrow(String username, String bookId) {
    JsonArray borrows = MainApp.BORROWS;
    int borrowId = borrows.size() + 1;
    Borrow borrow = new Borrow(borrowId, username, bookId);
    borrows.add(borrow.getData());
    JsonUtils.saveToFile(borrows, MainApp.BORROWS_DB_PATH);
    return borrowId;
  }

  public void saveToDatabase() {
    JsonArray borrows = MainApp.BORROWS;
    for (int i = 0; i < borrows.size(); i++) {
      if (JsonUtils.getAsInt(borrows.get(i).getAsJsonObject(), "borrowId", 0) == getBorrowId()) {
        borrows.set(i, data);
        JsonUtils.saveToFile(borrows, MainApp.BORROWS_DB_PATH);
        return;
      }
    }
    assert false;
  }

  public Borrow(JsonObject data) {
    this.data = data;
  }

  public JsonObject getData() {
    return data;
  }

  public String getUsername() {
    return JsonUtils.getAsString(data, "username", null);
  }

  public int getBorrowId() {
    return JsonUtils.getAsInt(data, "borrowId", 0);
  }

  public String getBookId() {
    return JsonUtils.getAsString(data, "bookId", "");
  }

  public String getRequestedTime() {
    return JsonUtils.getAsString(data, "requestedTime", "");
  }

  public String getBorrowedTime() {
    return JsonUtils.getAsString(data, "borrowedTime","");
  }

  public String getReturnedTime() {
    return JsonUtils.getAsString(data, "returnedTime", "");
  }

  public String getCanceledTime() {
    return JsonUtils.getAsString(data, "canceledTime", "");
  }

  public String getLatestTimestamp() {
    return Stream.of(getRequestedTime(), getBorrowedTime(), getReturnedTime(), getCanceledTime())
        .max(String::compareTo)
        .orElseThrow(() -> new IllegalStateException("Cannot get latest timestamp"));
  }

  public int getStatus() {
    return JsonUtils.getAsInt(data, "status", 0);
  }

  public static String getStatusText(int status) {
    return switch (status) {
      case Borrow.STATUS_REQUESTED -> "REQUESTED";
      case Borrow.STATUS_RETURNED -> "RETURNED";
      case Borrow.STATUS_CANCELED -> "CANCELED";
      case Borrow.STATUS_BORROWED -> "BORROWED";
      default -> throw new IllegalStateException("Unexpected value: " + status);
    };
  }

  public String getStatusText() {
    return getStatusText(getStatus());
  }

  public void setBorrowed() {
    data.addProperty("borrowedTime", Timestamp.now().toString());
    data.addProperty("status", STATUS_BORROWED);
    saveToDatabase();
  }

  public void setReturned() {
    data.addProperty("returnedTime", Timestamp.now().toString());
    data.addProperty("status", STATUS_RETURNED);
    changeQuantity(1, 0);
    saveToDatabase();
  }

  public void setCanceled() {
    data.addProperty("canceledTime", Timestamp.now().toString());
    data.addProperty("status", STATUS_CANCELED);
    changeQuantity(1, -1);
    saveToDatabase();
  }

  private void changeQuantity(int bookDelta, int borrowsCountDelta) {
    assert Math.abs(bookDelta) == 1 && Math.abs(borrowsCountDelta) <= 1;
    for (JsonElement e : MainApp.BOOKS) {
      Book book = Book.fromJsonObject(e.getAsJsonObject());
      if (book.getId().equals(getBookId())) {
        book.adjustQuantity(bookDelta);
        book.adjustBorrowsCount(borrowsCountDelta);
        book.updateToDatabase();
        return;
      }
    }
    assert false;
  }

  public static ArrayList<Pair<String, Integer>> getRecentBorrows(int days) {
    ArrayList<Pair<String, Integer>> recentBorrows = new ArrayList<>();
    LocalDate today = LocalDate.now();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd");
    for (int i = 0; i < days; ++i) {
      recentBorrows.add(new Pair<>(today.minusDays(i).format(fmt), 0));
    }
    for (JsonElement e : MainApp.BORROWS) {
      Borrow borrow = new Borrow(e.getAsJsonObject());
      if (borrow.getStatus() == STATUS_CANCELED) {
        continue;
      }
      LocalDate date = LocalDate.parse(borrow.getRequestedTime(),
          DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
      int diff = (int) ChronoUnit.DAYS.between(date, today);
      if (diff < days) {
        Pair<String, Integer> p = recentBorrows.get(diff);
        recentBorrows.set(diff, new Pair<>(p.getKey(), p.getValue() + 1));
      }
    }
    return recentBorrows;
  }
}
