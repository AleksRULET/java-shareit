package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ItemPatchDto {
    @Size(max = 30)
    private String name;
    @Size(max = 200)
    private String description;
    private Boolean available;
}
