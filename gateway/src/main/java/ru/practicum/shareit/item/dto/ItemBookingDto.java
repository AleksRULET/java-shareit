package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentRefundDto> comments;

    @Data
    public static class Booking {
        private final Long id;
        private final Long bookerId;
    }
}
