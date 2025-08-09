package com.application.librarymanagement.book;

import javafx.beans.property.SimpleStringProperty;

public final class BookDetails {
  private final SimpleStringProperty key;
  private final SimpleStringProperty value;

  public BookDetails(String key, String value) {
    this.key = new SimpleStringProperty(key);
    this.value = new SimpleStringProperty(value);
  }

  public String getKey() { return key.get(); }
  public String getValue() { return value.get(); }
  public SimpleStringProperty keyProperty() { return key; }
  public SimpleStringProperty valueProperty() { return value; }
}