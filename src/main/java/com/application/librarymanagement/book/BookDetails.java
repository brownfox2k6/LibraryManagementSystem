package com.application.librarymanagement.book;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class BookDetails {
  private final StringProperty key = new SimpleStringProperty();
  private final StringProperty value = new SimpleStringProperty();

  public BookDetails(String key, String value) {
    this.key.set(key);
    this.value.set(value);
  }

  public String getKey() {
    return key.get();
  }

  public StringProperty keyProperty() {
    return key;
  }

  public String getValue() {
    return value.get();
  }

  public StringProperty valueProperty() {
    return value;
  }
}