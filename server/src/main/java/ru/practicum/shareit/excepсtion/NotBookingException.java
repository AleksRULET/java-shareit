package ru.practicum.shareit.excepсtion;

public class NotBookingException extends RuntimeException {
    public NotBookingException(String message) {
        super(message);
    }
}
