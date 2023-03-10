package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingItemDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private Booker booker;
    private Item item;

    @Data
    public static class Booker {
        private final long id;
        private final String name;
    }

    @Data
    public static class Item {
        private final long id;
        private final String name;
    }
}
