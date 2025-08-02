package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class BookDetailsController {
  @FXML private TableView<BookDetail> detailsTable;
  @FXML private TableColumn<BookDetail, String> keyCol;
  @FXML private TableColumn<BookDetail, String> valueCol;
  @FXML private ImageView thumbnail;
  @FXML private Label title;
  @FXML private Label authors;
  @FXML private Label publisher;
  @FXML private Label description;
  @FXML private Label quantity;
  @FXML private Button borrowButton;

  private Book book;

  @FXML
  public void initialize() {
    keyCol.setCellValueFactory(cd -> cd.getValue().keyProperty());
    valueCol.setCellValueFactory(cd -> cd.getValue().valueProperty());
    keyCol.setPrefWidth(105);
    valueCol.setPrefWidth(1000);
    keyCol.setResizable(false);
    valueCol.setResizable(false);
    keyCol.setSortable(false);
    valueCol.setSortable(false);
    detailsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    detailsTable.setRowFactory(tv -> {
      TableRow<BookDetail> row = new TableRow<>();
      row.setPadding(new Insets(8, 0, 8, 0));
      return row;
    });
    setAutoExpandHeight();
    wrapTextInColumn(keyCol);
    wrapTextInColumn(valueCol);
  }

  public void setData(Book book) {
    this.book = book;
    setCase();
    setTableData();
  }

  public void setCase() {
    String thumbnailLink = book.getThumbnailLink();
    if (thumbnailLink.isEmpty()) {
      thumbnail.setImage(ImageUtils.getImage("DefaultBookCover.jpg"));
    } else {
      thumbnail.setImage(new Image(thumbnailLink, 0, 0, true, true, true));
    }
    title.setText(book.getTitle());
    authors.setText(book.getAuthorsString() + " · " + book.getPublishedDate());
    publisher.setText(book.getPublisher());
    description.setText(book.getDescription());
    quantity.setText(getQuantity() + "");
    if (getQuantity() == 0) {
      borrowButton.setDisable(true);
    }
  }

  private void tryAddRowToTable(String key, String value) {
    if (!value.isEmpty()) {
      detailsTable.getItems().add(new BookDetail(key, value));
    }
  }

  public void setTableData() {
    tryAddRowToTable("ID", book.getId());
    tryAddRowToTable("Authors", book.getAuthorsString());
    tryAddRowToTable("Published Date", book.getPublishedDate());
    tryAddRowToTable("Publisher", book.getPublisher());
    tryAddRowToTable("Description", book.getDescription());
    tryAddRowToTable("Categories", book.getCategoriesAsString());
    tryAddRowToTable("ISBN-10", book.getIsbn10());
    tryAddRowToTable("ISBN-13", book.getIsbn13());
    tryAddRowToTable("Page Count", book.getPageCount());
    tryAddRowToTable("Ratings Count", book.getRatingsCount());
    tryAddRowToTable("Average Rating", book.getAverageRating());
  }

  private void wrapTextInColumn(TableColumn<BookDetail,String> column) {
    column.setCellFactory(col -> new TableCell<BookDetail,String>() {
      private final Label label = new Label();

      {
        // 1) Make the label wrap and pick up computed height
        label.setWrapText(true);
        label.setFont(Font.font("Candara", 18));
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.setPrefHeight(Region.USE_COMPUTED_SIZE);
        // bind its max width to the *column* width minus some padding
        label.maxWidthProperty().bind(col.widthProperty().subtract(10));

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(label);

        // 2) Bind the *cell’s* minHeight to the label’s height so the row
        //    can expand to fit the wrapped content + any padding
        this.minHeightProperty().bind(label.heightProperty().add(16));
        this.setPrefHeight(Control.USE_COMPUTED_SIZE);
      }

      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
          label.setText(null);
        } else {
          label.setText(item);
        }
      }
    });
  }

  private int getQuantity() {
    for (JsonElement e : JsonUtils.loadLocalJsonAsArray(MainApp.BOOKS_DB_PATH)) {
      Book b = Book.fromJsonObject(e.getAsJsonObject());
      if (b.getId().equals(book.getId())) {
        return b.getQuantity();
      }
    }
    return 0;
  }

  private void setAutoExpandHeight() {
    detailsTable.getItems().addListener((ListChangeListener<BookDetail>) c -> {
      Platform.runLater(() -> {
        double newHeight = detailsTable.prefHeight(detailsTable.getWidth());
        // Only set prefHeight—leave maxHeight alone
        detailsTable.setPrefHeight(newHeight);
      });
    });

    // Initial kick
    Platform.runLater(() -> {
      double newHeight = detailsTable.prefHeight(detailsTable.getWidth());
      detailsTable.setPrefHeight(newHeight);
    });

    // Allow the parent VBox (or AnchorPane) to let this table grow
    // (if you did this in code; otherwise set it in FXML)
    VBox.setVgrow(detailsTable, Priority.ALWAYS);
  }
}

class BookDetail {
  private final SimpleStringProperty key;
  private final SimpleStringProperty value;

  public BookDetail(String key, String value) {
    this.key = new SimpleStringProperty(key);
    this.value = new SimpleStringProperty(value);
  }

  public String getKey() { return key.get(); }
  public String getValue() { return value.get(); }
  public SimpleStringProperty keyProperty() { return key; }
  public SimpleStringProperty valueProperty() { return value; }
}