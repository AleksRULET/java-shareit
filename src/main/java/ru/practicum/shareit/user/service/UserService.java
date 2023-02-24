package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto findById(long id);

    UserDto saveUser(UserDto userDto);

    UserDto edit(long userId, UserDto userDto);

    void removeUser(Long userId);
}
