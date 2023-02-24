package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {
    private long id;
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItems = new HashMap<>();

    @Override
    public Item add(long userId, Item item) {
        ++id;
        item.setId(id);
        items.put(id, item);
        final List<Item> itemsIndex = userItems.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        itemsIndex.add(item);
        userItems.put(userId, itemsIndex);
        return item;
    }

    @Override
    public Item edit(long userId, long itemId, Item item) {
        Item itemOld = items.get(itemId);
        if (item.getName() != null && !item.getName().isBlank()) {
            itemOld.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemOld.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemOld.setAvailable(item.getAvailable());
        }
        return itemOld;
    }

    @Override
    public Item findById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findAll(long userId) {
        if (userItems.containsKey(userId)) {
            return userItems.get(userId);
        } else {
            return List.of();
        }
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase()) || x.getDescription().toLowerCase().contains(text.toLowerCase()))
                .distinct()
                .collect(Collectors.toList());
    }
}
