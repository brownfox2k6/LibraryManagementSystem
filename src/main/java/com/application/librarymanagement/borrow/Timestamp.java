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
    // Expected format: "hh:mm dd/mm/yyyy"
    String[] parts = s.split(" ");
    if (parts.length != 2) throw new IllegalArgumentException("Invalid timestamp format");

    String[] timeParts = parts[0].split(":");
    String[] dateParts = parts[1].split("/");

    this.hour = Integer.parseInt(timeParts[0]);
    this.minute = Integer.parseInt(timeParts[1]);
    this.date = Integer.parseInt(dateParts[0]);
    this.month = Integer.parseInt(dateParts[1]);
    this.year = Integer.parseInt(dateParts[2]);
  }

  public static Timestamp now() {
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    return new Timestamp(
            now.getHour(),
            now.getMinute(),
            now.getDayOfMonth(),
            now.getMonthValue(),
            now.getYear()
    );
  }


  @Override
  public String toString() {
    return String.format("%02d:%02d %02d/%02d/%04d", hour, minute, date, month, year);
  }

  // Getters
  public int getHour() { return hour; }
  public int getMinute() { return minute; }
  public int getDate() { return date; }
  public int getMonth() { return month; }
  public int getYear() { return year; }
}
