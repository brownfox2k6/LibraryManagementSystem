package com.application.librarymanagement.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.application.librarymanagement.MainApp;

import javafx.scene.paint.Color;

public final class InitializeDatabase {
  private static final String CONTENT_BOOKS = """
      []
      """.stripIndent();
  private static final String CONTENT_BORROWS = """
      []
      """.stripIndent();
  private static final String CONTENT_CONFIG = """
      {
        "theme": "cupertino-light",
        "currentSession": ""
      }
      """.stripIndent();
  private static final String CONTENT_USERS = """
      [
        {
          "name": "admin",
          "username": "admin",
          "password": "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918",
          "type": 1
        }
      ]
      """.stripIndent();

  public InitializeDatabase() {
    createJsonFolder();
    createFile(MainApp.BOOKS_DB_PATH, CONTENT_BOOKS);
    createFile(MainApp.BORROWS_DB_PATH, CONTENT_BORROWS);
    createFile(MainApp.CONFIG_PATH, CONTENT_CONFIG);
    createFile(MainApp.USERS_DB_PATH, CONTENT_USERS);
  }

  private void createJsonFolder() {
    File dir = new File("json");
    if (!dir.exists()) {
      if (!dir.mkdir()) {
        showErrorMessage("");
      }
    }
  }

  private void createFile(Path path, String content) {
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

  private void showErrorMessage(String message) {
    MainApp.showPopupMessage("An error occurred when initializing the database"
        + (message.isEmpty() ? "." : ": " + message), Color.DARKRED);
  }
}
