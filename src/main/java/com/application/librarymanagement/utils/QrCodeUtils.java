package com.application.librarymanagement.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class QrCodeUtils {
  public static ImageView createQrCode(String text, int size) {
    try {
      QRCodeWriter writer = new QRCodeWriter();
      Map<EncodeHintType, Object> hints = new HashMap<>();
      hints.put(EncodeHintType.MARGIN, 1);
      BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints);
      WritableImage img = new WritableImage(size, size);
      PixelWriter pw = img.getPixelWriter();
      for (int x = 0; x < size; x++) {
        for (int y = 0; y < size; y++) {
          pw.setColor(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
        }
      }
      return new ImageView(img);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
