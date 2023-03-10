package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode
public class ItemRequestCreateDto {
    @Size(max = 512)
    @NotBlank
    private String description;
}
