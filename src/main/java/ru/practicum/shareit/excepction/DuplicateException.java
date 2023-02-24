package ru.practicum.shareit.excepction;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }

}
