package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto findById(long itemId);

    List<ItemDto> findAll(long userId);

    ItemDto save(long userId, ItemDto itemDto);

    ItemDto edit(long userId, long itemId, ItemDto itemDto);

    List<ItemDto> findByText(String text);
}
