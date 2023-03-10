package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserPatchDto {
    @Size(max = 30)
    private String name;
    @Email
    private String email;
}
