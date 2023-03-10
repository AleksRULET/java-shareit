package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.booking.validation.ValidPage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody ItemDto itemDto
    ) {
        log.info("Post item with userId={}", userId);
        return itemClient.add(userId, itemDto);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> edit(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @Validated({Update.class}) @RequestBody ItemDto itemDto
    ) {
        log.info("Patch item with userId={}, itemId={}", userId, itemId);
        return itemClient.edit(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId
    ) {
        log.info("Get item with userId={}, itemId={}", userId, itemId);
        return itemClient.findById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get item with userId={}", userId);
        return itemClient.findAllForUser(page, size, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam String text,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        int page = ValidPage.page(from, size);
        log.info("Get item search={}", text);
        return itemClient.search(text, page, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody CommentDto commentDto,
            @PathVariable Long itemId
    ) {
        log.info("Post comment itemId={}", itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
