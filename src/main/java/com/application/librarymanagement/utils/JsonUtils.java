package com.application.librarymanagement.utils;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonUtils {
  private static final int TIMEOUT = 10000;

  /**
   * Sends an HTTP GET to the given URL and parses the response body as JSON.
   * @param urlString the HTTP(S) URL to fetch
   * @return the parsed JsonObject
   * @throws IOException if network or I/O errors occur
   */
  public static JsonObject fetchJson(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Accept", "application/json");
    conn.setConnectTimeout(TIMEOUT);
    conn.setReadTimeout(TIMEOUT);
    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new IOException("Unexpected HTTP response message: " + conn.getResponseMessage());
    }
    StringBuilder sb = new StringBuilder();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }
    } finally {
      conn.disconnect();
    }
    return JsonParser.parseString(sb.toString()).getAsJsonObject();
  }

  public static JsonObject loadLocalJsonAsObject(Path path) {
    try {
      String content = new String(Files.readAllBytes(path));
      JsonObject json = JsonParser.parseString(content).getAsJsonObject();
      assert json != null;
      return json;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static JsonArray loadLocalJsonAsArray(Path path) {
    try {
      String content = new String(Files.readAllBytes(path));
      JsonArray array = JsonParser.parseString(content).getAsJsonArray();
      assert array != null;
      return array;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getAsString(JsonObject object, String key, String defaultValue) {
    return object != null && object.has(key) ? object.get(key).getAsString() : defaultValue;
  }

  public static int getAsInt(JsonObject object, String key, int defaultValue) {
    return object != null && object.has(key) ? object.get(key).getAsInt() : defaultValue;
  }

  public static JsonArray getAsJsonArray(JsonObject object, String key) {
    return object != null && object.has(key) ? object.get(key).getAsJsonArray() : new JsonArray();
  }

  public static String jsonArrayToString(JsonArray array) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.size(); i++) {
      sb.append(array.get(i).getAsString());
      if (i != array.size() - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public static void saveToFile(JsonElement object, Path path) {
    try {
      Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
      Files.writeString(path, gson.toJson(object));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds or updates a property in the given JSON object and writes the
   * resulting JSON back to the specified file path in a pretty-printed format.
   * @param object the {@link JsonObject} to modify
   * @param path   the file system {@link Path} where the JSON should be written
   * @param name   the property name to add or update
   * @param value  the property value to set
   * @throws RuntimeException if an I/O error or JSON serialization error occurs
   */
  public static void addProperty(JsonObject object, Path path, String name, String value) {
    try {
      object.addProperty(name, value);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      Files.writeString(path, gson.toJson(object));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
