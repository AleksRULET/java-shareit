package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.excepction.InvalidValueException;

public class ValidPage {
    public static Integer page(Integer from, Integer size) {
        if (from % size == 0) {
            return from / size;
        } else {
            throw new InvalidValueException("передан не первый элемент страницы");
        }
    }
}
