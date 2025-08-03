package com.application.librarymanagement.borrow;

public class Time {
  private int hour;
  private int minute;
  private int date;
  private int month;
  private int year;

  public Time(int hour, int minute, int date, int month, int year) {
    this.hour = hour;
    this.minute = minute;
    this.date = date;
    this.month = month;
    this.year = year;
  }

  public String toString() {
    // return as "hh:mm dd/mm/yyyy" format
    return "";
  }
}
