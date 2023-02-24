package ru.practicum.shareit.excepction;

public class NotOwnerException extends RuntimeException {
    public NotOwnerException(String message) {
        super(message);
    }
}
