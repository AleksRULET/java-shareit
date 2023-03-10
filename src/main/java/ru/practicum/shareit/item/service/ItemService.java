package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemPatchDto itemPatchDto);

    ItemDto getItem(long userId, long itemId);

    List<ItemDto> getItemsForUser(long userId, int from, int size);

    List<ItemDto> searchItems(String text, int from, int size);

    CommentDto addComment(long userId, long itemId, CommentCreateDto commentCreateDto);

}
