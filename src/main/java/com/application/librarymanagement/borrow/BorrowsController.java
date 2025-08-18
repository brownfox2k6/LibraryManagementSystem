package com.application.librarymanagement.borrow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public final class BorrowsController {
  @FXML private VBox borrows;
  @FXML private Text usernameText;
  @FXML private TextField usernameField;
  @FXML private Label resultLabel;
  @FXML private Button previousPageButton;
  @FXML private Button nextPageButton;
  @FXML private ListView<String> userSuggestionsList;

  private static final int PAGE_SIZE = 20;
  private int page;
  private User user;
  private final ArrayList<Borrow> borrowList = new ArrayList<>();
  private final ArrayList<Borrow> filteredList = new ArrayList<>();
  private FilteredList<String> filteredUsernames;
  private SortedList<String> sortedUsernames;

  public void initialize() {
    HashSet<String> set = new HashSet<>();
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.USERS_DB_PATH)) {
      set.add(new User(e.getAsJsonObject()).getUsername());
    }
    ObservableList<String> usernameItems = FXCollections.observableArrayList(set);
    filteredUsernames = new FilteredList<>(usernameItems, s -> false);
    sortedUsernames = new SortedList<>(filteredUsernames);
    sortedUsernames.setComparator(Comparator.comparing(String::toLowerCase));
    userSuggestionsList.setItems(sortedUsernames);
    makeNodeDisappear(userSuggestionsList);
    userSuggestionsList.setMaxHeight(200);
    user = InAppController.CURRENT_USER;
    if (user.isMember()) {
      makeNodeDisappear(usernameText);
      makeNodeDisappear(usernameField);
    }
    usernameField.textProperty().addListener((obs, oldText, newText) -> {
      String q = (newText == null ? "" : newText.trim()).toLowerCase();
      sortedUsernames.setComparator((a, b) -> {
        String la = a.toLowerCase(), lb = b.toLowerCase();
        boolean aStarts = !q.isEmpty() && la.startsWith(q);
        boolean bStarts = !q.isEmpty() && lb.startsWith(q);
        if (aStarts && !bStarts) return -1;
        if (!aStarts && bStarts) return 1;
        return la.compareTo(lb);
      });
      if (q.isEmpty()) {
        filteredUsernames.setPredicate(s -> false);
        makeNodeDisappear(userSuggestionsList);
      } else {
        filteredUsernames.setPredicate(name -> name != null && name.toLowerCase().contains(q));
        boolean has = !filteredUsernames.isEmpty();
        userSuggestionsList.setVisible(has);
        userSuggestionsList.setManaged(has);
        if (has) {
          userSuggestionsList.getSelectionModel().clearSelection();
          userSuggestionsList.scrollTo(0);
        }
      }
    });
    userSuggestionsList.setOnMouseClicked(e -> {
      String sel = userSuggestionsList.getSelectionModel().getSelectedItem();
      if (sel != null) {
        applyUsernameSuggestion(sel);
      }
    });
    usernameField.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.DOWN) {
        if (userSuggestionsList.isVisible()) {
          userSuggestionsList.requestFocus();
          userSuggestionsList.getSelectionModel().selectFirst();
          e.consume();
        }
      } else if (e.getCode() == KeyCode.ENTER) {
        if (!userSuggestionsList.isVisible()) {
          filterByUsername();
        } else {
          String sel = userSuggestionsList.getSelectionModel().getSelectedItem();
          if (sel == null && !sortedUsernames.isEmpty()) {
            sel = sortedUsernames.get(0);
          }
          if (sel != null) {
            applyUsernameSuggestion(sel);
            e.consume();
          }
        }
      } else if (e.getCode() == KeyCode.ESCAPE) {
        makeNodeDisappear(userSuggestionsList);
      }
    });
    userSuggestionsList.setOnKeyPressed(e -> {
      if (e.getCode() == KeyCode.ENTER) {
        String sel = userSuggestionsList.getSelectionModel().getSelectedItem();
        if (sel != null) {
          applyUsernameSuggestion(sel);
          e.consume();
        }
      } else if (e.getCode() == KeyCode.ESCAPE) {
        makeNodeDisappear(userSuggestionsList);
        usernameField.requestFocus();
      }
    });
    loadBorrows();
    displayBorrowsByUsernameAndStatus(0);
  }

  private void applyUsernameSuggestion(String username) {
    usernameField.setText(username);
    makeNodeDisappear(userSuggestionsList);
    filterByUsername();
  }

  private void loadBorrows() {
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BORROWS_DB_PATH)) {
      Borrow borrow = new Borrow(e.getAsJsonObject());
      if (user.isAdmin() || borrow.getUsername().equals(user.getUsername())) {
        borrowList.add(borrow);
      }
    }
    borrowList.sort(Comparator.comparing(Borrow::getLatestTimestamp, Comparator.reverseOrder()));
  }

  private void displayBorrows(ArrayList<Borrow> borrowList) {
    borrows.getChildren().clear();
    int start = PAGE_SIZE * (page - 1);
    int end = Math.min(PAGE_SIZE * page, borrowList.size());
    previousPageButton.setDisable(page == 1);
    nextPageButton.setDisable(end == borrowList.size());
    for (int i = start; i < end; i++) {
      FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("scenes/BorrowCase.fxml"));
      HBox borrowCaseBox;
      try {
        borrowCaseBox = fxmlLoader.load();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      BorrowCaseController bcc = fxmlLoader.getController();
      bcc.setData(borrowList.get(i));
      borrows.getChildren().add(borrowCaseBox);
    }
    resultLabel.setText(String.format("Page %d/%d: Results %d to %d out of %d.",
        page, (borrowList.size() + PAGE_SIZE - 1) / PAGE_SIZE, start + 1, end, borrowList.size()));
  }

  @FXML
  private void gotoPreviousPage() {
    --page;
    displayBorrows(filteredList);
  }

  @FXML
  private void gotoNextPage() {
    ++page;
    displayBorrows(filteredList);
  }

  private void makeNodeDisappear(Node node) {
    node.setVisible(false);
    node.setManaged(false);
  }

  private void displayBorrowsByUsernameAndStatus(int status) {
    page = 1;
    filteredList.clear();
    for (Borrow borrow : borrowList) {
      if (borrow.getUsername().contains(usernameField.getText())
          && (status == 0 || borrow.getStatus() == status)) {
        filteredList.add(borrow);
      }
    }
    displayBorrows(filteredList);
    StringBuilder sb = new StringBuilder("Filtered by: ");
    if (!usernameField.getText().isEmpty()) {
      sb.append("username contains ");
      sb.append('"');
      sb.append(usernameField.getText());
      sb.append('"');
      sb.append(status == 0? ". " : ", ");
    }
    if (status != 0) {
      sb.append("status=");
      sb.append(Borrow.getStatusText(status));
      sb.append(". ");
    }
    if (usernameField.getText().isEmpty() && status == 0) {
      sb.setLength(0);
    }
    if (filteredList.isEmpty()) {
      sb.append("No records found.");
      MainApp.showPopupMessage(sb.toString(), Color.DARKRED);
    } else if (filteredList.size() == 1) {
      sb.append("Found 1 record.");
      MainApp.showPopupMessage(sb.toString(), Color.DARKGREEN);
    } else {
      sb.append(String.format("Found %d records.", filteredList.size()));
      MainApp.showPopupMessage(sb.toString(), Color.DARKGREEN);
    }
  }

  @FXML
  private void filterByStatusRequested() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_REQUESTED);
  }

  @FXML
  private void filterByStatusBorrowed() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_BORROWED);
  }

  @FXML
  private void filterByStatusReturned() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_RETURNED);
  }

  @FXML
  private void filterByStatusCanceled() {
    displayBorrowsByUsernameAndStatus(Borrow.STATUS_CANCELED);
  }

  @FXML
  private void filterByUsername() {
    displayBorrowsByUsernameAndStatus(0);
  }

  public void filterByUsernameExternal(String username) {
    usernameField.setText(username);
    makeNodeDisappear(userSuggestionsList);
    page = 1;
    filteredList.clear();
    for (Borrow borrow : borrowList) {
      if (borrow.getUsername().contains(username)) {
        filteredList.add(borrow);
      }
    }
    displayBorrows(filteredList);
    if (filteredList.isEmpty()) {
      MainApp.showPopupMessage("No records found for username: " + username, Color.DARKRED);
    } else {
      MainApp.showPopupMessage("Found " + filteredList.size() + " records for username: " + username, Color.DARKGREEN);
    }
  }
}
