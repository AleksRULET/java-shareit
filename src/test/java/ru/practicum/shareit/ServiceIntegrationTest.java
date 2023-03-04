package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRefundDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ServiceIntegrationTest {
    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;
    @Autowired
    ItemService itemService;
    @Autowired
    BookingService bookingService;

    UserRefundDto userRefund1;
    UserRefundDto userRefund2;
    UserRefundDto userRefund3;
    UserRefundDto userRefund4;
    ItemRequestRefundDto requestRefundDto1;
    ItemRequestRefundDto requestRefundDto2;
    ItemRefundDto itemRefund1;
    ItemRefundDto itemRefund2;
    ItemRefundDto itemRefund3;
    BookingItemDto bookingRefund1;
    BookingItemDto bookingRefund2;
    BookingItemDto bookingRefund3;
    BookingItemDto bookingRefund4;
    BookingItemDto bookingRefund5;
    CommentRefundDto commentRefund1;
    CommentRefundDto commentRefund2;

    @BeforeEach
    void addAllData() {
        UserDto user1 = new UserDto("1a@mail.ru", "John1");
        UserDto user2 = new UserDto("2a@mail.ru", "John2");
        UserDto user3 = new UserDto("3a@mail.ru", "John3");
        UserDto user4 = new UserDto("4a@mail.ru", "John4");
        userRefund1 = userService.save(user1);
        userRefund2 = userService.save(user2);
        userRefund3 = userService.save(user3);
        userRefund4 = userService.save(user4);

        ItemRequestDto request1 = new ItemRequestDto("нужен предмет 1");
        ItemRequestDto request2 = new ItemRequestDto("нужен предмет 2");
        requestRefundDto1 = requestService.add(userRefund2.getId(), request1);
        requestRefundDto2 = requestService.add(userRefund1.getId(), request2);

        ItemDto item1 = new ItemDto("предмет 1", "описание 1", true, requestRefundDto1.getId());
        ItemDto item2 = new ItemDto("предмет 2", "описание 2", true, requestRefundDto2.getId());
        ItemDto item3 = new ItemDto("предмет 3", "описание 3", false, null);
        itemRefund1 = itemService.add(userRefund1.getId(), item1);
        itemRefund2 = itemService.add(userRefund2.getId(), item2);
        itemRefund3 = itemService.add(userRefund2.getId(), item3);

        BookingDto booking1 = new BookingDto(
                LocalDateTime.now().minusDays(6), LocalDateTime.now().minusDays(5), itemRefund1.getId());
        BookingDto booking2 = new BookingDto(
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), itemRefund2.getId());
        BookingDto booking3 = new BookingDto(
                LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(3), itemRefund1.getId());
        BookingDto booking4 = new BookingDto(
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), itemRefund2.getId());
        BookingDto booking5 = new BookingDto(
                LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(1), itemRefund2.getId());
        bookingRefund1 = bookingService.add(userRefund4.getId(), booking1);
        bookingRefund2 = bookingService.add(userRefund1.getId(), booking2);
        bookingRefund3 = bookingService.add(userRefund3.getId(), booking3);
        bookingRefund4 = bookingService.add(userRefund4.getId(), booking4);
        bookingRefund5 = bookingService.add(userRefund3.getId(), booking5);
        bookingService.approved(userRefund1.getId(), bookingRefund1.getId(), true);
        bookingService.approved(userRefund1.getId(), bookingRefund3.getId(), true);
        bookingService.approved(userRefund2.getId(), bookingRefund2.getId(), false);

        CommentDto comment1 = new CommentDto(null, "отличный предмет 1");
        CommentDto comment2 = new CommentDto(null, "отличный предмет 2");
        commentRefund1 = itemService.addComment(userRefund3.getId(), itemRefund1.getId(), comment1);
        commentRefund2 = itemService.addComment(userRefund4.getId(), itemRefund1.getId(), comment2);
    }

    @Test
    void testAllData() {
        assertEquals(userRefund1.getName(), "John1");
        assertEquals(userRefund1.getEmail(), "1a@mail.ru");
        assertEquals(userRefund2.getName(), "John2");
        assertEquals(userRefund2.getEmail(), "2a@mail.ru");
        assertEquals(userRefund3.getName(), "John3");
        assertEquals(userRefund3.getEmail(), "3a@mail.ru");
        assertEquals(userRefund4.getName(), "John4");
        assertEquals(userRefund4.getEmail(), "4a@mail.ru");

        assertEquals(requestRefundDto1.getDescription(), "нужен предмет 1");
        assertEquals(requestRefundDto2.getDescription(), "нужен предмет 2");

        assertEquals(itemRefund1.getName(), "предмет 1");
        assertEquals(itemRefund1.getDescription(), "описание 1");
        assertEquals(itemRefund1.getAvailable(), true);
        assertEquals(itemRefund2.getName(), "предмет 2");
        assertEquals(itemRefund2.getDescription(), "описание 2");
        assertEquals(itemRefund2.getAvailable(), true);
        assertEquals(itemRefund3.getName(), "предмет 3");
        assertEquals(itemRefund3.getDescription(), "описание 3");
        assertEquals(itemRefund3.getAvailable(), false);
        assertNull(itemRefund3.getRequestId());

        assertEquals(bookingRefund1.getStatus(), Status.WAITING);
        assertEquals(bookingRefund1.getBooker().getName(), "John4");
        assertEquals(bookingRefund1.getItem().getName(), "предмет 1");
        assertEquals(bookingRefund2.getStatus(), Status.WAITING);
        assertEquals(bookingRefund2.getBooker().getName(), "John1");
        assertEquals(bookingRefund2.getItem().getName(), "предмет 2");
        assertEquals(bookingRefund3.getStatus(), Status.WAITING);
        assertEquals(bookingRefund3.getBooker().getName(), "John3");
        assertEquals(bookingRefund3.getItem().getName(), "предмет 1");
        assertEquals(bookingRefund4.getStatus(), Status.WAITING);
        assertEquals(bookingRefund4.getBooker().getName(), "John4");
        assertEquals(bookingRefund4.getItem().getName(), "предмет 2");
        assertEquals(bookingRefund5.getStatus(), Status.WAITING);
        assertEquals(bookingRefund5.getBooker().getName(), "John3");
        assertEquals(bookingRefund5.getItem().getName(), "предмет 2");
    }

    @Test
    void item_findAllForUser() {
        List<ItemBookingDto> items = itemService.findAllForUser(0, 5, userRefund1.getId());
        ItemBookingDto item = items.get(0);

        assertEquals(items.size(), 1);
        assertEquals(item.getId(), itemRefund1.getId());
        assertEquals(item.getName(), "предмет 1");
        assertEquals(item.getDescription(), "описание 1");
        assertEquals(item.getComments().size(), 2);
        assertEquals(item.getLastBooking().getId(), bookingRefund3.getId());
        assertNull(item.getNextBooking());
        assertTrue(item.getAvailable());
    }

    @Test
    void item_search() {
        List<ItemRefundDto> items = itemService.search("описание 2", 0, 5);
        ItemRefundDto item = items.get(0);

        assertEquals(items.size(), 1);
        assertEquals(item.getName(), "предмет 2");
        assertTrue(item.getAvailable());
        assertEquals(item.getDescription(), "описание 2");
    }

    @Test
    void booking_findAllForUser_All() {
        List<BookingItemDto> bookings = bookingService
                .findAllForUser(userRefund3.getId(), State.ALL, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getId(), bookingRefund5.getId());
    }

    @Test
    void booking_findAllForUser_CURRENT() {
        List<BookingItemDto> bookings = bookingService
                .findAllForUser(userRefund3.getId(), State.CURRENT, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getId(), bookingRefund5.getId());
    }

    @Test
    void booking_findAllForUser_PAST() {
        List<BookingItemDto> bookings = bookingService
                .findAllForUser(userRefund4.getId(), State.PAST, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getId(), bookingRefund1.getId());
    }

    @Test
    void booking_findAllForUser_FUTURE() {
        List<BookingItemDto> bookings = bookingService
                .findAllForUser(userRefund4.getId(), State.FUTURE, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getId(), bookingRefund4.getId());
    }

    @Test
    void booking_findAllForUser_WAITING() {
        List<BookingItemDto> bookings = bookingService
                .findAllForUser(userRefund3.getId(), State.WAITING, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getId(), bookingRefund5.getId());

    }

    @Test
    void booking_findAllForUser_REJECTED() {
        List<BookingItemDto> bookings = bookingService
                .findAllForUser(userRefund1.getId(), State.REJECTED, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getId(), bookingRefund2.getId());

    }

    @Test
    void booking_findAllForOwner_All() {
        List<BookingItemDto> bookings = bookingService
                .findAllForOwner(userRefund2.getId(), State.ALL, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 3);
        assertEquals(booking.getId(), bookingRefund2.getId());
    }

    @Test
    void booking_findAllForOwner_CURRENT() {
        List<BookingItemDto> bookings = bookingService
                .findAllForOwner(userRefund2.getId(), State.CURRENT, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getId(), bookingRefund5.getId());
    }

    @Test
    void booking_findAllForOwner_PAST() {
        List<BookingItemDto> bookings = bookingService
                .findAllForOwner(userRefund1.getId(), State.PAST, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getId(), bookingRefund3.getId());
    }

    @Test
    void booking_findAllForOwner_FUTURE() {
        List<BookingItemDto> bookings = bookingService
                .findAllForOwner(userRefund2.getId(), State.FUTURE, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getId(), bookingRefund2.getId());
    }

    @Test
    void booking_findAllForOwner_WAITING() {
        List<BookingItemDto> bookings = bookingService
                .findAllForOwner(userRefund2.getId(), State.WAITING, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getId(), bookingRefund4.getId());

    }

    @Test
    void booking_findAllForOwner_REJECTED() {
        List<BookingItemDto> bookings = bookingService
                .findAllForOwner(userRefund2.getId(), State.REJECTED, 0, 5);
        BookingItemDto booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getId(), bookingRefund2.getId());

    }

}