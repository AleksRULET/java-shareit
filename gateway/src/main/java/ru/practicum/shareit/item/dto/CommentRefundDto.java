package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRefundDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
