package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(long userId, ItemRequestCreateDto itemRequestCreateDto);

    List<ItemRequestDto> getAllItemRequestCreatedByUser(long userId);

    List<ItemRequestDto> findAllCreatedByOther(long userId, int from, int size);

    ItemRequestDto getItemRequest(long userId, long requestId);
}
