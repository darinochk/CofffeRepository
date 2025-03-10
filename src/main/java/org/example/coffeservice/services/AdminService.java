package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.user.UserRequestDTO;
import org.example.coffeservice.dto.response.coffee.*;
import org.example.coffeservice.dto.response.user.UserResponseDTO;
import org.example.coffeservice.models.coffee.*;
import org.example.coffeservice.models.user.Role;
import org.example.coffeservice.models.user.User;
import org.example.coffeservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<BookingResponseDTO> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            return bookings.stream()
                    .map(this::convertBookingToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all bookings", e);
        }
    }

    public List<DeskResponseDTO> getAllDesks() {
        try {
            List<Desk> desks = deskRepository.findAll();
            return desks.stream()
                    .map(this::convertDeskToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all desks", e);
        }
    }

    public List<UserResponseDTO> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::convertUserToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all users", e);
        }
    }

    public void deleteBooking(Long id) {
        try {
            bookingRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting booking with id " + id, e);
        }
    }

    public void deleteDesk(Long id) {
        try {
            deskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting desk with id " + id, e);
        }
    }

    public UserResponseDTO createAdmin(UserRequestDTO userRequest) {
        try {
            System.out.println("Received request to create user with role: " + userRequest.getRole());

            if (userRequest.getRole() == null) {
                throw new IllegalArgumentException("Role cannot be null");
            }

            Role role;
            if ("ADMIN".equals(userRequest.getRole())) {
                role = Role.ADMIN;
            } else if ("VISITOR".equals(userRequest.getRole())) {
                role = Role.VISITOR;
            } else {
                throw new IllegalArgumentException("Invalid role provided: " + userRequest.getRole());
            }

            User user = new User();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            user.setPhone(userRequest.getPhone());
            user.setLocked(userRequest.isLocked());
            user.setRole(role);

            System.out.println("Assigned role to user: " + user.getRole());

            User savedUser = userRepository.save(user);

            return convertUserToDTO(savedUser);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided: " + userRequest.getRole(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error creating admin", e);
        }
    }


    private boolean isValidRole(String roleString) {
        try {
            Role.valueOf(roleString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public UserResponseDTO addUser(User user) {
        try {
            user.setRole(Role.VISITOR);
            User savedUser = userRepository.save(user);
            return convertUserToDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error adding new user", e);
        }
    }

    public UserResponseDTO updateUser(Long id, User user) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhone(user.getPhone());
            User updatedUser = userRepository.save(existingUser);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user with id " + id, e);
        }
    }

    public UserResponseDTO assignRoleToUser(Long userId, String role) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));

            Role newRole = Role.valueOf(role.toUpperCase());
            user.setRole(newRole);

            User updatedUser = userRepository.save(user);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error assigning role to user with id " + userId, e);
        }
    }


    public UserResponseDTO blockUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            user.setLocked(true);
            User updatedUser = userRepository.save(user);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error blocking user with id " + id, e);
        }
    }

    public UserResponseDTO unblockUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
            user.setLocked(false);
            User updatedUser = userRepository.save(user);
            return convertUserToDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error unblocking user with id " + id, e);
        }
    }

    public ReviewResponseDTO moderateReview(Long reviewId, String newText) {
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException("Review not found with id " + reviewId));
            review.setReviewText(newText);
            Review updatedReview = reviewRepository.save(review);
            return convertReviewToDTO(updatedReview);
        } catch (Exception e) {
            throw new RuntimeException("Error moderating review with id " + reviewId, e);
        }
    }

    public BookingResponseDTO confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + bookingId));

        booking.setStatus("CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        return convertToDTO(savedBooking);
    }

    public OrderDetailsResponseDTO confirmOrderDetails(Long orderDetailsId) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailsId)
                .orElseThrow(() -> new IllegalArgumentException("OrderDetails not found with id " + orderDetailsId));
        orderDetails.setStatus("CONFIRMED");

        List<Order> orders = orderRepository.findByOrderDetailsId(orderDetailsId);
        double totalAmount = orders.stream()
                .mapToDouble(order -> order.getQuantity() * order.getFood().getPrice())
                .sum();
        orderDetails.setAmount(totalAmount);
        OrderDetails savedDetails = orderDetailsRepository.save(orderDetails);

        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());

        return new OrderDetailsResponseDTO(savedDetails.getId(), savedDetails.getAmount(), orderDTOs);
    }

    private BookingResponseDTO convertBookingToDTO(Booking booking) {
        String userName = booking.getUser().getFirstName() + " " + booking.getUser().getLastName();
        String deskLocation = booking.getDesk().getLocation();
        return new BookingResponseDTO(booking.getId(), userName, deskLocation,
                booking.getStartDate(), booking.getEndDate(), booking.getStatus());
    }

    private DeskResponseDTO convertDeskToDTO(Desk desk) {
        return new DeskResponseDTO(desk.getId(), desk.getDeskNumber(), desk.getCapacity(), desk.getLocation());
    }

    private UserResponseDTO convertUserToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .locked(user.isLocked())
                .build();
    }

    private ReviewResponseDTO convertReviewToDTO(Review review) {
        String userName = review.getUser().getFirstName() + " " + review.getUser().getLastName();
        return new ReviewResponseDTO(review.getId(), userName, review.getRating(), review.getReviewText(), review.getReviewDate());
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getFood().getName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDetails().getId()
        );
    }

    private BookingResponseDTO convertToDTO(Booking booking) {
        String userName = booking.getUser().getFirstName() + " " + booking.getUser().getLastName();
        String deskLocation = booking.getDesk().getLocation();
        return new BookingResponseDTO(
                booking.getId(),
                userName,
                deskLocation,
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus()
        );
    }

}
