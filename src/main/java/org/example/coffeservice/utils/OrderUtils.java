package org.example.coffeservice.utils;

import org.example.coffeservice.models.coffee.Order;

/** Utility class for order-related calculations. */
public final class OrderUtils {

  private OrderUtils() {
    // Utility class - prevent instantiation
  }

  /**
   * Calculates the total price for an order.
   *
   * @param order the order to calculate price for
   * @return the total price (quantity * food price)
   */
  public static double calculateTotalPrice(Order order) {
    if (order == null || order.getFood() == null) {
      return Constants.DEFAULT_TOTAL_PRICE;
    }
    return order.getQuantity() * order.getFood().getPrice();
  }

  /**
   * Calculates the total amount for a list of orders.
   *
   * @param orders the list of orders
   * @return the total amount
   */
  public static double calculateTotalAmount(java.util.List<Order> orders) {
    return orders.stream()
        .mapToDouble(order -> order.getQuantity() * order.getFood().getPrice())
        .sum();
  }
}
