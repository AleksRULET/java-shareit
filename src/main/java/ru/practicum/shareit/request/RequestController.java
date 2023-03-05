package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.booking.validation.ValidPage;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService service;

    @PostMapping
    public ItemRequestRefundDto add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody ItemRequestDto requestDto
    ) {
        return service.add(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestRefundDto> findAllOwner(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        return service.findAllOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestRefundDto> findAllNotOwner(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        int page = ValidPage.page(from, size);
        return service.findAllNotOwner(userId, page, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestRefundDto findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId
    ) {
        return service.findById(userId, requestId);
    }
}
