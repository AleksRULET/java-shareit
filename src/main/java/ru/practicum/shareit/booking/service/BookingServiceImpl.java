package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.excepction.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingDao;
    private final UserRepository userDao;
    private final ItemRepository itemDao;

    @Transactional
    @Override
    public BookingItemDto add(Long userId, BookingDto bookingDto) {
        Item item = itemDao.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Вещь не найдена"));
        if (!item.getIsAvailable()) {
            throw new NotAvailableException("Нет доступа");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new NotBookingException("Пользователь не может бронировать свою вещь");
        }
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        Booking booking = BookingMapper.toBooking(user, item, bookingDto);
        booking.setStatus(Status.WAITING);
        return BookingMapper.toBookingItemDto(bookingDao.save(booking));
    }

    @Transactional
    @Override
    public BookingItemDto approved(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingDao.findById(bookingId).orElseThrow(() -> new NotBookingException("Запрос отстутствует"));
        if (booking.getStatus() != Status.WAITING) {
            throw new StatusConfirmedException("Ответ по бронированию уже был");
        }
        if (userId.equals(booking.getItem().getOwner().getId())) {
            Status status;
            if (approved) {
                status = Status.APPROVED;
            } else {
                status = Status.REJECTED;
            }
            booking.setStatus(status);
            return BookingMapper.toBookingItemDto(booking);
        } else {
            throw new NotOwnerException("Доступно только владельцу");
        }
    }

    @Override
    public BookingItemDto findById(Long userId, Long bookingId) {
        Booking booking = bookingDao.findById(bookingId).orElseThrow(() -> new ObjectNotFoundException("Брони нет"));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingItemDto(booking);
        } else {
            throw new NotOwnerException("Доступно только владельцу");
        }
    }

    @Override
    public List<BookingItemDto> findAllForUser(Long userId, State state) {
        userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingDao.findByBooker_IdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingDao.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingDao.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingDao.findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingDao.findByBooker_IdAndStatus(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingDao.findByBooker_IdAndStatus(userId, Status.REJECTED);
                break;
        }
        return BookingMapper.toBookingsItemDto(bookings);
    }

    @Override
    public List<BookingItemDto> findAllForOwner(Long userId, State state) {
        userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingDao.findAllOwner(userId);
                break;
            case CURRENT:
                bookings = bookingDao.findCurrentOwner(userId);
                break;
            case PAST:
                bookings = bookingDao.findPastOwner(userId);
                break;
            case FUTURE:
                bookings = bookingDao.findFutureOwner(userId);
                break;
            case WAITING:
                bookings = bookingDao.findStatusOwner(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingDao.findStatusOwner(userId, Status.REJECTED);
                break;
        }
        return BookingMapper.toBookingsItemDto(bookings);
    }
}
