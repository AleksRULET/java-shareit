package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentRefundDtoTest {
    @Autowired
    private JacksonTester<CommentRefundDto> json;

    @Test
    void testCommentRefundDto() throws Exception {
        LocalDateTime time = LocalDateTime.parse("2023-03-06T00:26:37.6943719");
        CommentRefundDto commentRefundDto = new CommentRefundDto(3L,
                "Текст", "John",
                time
        );
        JsonContent<CommentRefundDto> result = json.write(commentRefundDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Текст");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-03-06T00:26:37.6943719");
    }
}
