package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRefundDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRefundDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemRefundDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                             @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return service.add(userId, itemDto);
    }


    @PatchMapping("/{itemId}")
    public ItemRefundDto edit(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId, @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        return service.edit(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemBookingDto findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long itemId) {
        return service.findById(userId, itemId);
    }

    @GetMapping
    public List<ItemBookingDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.findAllForUser(userId);
    }

    @GetMapping("/search")
    public List<ItemRefundDto> search(@RequestParam String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return service.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentRefundDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @Validated({Create.class}) @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        return service.addComment(userId, itemId, commentDto);
    }
}
