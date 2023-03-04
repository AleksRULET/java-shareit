package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    User user1 = new User(null, "1a@mail.ru", "John1");
    User user2 = new User(null, "2a@mail.ru", "John2");
    User user3 = new User(null, "3a@mail.ru", "John3");
    User user4 = new User(null, "4a@mail.ru", "John4");
    ItemRequest request1 = new ItemRequest(null, "Описание 1", user1, LocalDateTime.now());
    ItemRequest request2 = new ItemRequest(null, "Описание 2", user2, LocalDateTime.now().minusDays(1));
    Item item1 = new Item(null, user1, "предмет1", "Описание 1", true, request1);
    Item item2 = new Item(null, user2, "предмет2", "Описание 2", true, request2);
    Item item3 = new Item(null, user3, "предмет3", "Описание 3", false, null);

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
    }

    @Test
    void findAllForUser() {
        List<Item> items = itemRepository.findAllForUser(user1, PageRequest.ofSize(5)).getContent();
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 1);
        assertEquals(item.getDescription(), "Описание 1");
        assertEquals(item.getOwner().getName(), "John1");

    }

    @Test
    void search() {
        String text = "Описание 1";
        List<Item> items = itemRepository.search(text, PageRequest.ofSize(5)).getContent();
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 1);
        assertEquals(item.getDescription(), "Описание 1");
        assertEquals(item.getOwner().getName(), "John1");
    }

    @Test
    void findByRequesters() {
        List<ItemRequest> requests = List.of(request1, request2);
        List<Item> items = itemRepository.findByRequesters(requests);
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 2);
        assertEquals(item.getDescription(), "Описание 1");
        assertEquals(item.getOwner().getName(), "John1");
    }

    @Test
    void findByRequester() {
        List<Item> items = itemRepository.findByRequester(request1);
        Item item = items.get(0);
        System.out.println(item.getName() + "  " + item.getId());

        assertEquals(items.size(), 1);
        assertEquals(item.getDescription(), "Описание 1");
        assertEquals(item.getOwner().getName(), "John1");
    }
}
