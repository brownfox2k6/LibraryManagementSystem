package com.application.librarymanagement.borrow;

public class Timestamp {
  private int hour;
  private int minute;
  private int date;
  private int month;
  private int year;

  public Timestamp(int hour, int minute, int date, int month, int year) {
    this.hour = hour;
    this.minute = minute;
    this.date = date;
    this.month = month;
    this.year = year;
  }

  public Timestamp(String s) {
    // convert "hh:mm dd/mm/yyyy" to a Time object
  }

  @Override
  public String toString() {
    // return as "hh:mm dd/mm/yyyy" format
    return "";
  }
}
