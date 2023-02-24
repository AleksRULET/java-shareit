package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao repository;

    @Override
    public List<UserDto> findAllUsers() {
        return UserMapper.toUsersDto(repository.findAll());
    }

    @Override
    public UserDto findById(long id) {
        return UserMapper.toUserDto(repository.findById(id));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto edit(long userId, UserDto userDto) {
        return UserMapper.toUserDto(repository.edit(userId, UserMapper.toUser(userDto)));
    }

    @Override
    public void removeUser(Long userId) {
        repository.deleteUser(userId);
    }
}