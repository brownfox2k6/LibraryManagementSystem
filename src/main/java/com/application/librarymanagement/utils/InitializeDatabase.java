package com.application.librarymanagement.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.application.librarymanagement.MainApp;

import javafx.scene.paint.Color;

public final class InitializeDatabase {
  private static final String books = """
      []
      """.stripIndent();
  private static final String borrows = """
      []
      """.stripIndent();
  private static final String config = """
      {
        "theme": "cupertino-light",
        "currentSession": ""
      }
      """.stripIndent();
  private static final String users = """
      [
        {
          "name": "admin",
          "username": "admin",
          "password": "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918",
          "type": 1
        }
      ]
      """.stripIndent();

  public static void initialize() {
    createJsonFolder();
    createFile(MainApp.BOOKS_DB_PATH, books);
    createFile(MainApp.BORROWS_DB_PATH, borrows);
    createFile(MainApp.CONFIG_PATH, config);
    createFile(MainApp.USERS_DB_PATH, users);
  }

  private static void createJsonFolder() {
    File dir = new File("json");
    if (!dir.exists()) {
      if (!dir.mkdir()) {
        showErrorMessage("");
      }
    }
  }

  private static void createFile(Path path, String content) {
    File file = new File(path.toUri());
    if (!file.exists()) {
      try {
        if (file.createNewFile()) {
          FileWriter fileWriter = new FileWriter(file);
          fileWriter.write(content);
          fileWriter.close();
        } else {
          showErrorMessage("");
        }
      } catch (IOException e) {
        showErrorMessage(e.getMessage());
      }
    }
  }

  private static void showErrorMessage(String msg) {
    MainApp.showPopupMessage("An error occurred when initializing the database"
        + (msg.isEmpty() ? "." : ": " + msg), Color.DARKRED);
  }
}
