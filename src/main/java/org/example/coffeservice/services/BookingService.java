package org.example.coffeservice.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.example.coffeservice.dto.request.coffee.BookingRequestDTO;
import org.example.coffeservice.dto.response.coffee.BookingResponseDTO;
import org.example.coffeservice.models.coffee.Booking;
import org.example.coffeservice.models.coffee.Desk;
import org.example.coffeservice.models.user.User;
import org.example.coffeservice.repositories.BookingRepository;
import org.example.coffeservice.repositories.DeskRepository;
import org.example.coffeservice.utils.Constants;
import org.example.coffeservice.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

  @Autowired private BookingRepository bookingRepository;

  @Autowired private DeskRepository deskRepository;

  public List<BookingResponseDTO> getAllBookings() {
    try {
      List<Booking> bookings = bookingRepository.findAll();
      return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения всех бронирований", exception);
    }
  }

  public List<BookingResponseDTO> getBookingsByUser() {
    try {
      User currentUser = SecurityUtils.getCurrentUser();
      List<Booking> bookings = bookingRepository.findByUserId(currentUser.getId());
      return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка получения бронирований пользователя", exception);
    }
  }

  public List<BookingResponseDTO> getBookingsByDesk(Long deskId) {
    try {
      List<Booking> bookings = bookingRepository.findByDeskId(deskId);
      return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    } catch (Exception exception) {
      throw new RuntimeException(
          "Ошибка получения бронирований для стола с id " + deskId, exception);
    }
  }

  public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
    try {
      User currentUser = SecurityUtils.getCurrentUser();
      Desk desk =
          deskRepository
              .findById(bookingRequest.getDeskId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Стол не найден с id " + bookingRequest.getDeskId()));

      if (!isDeskAvailable(
          desk.getId(), bookingRequest.getStartDate(), bookingRequest.getEndDate())) {
        throw new IllegalArgumentException("Стол уже забронирован на выбранное время.");
      }

      Booking booking = new Booking();
      booking.setUser(currentUser);
      booking.setDesk(desk);
      booking.setStartDate(bookingRequest.getStartDate());
      booking.setEndDate(bookingRequest.getEndDate());
      booking.setStatus(Constants.STATUS_IS_BEING_PROCESSED);

      Booking savedBooking = bookingRepository.save(booking);

      return convertToDTO(savedBooking);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка создания бронирования", exception);
    }
  }

  public BookingResponseDTO updateBooking(Long id, BookingRequestDTO bookingRequest) {
    try {
      Booking existingBooking =
          bookingRepository
              .findById(id)
              .orElseThrow(
                  () -> new IllegalArgumentException("Бронирование не найдено с id " + id));

      User currentUser = SecurityUtils.getCurrentUser();
      if (!existingBooking.getUser().equals(currentUser)) {
        throw new IllegalArgumentException("Вы можете обновлять только свои бронирования.");
      }

      Desk desk =
          deskRepository
              .findById(bookingRequest.getDeskId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Стол не найден с id " + bookingRequest.getDeskId()));
      existingBooking.setDesk(desk);
      existingBooking.setStartDate(bookingRequest.getStartDate());
      existingBooking.setEndDate(bookingRequest.getEndDate());
      existingBooking.setStatus(bookingRequest.getStatus());

      Booking updatedBooking = bookingRepository.save(existingBooking);
      return convertToDTO(updatedBooking);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка обновления бронирования с id " + id, exception);
    }
  }

  public void deleteBooking(Long id) {
    try {
      Booking existingBooking =
          bookingRepository
              .findById(id)
              .orElseThrow(
                  () -> new IllegalArgumentException("Бронирование не найдено с id " + id));

      User currentUser = SecurityUtils.getCurrentUser();
      if (!existingBooking.getUser().equals(currentUser)) {
        throw new IllegalArgumentException("Вы можете удалять только свои бронирования.");
      }
      bookingRepository.deleteById(id);
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка удаления бронирования с id " + id, exception);
    }
  }

  private boolean isDeskAvailable(Long deskId, Date startDate, Date endDate) {
    try {
      List<Booking> existingBookings =
          bookingRepository.findByDeskIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
              deskId, startDate, endDate);
      return existingBookings.isEmpty();
    } catch (Exception exception) {
      throw new RuntimeException("Ошибка проверки доступности стола", exception);
    }
  }

  public BookingResponseDTO convertToDTO(Booking booking) {
    String userName = booking.getUser().getFirstName() + " " + booking.getUser().getLastName();
    String deskLocation = booking.getDesk().getLocation();
    return new BookingResponseDTO(
        booking.getId(),
        userName,
        deskLocation,
        booking.getStartDate(),
        booking.getEndDate(),
        booking.getStatus());
  }
}
