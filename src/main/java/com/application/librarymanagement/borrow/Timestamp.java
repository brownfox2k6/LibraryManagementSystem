package com.application.librarymanagement.borrow;

public final class Timestamp {
  private int year;
  private int month;
  private int date;
  private int hour;
  private int minute;

  public Timestamp(int year, int month, int date, int hour, int minute) {
    this.year = year;
    this.month = month;
    this.date = date;
    this.hour = hour;
    this.minute = minute;
  }

  public Timestamp(String s) {
    // Expected format: "hh:mm dd/mm/yyyy"
    String[] parts = s.split(" ");
    if (parts.length != 2) throw new IllegalArgumentException("Invalid timestamp format");

    String[] datePart = parts[0].split("/");
    String[] timePart = parts[1].split(":");

    this.year = Integer.parseInt(datePart[0]);
    this.month = Integer.parseInt(datePart[1]);
    this.date = Integer.parseInt(datePart[2]);
    this.hour = Integer.parseInt(timePart[0]);
    this.minute = Integer.parseInt(timePart[1]);
  }

  public static Timestamp now() {
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
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
