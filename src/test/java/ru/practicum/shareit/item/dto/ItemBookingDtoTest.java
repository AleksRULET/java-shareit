package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemBookingDtoTest {
    @Autowired
    private JacksonTester<ItemBookingDto> json;

    @Test
    void testItemBookingDto() throws Exception {
        ItemBookingDto itemBookingDto = new ItemBookingDto( 3L,
                "Предмет", "Описание",
                true, new ItemBookingDto.Booking(1L, 2L),
                new ItemBookingDto.Booking(3L, 4L),
                new ArrayList<>()
        );
        JsonContent<ItemBookingDto> result = json.write(itemBookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Предмет");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Описание");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.lastBooking").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.lastBooking").extracting("bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathValue("$.nextBooking").extracting("id").isEqualTo(3);
        assertThat(result).extractingJsonPathValue("$.nextBooking").extracting("bookerId").isEqualTo(4);
        assertThat(result).extractingJsonPathArrayValue("$.comments").isEqualTo(new ArrayList<>());
    }
}
