package generator;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.book.Book;
import com.application.librarymanagement.book.Search;
import com.application.librarymanagement.borrow.Borrow;
import com.application.librarymanagement.borrow.Timestamp;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class GenerateSampleDatabase {
  private static final List<String> bookIds = new ArrayList<>();
  private static final List<String> usernames = new ArrayList<>();
  private static final Map<String, List<Integer>> borrowsOfUsername = new HashMap<>();

  public static void loadUsernames() {
    try (InputStream in = GenerateSampleDatabase.class.getResourceAsStream("/names-dataset.txt")) {
      assert in != null;
      try (Scanner sc = new Scanner(in)) {
        while (sc.hasNextLine()) {
          String username = sc.nextLine();
          usernames.add(username);
          borrowsOfUsername.put(username, new ArrayList<>());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void createSampleBooks() {
    System.out.println("----- Creating sample books -----");
    try (InputStream in = GenerateSampleDatabase.class.getResourceAsStream("/isbn-dataset.txt")) {
      assert in != null;
      try (Scanner sc = new Scanner(in)) {
        Search search = new Search();
        int count = 0;
        JsonArray books = new JsonArray();
        while (sc.hasNextLine()) {
          String isbn = sc.nextLine();
          System.out.printf("Adding book which ISBN=%s... ", isbn);
          search.setIsbn(isbn);
          JsonObject obj;
          try {
            obj = JsonUtils.fetchJson(String.format("https://www.googleapis.com/books/v1/volumes?q=+isbn:%s&maxResults=1", isbn));
          } catch (IOException ex) {
            System.out.println("Failed.");
            continue;
          }
          JsonArray result = JsonUtils.getAsJsonArray(obj, "items");
          if (!result.isEmpty()) {
            Book book = Book.fromJsonObject(result.get(0).getAsJsonObject());
            book.setQuantity(5);
            books.add(book.getData());
            bookIds.add(book.getId());
            System.out.printf("Success. (%d)\n", ++count);
          } else {
            System.out.println("Failed.");
          }
        }
        JsonUtils.saveToFile(books, MainApp.BOOKS_DB_PATH);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void createSampleMembers() {
    System.out.println("----- Creating sample members -----");
    JsonArray users = new JsonArray();
    for (int i = 0; i < usernames.size(); ++i) {
      String username = usernames.get(i);
      String name = username.substring(0, 1).toUpperCase() + username.substring(1);
      String email = username + "@gmail.com";
      User member = new User(name, username, email, username, User.TYPE_MEMBER);
      for (int id : borrowsOfUsername.get(username)) {
        member.getBorrows().add(id);
      }
      users.add(member.getData());
      System.out.printf("[%d] Added member [%s] to database.\n", i + 1, username);
    }
    users.add(new User("Admin", "admin", "admin@gmail.com", "admin", User.TYPE_ADMIN).getData());
    JsonUtils.saveToFile(users, MainApp.USERS_DB_PATH);
  }

  public static void createSampleBorrows() {
    System.out.println("----- Creating sample borrows -----");
    long epochStart = LocalDateTime.of(2023, 1, 1, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
    long epochEnd1 = LocalDateTime.of(2025, 7, 31, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
    long epochEnd2 = LocalDateTime.of(2025, 8, 5, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
    long epochEnd3 = LocalDateTime.of(2025, 8, 15, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
    long epochEnd4 = LocalDateTime.of(2025, 8, 16, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);
    JsonArray borrows = new JsonArray();
    for (int i = 1; i <= 100000; ++i) {
      int status = ThreadLocalRandom.current().nextInt(1, 5);
      long requested = ThreadLocalRandom.current().nextLong(epochStart, epochEnd1);
      long borrowed = ThreadLocalRandom.current().nextLong(requested, epochEnd2);
      long returned = ThreadLocalRandom.current().nextLong(borrowed, epochEnd3);
      long canceled = ThreadLocalRandom.current().nextLong(returned, epochEnd4);
      String username = usernames.get(ThreadLocalRandom.current().nextInt(usernames.size()));
      String bookId = bookIds.get(ThreadLocalRandom.current().nextInt(bookIds.size()));
      JsonObject d = new JsonObject();
      d.addProperty("borrowId", i);
      d.addProperty("username", username);
      d.addProperty("bookId", bookId);
      d.addProperty("requestedTime", new Timestamp(requested).toString());
      if (status == Borrow.STATUS_BORROWED || status == Borrow.STATUS_RETURNED) {
        d.addProperty("borrowedTime", new Timestamp(borrowed).toString());
      }
      if (status == Borrow.STATUS_RETURNED) {
        d.addProperty("returnedTime", new Timestamp(returned).toString());
      }
      if (status == Borrow.STATUS_CANCELED) {
        d.addProperty("canceledTime", new Timestamp(canceled).toString());
      }
      d.addProperty("status", status);
      borrows.add(d);
      borrowsOfUsername.get(username).add(i);
      System.out.printf("Added borrow #%d.\n", i);
    }
    JsonUtils.saveToFile(borrows, MainApp.BORROWS_DB_PATH);
  }

  public static void main(String[] args) {
    loadUsernames();
    createSampleBooks();
    createSampleBorrows();
    createSampleMembers();
  }
}
