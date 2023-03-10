package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.pagination.PageRequestWithOffset;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final Sort sort = Sort.by(Sort.Direction.DESC, "start");

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserService userService;

    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto createBooking(long userId, BookingCreateDto bookingCreateDto) {
        User booker = userService.getUser(userId);
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Нет вещи с id : " + bookingCreateDto.getItemId()));
        Booking booking = bookingMapper.toBooking(bookingCreateDto, item, booker);
        if (booking.getBooker().equals(booking.getItem().getOwner())) {
            throw new EntityNotFoundException("Владелец не может забронировать");
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException(String.format("Вещь id : %s забронирована", item.getId()));
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new DateTimeException(String.format("Дата окончания [%s] должна быть после даты старта [%s]",
                    booking.getEnd(), booking.getStart()));
        }
        Booking createdBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(createdBooking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(long userId, long bookingId, boolean status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Ничего не найдено с id : " + bookingId));
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new IllegalArgumentException("Ошибка изменения статуса");
        }
        User owner = userService.getUser(userId);
        Item item = booking.getItem();
        if (!item.getOwner().equals(owner)) {
            throw new EntityNotFoundException("Сущность не найдена");
        }
        if (status) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking createdBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(createdBooking);
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        User user = userService.getUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Сущность не найдена"));
        Item item = booking.getItem();
        if (!booking.getBooker().equals(user) && !item.getOwner().equals(user)) {
            throw new EntityNotFoundException("Сущность не найдена");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookings(long userId, String stateParam, int from, int size) {
        userService.getUser(userId);
        State state = State.of(stateParam);
        if (Objects.isNull(state)) {
            throw new IllegalArgumentException("Неизвестое состояние: " + stateParam);
        }
        Page<Booking> bookings = Page.empty();
        Pageable pageable = PageRequestWithOffset.of(from, size, sort);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_Id(userId, pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBooker_IdAndStatusInAndStartIsBeforeAndEndIsAfter(userId,
                        List.of(Status.APPROVED, Status.WAITING, Status.REJECTED), LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBooker_IdAndStatusInAndEndIsBefore(userId,
                        List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_IdAndStatusInAndStartIsAfter(userId,
                        List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBooker_IdAndStatus(userId, Status.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBooker_IdAndStatus(userId, Status.REJECTED, pageable);
                break;
        }
        return bookings.map(bookingMapper::toBookingDto).getContent();
    }

    @Override
    public List<BookingDto> getBookingsItemOwner(long userId, String stateParam, int from, int size) {
        userService.getUser(userId);
        State state = State.of(stateParam);
        if (Objects.isNull(state)) {
            throw new IllegalArgumentException("Неизвестое состояние: " + stateParam);
        }
        Page<Booking> bookings = Page.empty();
        Pageable pageable = PageRequestWithOffset.of(from, size, sort);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItem_Owner_Id(userId, pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatusInAndStartIsBeforeAndEndIsAfter(userId,
                        List.of(Status.APPROVED, Status.WAITING, Status.REJECTED), LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatusInAndEndIsBefore(userId,
                        List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatusInAndStartIsAfter(userId,
                        List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(userId, Status.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(userId, Status.REJECTED, pageable);
                break;
        }
        return bookings.map(bookingMapper::toBookingDto).getContent();
    }

}
