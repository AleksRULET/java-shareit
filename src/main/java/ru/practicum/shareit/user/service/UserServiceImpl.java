package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.excepction.ObjectNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserRefundDto> findAll() {
        return UserMapper.toUsersDto(repository.findAll());
    }

    @Override
    public UserRefundDto findById(long id) {
        return UserMapper.toUserDto(repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден")));
    }

    @Transactional
    @Override
    public UserRefundDto save(UserDto userDto) {
        User user  = UserMapper.toUser(null, userDto);
        User userRefund = repository.save(user);
        return UserMapper.toUserDto(userRefund);
    }

    @Transactional
    @Override
    public UserRefundDto edit(long userId, UserDto userDto) {
        User userNew = UserMapper.toUser(userId, userDto);
        User userOld = repository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        if (userNew.getName() != null && !userNew.getName().isBlank()) {
            userOld.setName(userNew.getName());
        }
        if (userNew.getEmail() != null && !userNew.getEmail().isBlank()) {
            userOld.setEmail(userNew.getEmail());
        }
        return UserMapper.toUserDto(userOld);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }
}