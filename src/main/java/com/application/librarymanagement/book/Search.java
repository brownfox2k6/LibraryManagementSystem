package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Builds and executes paginated search queries against the Google Books API.
 * Supports filtering by general query, title, author, publisher, subject,
 * ISBN, LCCN, and OCLC. Manages pagination through {@link #nextPage()} and
 * {@link #previousPage()} with a fixed page size.
 */
public final class Search {
  private String q = "";
  private String intitle = "";
  private String inauthor = "";
  private String inpublisher = "";
  private String subject = "";
  private String isbn = "";
  private String lccn = "";
  private String oclc = "";

  /** Maximum number of items to fetch per page. */
  private static final int MAX_COUNT = 40;

  /** The zero-based index of the first item in the current page. */
  private int startIndex = 0;

  /** Total number of items available from the last query. */
  private int totalItems;

  /**
   * [REQUIRED] Sets the general search query. Resets pagination to the first page.
   * @param q the free-text search terms (will be URL-encoded)
   */
  public void setQ(String q) {
    this.q = URLEncoder.encode(q, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * [OPTIONAL] Sets the title filter. Resets pagination to the first page.
   * @param intitle title keywords (will be URL-encoded)
   */
  public void setIntitle(String intitle) {
    this.intitle = URLEncoder.encode(intitle, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * [OPTIONAL] Sets the author filter. Resets pagination to the first page.
   * @param inauthor author name keywords (will be URL-encoded)
   */
  public void setInauthor(String inauthor) {
    this.inauthor = URLEncoder.encode(inauthor, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * [OPTIONAL] Sets the publisher filter. Resets pagination to the first page.
   * @param inpublisher publisher name keywords (will be URL-encoded)
   */
  public void setInpublisher(String inpublisher) {
    this.inpublisher = URLEncoder.encode(inpublisher, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * [OPTIONAL] Sets the subject filter. Resets pagination to the first page.
   * @param subject subject keywords (will be URL-encoded)
   */
  public void setSubject(String subject) {
    this.subject = URLEncoder.encode(subject, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * [OPTIONAL] Sets the ISBN filter. Resets pagination to the first page.
   * @param isbn ISBN value (will be URL-encoded)
   */
  public void setIsbn(String isbn) {
    this.isbn = URLEncoder.encode(isbn, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * [OPTIONAL] Sets the Library of Congress Control Number (LCCN) filter.
   * Resets pagination to the first page.
   * @param lccn LCCN value (will be URL-encoded)
   */
  public void setLccn(String lccn) {
    this.lccn = URLEncoder.encode(lccn, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * [OPTIONAL] Sets the OCLC Number filter. Resets pagination to the first page.
   * @param oclc OCLC value (will be URL-encoded)
   */
  public void setOclc(String oclc) {
    this.oclc = URLEncoder.encode(oclc, StandardCharsets.UTF_8);
    this.startIndex = 0;
  }

  /**
   * Determines if there is a next page of results available.
   * @return {@code true} if {@code startIndex + MAX_COUNT} is less than {@code totalItems}
   */
  public boolean canNextPage() {
    return this.startIndex + MAX_COUNT < this.totalItems;
  }

  /**
   * Advances the pagination to the next page if possible.
   */
  public void nextPage() {
    if (canNextPage()) {
      this.startIndex += MAX_COUNT;
    }
  }

  /**
   * Determines if there is a previous page of results available.
   * @return {@code true} if {@code startIndex} is greater than zero
   */
  public boolean canPreviousPage() {
    return this.startIndex > 0;
  }

  /**
   * Moves the pagination back to the previous page if possible.
   */
  public void previousPage() {
    if (canPreviousPage()) {
      this.startIndex -= MAX_COUNT;
    }
  }

  public String getUrl() {
    String url = "https://www.googleapis.com/books/v1/volumes?q=" + this.q;
    if (!this.intitle.isEmpty()) {
      url += "+intitle:" + this.intitle;
    }
    if (!this.inauthor.isEmpty()) {
      url += "+inauthor:" + this.inauthor;
    }
    if (!this.inpublisher.isEmpty()) {
      url += "+inpublisher:" + this.inpublisher;
    }
    if (!this.subject.isEmpty()) {
      url += "+subject:" + this.subject;
    }
    if (!this.isbn.isEmpty()) {
      url += "+isbn:" + this.isbn;
    }
    if (!this.lccn.isEmpty()) {
      url += "+lccn:" + this.lccn;
    }
    if (!this.oclc.isEmpty()) {
      url += "+oclc:" + this.oclc;
    }
    url += "&maxResults=" + MAX_COUNT;
    url += "&startIndex=" + this.startIndex;
    return url;
  }

  public JsonArray getBooks() {
    JsonObject result = null;
    try {
      result = JsonUtils.fetchJson(getUrl());
      System.out.println(getUrl());
    } catch (IOException ex) {
      MainApp.showPopupMessage("Unable to load books", Color.DARKRED);
    }
    this.totalItems = JsonUtils.getAsInt(result, "totalItems", 0);
    return JsonUtils.getAsJsonArray(result, "items");
  }
}