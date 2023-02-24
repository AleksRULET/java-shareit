package ru.practicum.shareit.excepction;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String massage) {
        super(massage);
    }
}
