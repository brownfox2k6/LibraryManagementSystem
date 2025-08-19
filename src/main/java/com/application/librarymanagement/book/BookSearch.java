package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.InAppController;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonElement;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class BookSearch {
  private String q = "";
  private String intitle = "";
  private String inauthor = "";
  private String inpublisher = "";
  private String subject = "";
  private String isbn = "";
  private String lccn = "";
  private String oclc = "";

  public void setQ(String q) {
    this.q = URLEncoder.encode(q, StandardCharsets.UTF_8);
  }

  public void setIntitle(String intitle) {
    this.intitle = URLEncoder.encode(intitle, StandardCharsets.UTF_8);
  }

  public void setInauthor(String inauthor) {
    this.inauthor = URLEncoder.encode(inauthor, StandardCharsets.UTF_8);
  }

  public void setInpublisher(String inpublisher) {
    this.inpublisher = URLEncoder.encode(inpublisher, StandardCharsets.UTF_8);
  }

  public void setSubject(String subject) {
    this.subject = URLEncoder.encode(subject, StandardCharsets.UTF_8);
  }

  public void setIsbn(String isbn) {
    this.isbn = URLEncoder.encode(isbn, StandardCharsets.UTF_8);
  }

  public void setLccn(String lccn) {
    this.lccn = URLEncoder.encode(lccn, StandardCharsets.UTF_8);
  }

  public void setOclc(String oclc) {
    this.oclc = URLEncoder.encode(oclc, StandardCharsets.UTF_8);
  }

  public String getUrl() {
    StringBuilder url = new StringBuilder("https://www.googleapis.com/books/v1/volumes?q=").append(q);
    if (!intitle.isEmpty()) {
      url.append("+intitle:").append(intitle);
    }
    if (!inauthor.isEmpty()) {
      url.append("+inauthor:").append(inauthor);
    }
    if (!inpublisher.isEmpty()) {
      url.append("+inpublisher:").append(inpublisher);
    }
    if (!subject.isEmpty()) {
      url.append("+subject:").append(subject);
    }
    if (!isbn.isEmpty()) {
      url.append("+isbn:").append(isbn);
    }
    if (!lccn.isEmpty()) {
      url.append("+lccn:").append(lccn);
    }
    if (!oclc.isEmpty()) {
      url.append("+oclc:").append(oclc);
    }
    url.append("&maxResults=40");
    return url.toString();
  }

  private boolean containsIgnoreCase(String s1, String s2) {
    if (s1.isEmpty() || s2.isEmpty()) {
      return false;
    }
    return s1.toLowerCase().contains(s2.toLowerCase());
  }

  public void getBooks(List<Book> results) {
    results.clear();
    Set<String> existIds = new HashSet<>();
    for (JsonElement e : MainApp.BOOKS) {
      Book b = Book.fromJsonObject(e.getAsJsonObject());
      if (containsIgnoreCase(b.getTitle(), intitle) || containsIgnoreCase(b.getTitle(), q)
          || containsIgnoreCase(b.getAuthors(), inauthor) || containsIgnoreCase(b.getAuthors(), q)
          || containsIgnoreCase(b.getPublisher(), inpublisher) || containsIgnoreCase(b.getPublisher(), q)
          || containsIgnoreCase(b.getCategories(), subject) || containsIgnoreCase(b.getCategories(), q)
          || containsIgnoreCase(b.getIsbn10(), isbn) || containsIgnoreCase(b.getIsbn10(), q)
          || containsIgnoreCase(b.getIsbn13(), isbn) || containsIgnoreCase(b.getIsbn13(), q)) {
        results.add(b);
        existIds.add(b.getId());
      }
    }
    if (InAppController.CURRENT_USER.isAdmin()) {
      try {
        for (JsonElement e : JsonUtils.getAsJsonArray(JsonUtils.fetchJson(getUrl()), "items")) {
          Book b = Book.fromJsonObject(e.getAsJsonObject());
          if (!existIds.contains(b.getId())) {
            results.add(b);
          }
        }
      } catch (IOException ex) {
        MainApp.showPopupMessage("Unable to load books", Color.DARKRED);
      }
    }
  }
}