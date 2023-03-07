package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestRefundDtoTest {
    @Autowired
    private JacksonTester<ItemRequestRefundDto> json;

    @Test
    void testItemRequestRefundDto() throws Exception {
        LocalDateTime time = LocalDateTime.parse("2023-03-06T00:26:37.6943719");
        ItemRequestRefundDto itemRequestRefundDto = new ItemRequestRefundDto(
                1L, "Запрос",
                time, List.of()
        );
        JsonContent<ItemRequestRefundDto> result = json.write(itemRequestRefundDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Запрос");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-03-06T00:26:37.6943719");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(new ArrayList<>());
    }
}