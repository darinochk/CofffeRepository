package org.example.coffeservice.utils;

import org.example.coffeservice.models.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for security-related operations.
 */
public final class SecurityUtils {

  private SecurityUtils() {
    // Utility class - prevent instantiation
  }

  /**
   * Gets the current authenticated user from SecurityContext.
   *
   * @return the current User
   * @throws IllegalArgumentException if user is not found or not authenticated
   */
  public static User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new IllegalArgumentException("User not authenticated");
    }
    return (User) authentication.getPrincipal();
  }

  /**
   * Gets the current user's email from SecurityContext.
   *
   * @return the current user's email
   */
  public static String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new IllegalArgumentException("User not authenticated");
    }
    return authentication.getName();
  }
}

