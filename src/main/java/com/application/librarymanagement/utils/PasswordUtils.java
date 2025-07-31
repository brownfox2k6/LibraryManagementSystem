package com.application.librarymanagement.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for handling password-related operations such as hashing.
 * <p>
 * Provides a method to securely hash passwords using the SHA-256 algorithm.
 */
public class PasswordUtils {

  /**
   * Hashes the given password using the SHA-256 cryptographic hash function.
   * <p>
   * The resulting hash is returned as a lowercase hexadecimal string.
   * <p>
   * Note: SHA-256 is a one-way hash function and cannot be reversed to retrieve the original password.
   *
   * @param password the plain text password to be hashed
   * @return the SHA-256 hashed value of the password in hexadecimal format
   * @throws RuntimeException if the SHA-256 algorithm is not available (very unlikely in standard Java)
   */
  public static String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hash = md.digest(password.getBytes());
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        hexString.append(String.format("%02x", b));
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Hashing failed", e);
    }
  }
}
