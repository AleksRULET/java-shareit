package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
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
        log.info("Get users all");
        return service.findById(userId);
    }

    @PatchMapping("/{userId}")
    public UserRefundDto edit(
            @PathVariable long userId,
            @RequestBody UserDto userDto
    ) {
        log.info("Get user with userId={}", userId);
        return service.edit(userId, userDto);
    }

    @PostMapping
    public UserRefundDto add(
            @RequestBody UserDto userDto
    ) {
        log.info("Post user");
        return service.save(userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(
            @PathVariable long userId
    ) {
        log.info("Delete user with userId={}", userId);
        service.delete(userId);
    }
}
