package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotBlank
    @Size(max = 30)
    private String name;
    @NotBlank
    @Email
    private String email;
}
