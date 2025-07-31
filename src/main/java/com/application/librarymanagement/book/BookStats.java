package com.application.librarymanagement.book;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookStats {
  private final IntegerProperty rank = new SimpleIntegerProperty();
  private final StringProperty title = new SimpleStringProperty();
  private final IntegerProperty borrowsCount = new SimpleIntegerProperty();

  public BookStats(int rank, String title, int borrowsCount) {
    this.rank.set(rank);
    this.title.set(title);
    this.borrowsCount.set(borrowsCount);
  }

  public int getRank() {
    return rank.get();
  }

  public IntegerProperty rankProperty() {
    return rank;
  }

  public String getTitle() {
    return title.get();
  }

  public StringProperty titleProperty() {
    return title;
  }

  public int getBorrowsCount() {
    return borrowsCount.get();
  }

  public IntegerProperty borrowsCountProperty() {
    return borrowsCount;
  }
}
