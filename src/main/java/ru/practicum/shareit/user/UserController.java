package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserRefundDto> findAll() {
        return service.findAll();
    }
    @GetMapping("/{userId}")
    public UserRefundDto findById(
            @PathVariable long userId
    ) {
        return service.findById(userId);
    }

    @PatchMapping("/{userId}")
    public UserRefundDto edit(
            @PathVariable long userId,
            @Validated({Update.class}) @RequestBody UserDto userDto
    ) {
        return service.edit(userId, userDto);
    }

    @PostMapping
    public UserRefundDto add(
            @Validated({Create.class}) @RequestBody UserDto userDto
    ) {
        return service.save(userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(
            @PathVariable long userId
    ) {
        service.delete(userId);
    }
}
