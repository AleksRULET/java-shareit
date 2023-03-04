package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private BookingRepository bookingRepository;

    User user1 = new User(null, "1@mail.ru", "John1");
    User user2 = new User(null, "2@mail.ru", "John2");
    User user3 = new User(null, "3@mail.ru", "John3");
    User user4 = new User(null, "4@mail.ru", "John4");
    ItemRequest request1 = new ItemRequest(null, "Описание 1", user1, LocalDateTime.now());
    ItemRequest request2 = new ItemRequest(null, "Описание 1", user2, LocalDateTime.now().minusDays(1));
    Item item1 = new Item(null, user1, "предмет1", "Описание 1", true, request1);
    Item item2 = new Item(null, user2, "предмет2", "Описание 2", true, request2);
    Item item3 = new Item(null, user3, "предмет3", "Описание 3", false, null);

    Booking booking1 = new Booking(
            null,
            LocalDateTime.now().minusDays(6),
            LocalDateTime.now().minusDays(5),
            item1,
            user4,
            Status.APPROVED);
    Booking booking2 = new Booking(
            null,
            LocalDateTime.now().plusDays(3),
            LocalDateTime.now().plusDays(4),
            item2,
            user1,
            Status.WAITING);
    Booking booking3 = new Booking(
            null,
            LocalDateTime.now().minusDays(4),
            LocalDateTime.now().minusDays(3),
            item1,
            user3,
            Status.REJECTED);
    Booking booking4 = new Booking(
            null,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            item2,
            user4,
            Status.APPROVED);
    Booking booking5 = new Booking(
            null,
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().plusDays(1),
            item2,
            user3,
            Status.APPROVED);

    @BeforeEach
    void addItem() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        requestRepository.save(request1);
        requestRepository.save(request2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        bookingRepository.save(booking5);
    }

    @Test
    void findAllOwner() {
        List<Booking> bookings = bookingRepository.findAllOwner(user1, PageRequest.ofSize(5)).getContent();
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getBooker().getName(), "John3");
        assertEquals(booking.getItem().getName(), "предмет1");
    }

    @Test
    void findCurrentOwner() {
        List<Booking> bookings = bookingRepository.findCurrentOwner(user2, PageRequest.ofSize(5)).getContent();
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getBooker().getName(), "John3");
        assertEquals(booking.getItem().getName(), "предмет2");
    }

    @Test
    void findPastOwner() {
        List<Booking> bookings = bookingRepository.findPastOwner(user1, PageRequest.ofSize(5)).getContent();
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getBooker().getName(), "John3");
        assertEquals(booking.getItem().getName(), "предмет1");
    }

    @Test
    void findFutureOwner() {
        List<Booking> bookings = bookingRepository.findFutureOwner(user2, PageRequest.ofSize(5)).getContent();
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getBooker().getName(), "John1");
        assertEquals(booking.getItem().getName(), "предмет2");
    }

    @Test
    void findStatusOwner() {
        List<Booking> bookings = bookingRepository.findStatusOwner(user2,
                Status.WAITING, PageRequest.ofSize(5)).getContent();
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getBooker().getName(), "John1");
        assertEquals(booking.getItem().getName(), "предмет2");
    }

    @Test
    void findItemByOwner() {
        List<Booking> bookings = bookingRepository.findItemByOwner(user1, item1.getId(),
                Status.APPROVED);
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getBooker().getName(), "John4");
        assertEquals(booking.getItem().getName(), "предмет1");
    }

    @Test
    void findAllItemsByOwner() {
        List<Booking> bookings = bookingRepository.findAllItemsByOwner(user2, Status.APPROVED);
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 2);
        assertEquals(booking.getBooker().getName(), "John4");
        assertEquals(booking.getItem().getName(), "предмет2");
    }

    @Test
    void findByBooker_IdAndItem_Id() {
        List<Booking> bookings = bookingRepository.findByBooker_IdAndItem_Id(user3, item2.getId(), Status.APPROVED);
        Booking booking = bookings.get(0);

        assertEquals(bookings.size(), 1);
        assertEquals(booking.getBooker().getName(), "John3");
        assertEquals(booking.getItem().getName(), "предмет2");
    }
}
