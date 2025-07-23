package com.application.librarymanagement.utils;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
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

  public static JsonObject loadLocalJson(String name) {
    try {
      Path path = Paths.get(String.format("json/%s.json", name));
      String content = new String(Files.readAllBytes(path));
      JsonObject json = JsonParser.parseString(content).getAsJsonObject();
      assert json != null;
      return json;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getAsString(JsonObject object, String entity) {
    return object.has(entity) ? object.get(entity).getAsString() : null;
  }

  public static List<String> getAsList(JsonObject object, String entity) {
    if (!object.has(entity)) {
      return null;
    }
    List<String> list = new ArrayList<>();
    JsonArray array = object.get(entity).getAsJsonArray();
    for (JsonElement e : array) {
      list.add(e.getAsString());
    }
    return list;
  }

  public static Map<String, String> getAsMap(JsonObject object, String entity, String key, String value) {
    if (!object.has(entity)) {
      return null;
    }
    Map<String, String> map = new HashMap<>();
    JsonArray array = object.get(entity).getAsJsonArray();
    for (JsonElement e : array) {
      JsonObject o = e.getAsJsonObject();
      String k = o.get(key).getAsString();
      String v = o.get(value).getAsString();
      map.put(k, v);
    }
    return map;
  }
  public static void saveLocalJson(Path path, JsonArray array) {
    try (var writer = Files.newBufferedWriter(path)) {
      new GsonBuilder().setPrettyPrinting().create().toJson(array, writer);
    } catch (IOException e) {
      throw new RuntimeException("Lỗi khi ghi JSON vào file: " + path, e);
    }
  }
}
