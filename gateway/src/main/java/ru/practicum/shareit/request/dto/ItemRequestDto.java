package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank(groups = {Create.class})
    @Size(max = 200, groups = {Update.class, Create.class})
    private String description;
}
