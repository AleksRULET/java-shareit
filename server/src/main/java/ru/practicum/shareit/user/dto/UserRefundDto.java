package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRefundDto {
    private Long id;
    private String email;
    private String name;
}
