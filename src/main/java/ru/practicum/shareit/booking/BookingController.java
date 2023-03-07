package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validation.ValidPage;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingItemDto add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody BookingDto bookingDto
    ) {
        return service.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingItemDto approved(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        return service.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingItemDto findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId
    ) {
        return service.findById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingItemDto> findAllForUser(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        State bookingState = validate(state);
        int page = ValidPage.page(from, size);
        return service.findAllForUser(userId, bookingState, page, size);
    }

    @GetMapping("/owner")
    public List<BookingItemDto> findAllForOwner(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        State bookingState = validate(state);
        int page = ValidPage.page(from, size);
        return service.findAllForOwner(userId, bookingState, page, size);
    }

    private State validate(String state) {
        State bookingState = null;
        for (State value : State.values()) {
            if (value.name().equals(state)) {
                bookingState = value;
            }
        }
        if (bookingState == null) {
            throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingState;
    }
}
