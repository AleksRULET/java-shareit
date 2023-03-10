package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentCreateDto {
    @NotBlank
    @Size(max = 1025)
    private String text;
}
