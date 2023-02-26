package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.excepction.DuplicateException;
import ru.practicum.shareit.excepction.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    private long id;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User save(User user) {
        validate(user);
        ++id;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User edit(long userId, User user) {
        User oldUser = users.get(userId);
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            if (!user.getEmail().equals(oldUser.getEmail())) {
                validate(user);
                oldUser.setEmail(user.getEmail());
            }
        }
        return oldUser;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private void validate(User user) {
        if (users.values().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new DuplicateException("Пользователь с указанной почтой уже существует");
        }
    }
}
