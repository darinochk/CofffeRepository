package org.example.coffeservice.utils;

/** Constants class for application-wide constants. */
public final class Constants {

  private Constants() {
    // Utility class - prevent instantiation
  }

  // Validation constants
  public static final int MIN_DESK_NUMBER = 1;
  public static final int MIN_CAPACITY = 1;
  public static final int MIN_QUANTITY = 1;
  public static final int MIN_RATING = 1;
  public static final int MAX_RATING = 5;
  public static final double MIN_PRICE = 0.0;
  public static final double DEFAULT_AMOUNT = 0.0;
  public static final double DEFAULT_TOTAL_PRICE = 0.0;

  // Payment constants
  public static final double MOCK_ACCOUNT_BALANCE = 1000.0;

  // Status constants
  public static final String STATUS_PENDING = "PENDING";
  public static final String STATUS_CONFIRMED = "CONFIRMED";
  public static final String STATUS_NEW = "NEW";
  public static final String STATUS_SUCCESS = "SUCCESS";
  public static final String STATUS_FAIL = "FAIL";
  public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
  public static final String STATUS_IS_BEING_PROCESSED = "IS BEING PROCESSED";
}
