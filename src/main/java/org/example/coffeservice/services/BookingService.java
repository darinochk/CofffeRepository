package org.example.coffeservice.services;

import org.example.coffeservice.dto.request.BookingRequestDTO;
import org.example.coffeservice.dto.response.BookingResponseDTO;
import org.example.coffeservice.models.Booking;
import org.example.coffeservice.models.Desk;
import org.example.coffeservice.models.User;
import org.example.coffeservice.models.OrderDetails;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.DeskRepository;
import org.example.coffeservice.repositories.OrderDetailsRepository;
import org.example.coffeservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<BookingResponseDTO> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            return bookings.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения всех бронирований", e);
        }
    }

    public List<BookingResponseDTO> getBookingsByUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не найден");
            }
            List<Booking> bookings = bookingRepository.findByUserId(currentUser.getId());
            return bookings.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения бронирований пользователя", e);
        }
    }

    public List<Booking> getBookingsByDesk(Long deskId) {
        try {
            return bookingRepository.findByDeskId(deskId);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения бронирований для стола с id " + deskId, e);
        }
    }

    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не найден");
            }
            Desk desk = deskRepository.findById(request.getDeskId())
                    .orElseThrow(() -> new IllegalArgumentException("Стол не найден с id " + request.getDeskId()));

            Booking booking = new Booking();
            booking.setUser(currentUser);
            booking.setDesk(desk);
            booking.setStartDate(request.getStartDate());
            booking.setEndDate(request.getEndDate());
            booking.setStatus(request.getStatus());

            if (isDeskAvailable(desk.getId(), request.getStartDate(), request.getEndDate())) {
                booking.setStatus("IS BEING PROCESSED");
                Booking savedBooking = bookingRepository.save(booking);

                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setBooking(savedBooking);
                orderDetails.setAmount(0.0);
                orderDetails.setStatus(null);
                orderDetailsRepository.save(orderDetails);

                return convertToDTO(savedBooking);
            } else {
                throw new IllegalArgumentException("Стол уже забронирован на выбранное время.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания бронирования", e);
        }
    }

    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO request) {
        try {
            Booking existingBooking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено с id " + id));

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не найден");
            }
            if (!existingBooking.getUser().equals(currentUser)) {
                throw new IllegalArgumentException("Вы можете обновлять только свои бронирования.");
            }

            Desk desk = deskRepository.findById(request.getDeskId())
                    .orElseThrow(() -> new IllegalArgumentException("Стол не найден с id " + request.getDeskId()));
            existingBooking.setDesk(desk);
            existingBooking.setStartDate(request.getStartDate());
            existingBooking.setEndDate(request.getEndDate());
            existingBooking.setStatus(request.getStatus());

            Booking updatedBooking = bookingRepository.save(existingBooking);
            return convertToDTO(updatedBooking);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обновления бронирования с id " + id, e);
        }
    }

    public void deleteBooking(Long id) {
        try {
            Booking existingBooking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Бронирование не найдено с id " + id));

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не найден");
            }
            if (!existingBooking.getUser().equals(currentUser)) {
                throw new IllegalArgumentException("Вы можете удалять только свои бронирования.");
            }
            bookingRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления бронирования с id " + id, e);
        }
    }

    private boolean isDeskAvailable(Long deskId, Date startDate, Date endDate) {
        try {
            List<Booking> existingBookings = bookingRepository.findByDeskIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                    deskId, startDate, endDate);
            return existingBookings.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка проверки доступности стола", e);
        }
    }

    public BookingResponseDTO convertToDTO(Booking booking) {
        String userName = booking.getUser().getFirstName() + " " + booking.getUser().getLastName();
        String deskLocation = booking.getDesk().getLocation();
        return new BookingResponseDTO(booking.getId(), userName, deskLocation,
                booking.getStartDate(), booking.getEndDate(), booking.getStatus());
    }
}
