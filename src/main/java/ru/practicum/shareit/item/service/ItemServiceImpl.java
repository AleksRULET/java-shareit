package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRefundDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.excepction.CommentException;
import ru.practicum.shareit.excepction.ObjectNotFoundException;
import ru.practicum.shareit.excepction.NotOwnerException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemDao;
    private final UserRepository userDao;
    private final CommentRepository commentDao;
    private final BookingRepository bookingDao;

    @Transactional
    @Override
    public ItemRefundDto add(long userId, ItemDto itemDto) {
        User user = userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemRefundDto(itemDao.save(item));
    }

    @Transactional
    @Override
    public ItemRefundDto edit(long userId, long itemId, ItemDto itemDto) {
        Item item = itemDao.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Записб не найдена"));
        if (item.getOwner().getId() != userId) {
            throw new NotOwnerException("Вы не явдяетесь владельцем записи");
        }
        User user = userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        Item itemNew = ItemMapper.toItem(itemDto, user);
        if (itemNew.getName() != null && !itemNew.getName().isBlank()) {
            item.setName(itemNew.getName());
        }
        if (itemNew.getDescription() != null && !itemNew.getDescription().isBlank()) {
            item.setDescription(itemNew.getDescription());
        }
        if (itemNew.getIsAvailable() != null) {
            item.setIsAvailable(itemNew.getIsAvailable());
        }
        return ItemMapper.toItemRefundDto(item);
    }

    @Override
    public ItemBookingDto findById(long userId, long itemId) {
        List<Booking> bookings = bookingDao.findItemByOwner(userId, itemId, Status.APPROVED);
        Booking nextBooking = findByNextBooking(bookings);
        Booking lastBooking = findByLastBooking(bookings);
        List<Comment> comments = commentDao.findByItem_Id(itemId);
        Item item = itemDao.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Запись не найдена"));
        return ItemMapper.toItemBookingDto(lastBooking, nextBooking, comments, item);
    }

    @Override
    public List<ItemBookingDto> findAllForUser(long userId) {
        List<Item> items = itemDao.findAllForUser(userId);
        List<ItemBookingDto> itemsBooking = new ArrayList<>();
        Map<Item, List<Comment>> comments = commentDao.findByItemInOrderByCreatedDesc(items)
                .stream()
                .collect(Collectors.groupingBy(Comment::getItem));
        Map<Item, List<Booking>> bookings = bookingDao.findAllItemsByOwner(userId, Status.APPROVED)
                .stream()
                .collect(Collectors.groupingBy(Booking::getItem));
        for (Item item : items) {
            Booking nextBooking = null;
            Booking lastBooking = null;
            List<Comment> commentsByItem = comments.getOrDefault(item, List.of());
            List<Booking> bookingsByItem = bookings.getOrDefault(item, List.of());
            if (!bookingsByItem.isEmpty()) {
                nextBooking = findByNextBooking(bookingsByItem);
                lastBooking = findByLastBooking(bookingsByItem);
            }
            itemsBooking.add(ItemMapper.toItemBookingDto(lastBooking, nextBooking, commentsByItem, item));
        }
        return itemsBooking;
    }

    @Override
    public List<ItemRefundDto> search(String text) {
        return ItemMapper.toItemsRefundDto(itemDao.search(text));
    }

    @Transactional
    @Override
    public CommentRefundDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userDao.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        Item item = itemDao.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Вещь не найдена"));
        List<Booking> bookings = bookingDao.findByBooker_IdAndItem_Id(userId, itemId,
                Status.APPROVED);
        if (!bookings.isEmpty()) {
            Comment comment = CommentMapper.toComment(user, item, commentDto);
            return CommentMapper.toCommentDto(commentDao.save(comment));
        } else {
            throw new CommentException("У пользователя нет брони");
        }
    }

    private Booking findByNextBooking(List<Booking> bookings) {
        return bookings
                .stream()
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .reduce((first, second) -> second).orElse(null);
    }

    private Booking findByLastBooking(List<Booking> bookings) {
        return bookings
                .stream()
                .filter(x -> !x.getStart().isAfter(LocalDateTime.now()))
                .findFirst().orElse(null);
    }
}
