package ru.practicum.shareit.excepction;

public class NotBookingException extends RuntimeException {
    public NotBookingException(String message) {
        super(message);
    }
}
