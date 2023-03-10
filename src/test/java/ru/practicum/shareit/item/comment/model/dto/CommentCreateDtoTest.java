package ru.practicum.shareit.item.comment.model.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.utils.ControllerConstants;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentCreateDtoTest {

    @Autowired
    private JacksonTester<CommentCreateDto> json;

    @SneakyThrows
    @Test
    void testCommentCreateDto() {
        String commentCreateDtoText = "text";
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText(commentCreateDtoText);

        JsonContent<CommentCreateDto> result = json.write(commentCreateDto);

        ControllerConstants controllerConstants = new ControllerConstants();

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentCreateDtoText);
    }
}