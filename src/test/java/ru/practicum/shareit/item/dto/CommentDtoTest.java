package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto( 3L,
                "Комментарий"
        );
        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Комментарий");
    }
}
