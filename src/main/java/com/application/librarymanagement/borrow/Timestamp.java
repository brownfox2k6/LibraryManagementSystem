package com.application.librarymanagement.borrow;

import java.time.LocalDateTime;

public final class Timestamp {
  private final int year;
  private final int month;
  private final int date;
  private final int hour;
  private final int minute;

  public Timestamp(int year, int month, int date, int hour, int minute) {
    this.year = year;
    this.month = month;
    this.date = date;
    this.hour = hour;
    this.minute = minute;
  }

  public static Timestamp now() {
    LocalDateTime now = LocalDateTime.now();
    return new Timestamp(
        now.getYear(),
        now.getMonthValue(),
        now.getDayOfMonth(),
        now.getHour(),
        now.getMinute()
    );
  }

  @Override
  public String toString() {
    return String.format("%04d/%02d/%02d %02d:%02d", year, month, date, hour, minute);
  }

  // Getters
  public int getHour() { return hour; }
  public int getMinute() { return minute; }
  public int getDate() { return date; }
  public int getMonth() { return month; }
  public int getYear() { return year; }
}
