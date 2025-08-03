package com.application.librarymanagement.book;

import com.application.librarymanagement.MainApp;
import com.application.librarymanagement.inapp.InAppController;
import com.application.librarymanagement.user.User;
import com.application.librarymanagement.utils.ImageUtils;
import com.application.librarymanagement.utils.JsonUtils;
import com.application.librarymanagement.utils.QrCodeUtils;
import com.google.gson.JsonElement;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

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
  @FXML private Button showQrCodeButton;
  @FXML private Button copyLinkToClipboardButton;
  @FXML private Button changeQuantityButton;
  @FXML private Text changeQuantityText;
  @FXML private Spinner<Integer> changeQuantitySpinner;

  private Book book;
  private User user;

  @FXML
  public void initialize() {
    keyCol.setCellValueFactory(cd -> cd.getValue().keyProperty());
    valueCol.setCellValueFactory(cd -> cd.getValue().valueProperty());
    keyCol.setPrefWidth(145);
    valueCol.setPrefWidth(955);
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
    user = InAppController.getCurrentUser();
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
    if (user.getUserType() == User.TYPE_ADMIN || getQuantity() == 0) {
      makeNodeDisappear(borrowButton);
    }
    if (book.getInfoLink().isEmpty()) {
      makeNodeDisappear(copyLinkToClipboardButton);
      makeNodeDisappear(showQrCodeButton);
    }
    if (user.getUserType() == User.TYPE_MEMBER) {
      makeNodeDisappear(changeQuantityText);
      makeNodeDisappear(changeQuantitySpinner);
      makeNodeDisappear(changeQuantityButton);
    } else {
      changeQuantitySpinner.setValueFactory(
          new SpinnerValueFactory.IntegerSpinnerValueFactory(-getQuantity(), Integer.MAX_VALUE, 1));
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
    tryAddRowToTable("Publisher", book.getPublisher());
    tryAddRowToTable("Published on", book.getPublishedDate());
    tryAddRowToTable("Pages", book.getPageCount());
    tryAddRowToTable("Language", book.getLanguage());
    tryAddRowToTable("Categories", book.getCategoriesAsString());
    tryAddRowToTable("ISBN 10", book.getIsbn10());
    tryAddRowToTable("ISBN 13", book.getIsbn13());
    tryAddRowToTable("Description", book.getDescription());
    tryAddRowToTable("Ratings count", book.getRatingsCount());
    tryAddRowToTable("Average rating", book.getAverageRating());
  }

  @FXML
  private void openQrCodeWindow() {
    ImageView imageView = QrCodeUtils.createQrCode(book.getInfoLink(), 300);
    imageView.setFitHeight(300);
    imageView.setFitWidth(300);
    imageView.setPreserveRatio(true);
    StackPane root = new StackPane(imageView);
    root.setStyle("-fx-padding:20; -fx-background-color:white;");
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setTitle("Scan QR to open link");
    stage.setScene(new Scene(root));
    stage.show();
  }

  @FXML
  private void copyLinkToClipboard() {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putUrl(book.getInfoLink());
    clipboard.setContent(content);
    showPopupMessage("Link copied to clipboard!");
  }

  @FXML
  private void changeQuantity() {
    int amount = changeQuantitySpinner.getValue();
    if (amount == 0) {
      return;
    }
    book.adjustQuantity(amount);
    book.updateToDatabase();
    quantity.setText(book.getQuantity() + "");
    changeQuantitySpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(-getQuantity(), Integer.MAX_VALUE, amount));
    String fmt = amount > 0 ? "Book(s) added: +%d (%d → %d)" : "Book(s) removed: %d (%d → %d)";
    showPopupMessage(String.format(fmt, amount, book.getQuantity() - amount, book.getQuantity()));
  }

  private void showPopupMessage(String message) {
    Label label = new Label(message);
    label.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
    StackPane pane = new StackPane(label);
    pane.setBackground(new Background(new BackgroundFill(
        Color.rgb(6,64,43,0.75), new CornerRadii(5), Insets.EMPTY)));
    pane.setPadding(new Insets(10));
    pane.setOpacity(0.9);
    Popup popup = new Popup();
    popup.getContent().add(pane);
    popup.setAutoFix(true);
    popup.setAutoHide(true);
    popup.setHideOnEscape(true);
    Window window = detailsTable.getScene().getWindow();
    double centerX = window.getX() + window.getWidth()  / 2.0
        - pane.prefWidth(-1) / 2.0;
    double centerY = window.getY() + window.getHeight() / 2.0
        - pane.prefHeight(-1) / 2.0;
    popup.show(window, centerX, centerY);
    PauseTransition wait = new  PauseTransition(Duration.seconds(3));
    wait.setOnFinished(e -> popup.hide());
    wait.play();
  }

  private void makeNodeDisappear(Node node) {
    node.setVisible(false);
    node.setManaged(false);
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