package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ItemRequest {
    Long is;
    String description;
    User requester;
    LocalDateTime created;
}
