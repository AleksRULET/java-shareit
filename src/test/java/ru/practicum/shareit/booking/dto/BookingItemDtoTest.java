package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingItemDtoTest {
    @Autowired
    private JacksonTester<BookingItemDto> json;

    @Test
    void testBookingItemDto() throws Exception {
        BookingItemDto bookingDto = new BookingItemDto(
                1L,
                LocalDateTime.parse("2023-03-06T00:26:37.6943719"),
                LocalDateTime.parse("2023-04-06T00:26:37.6943719"),
                Status.WAITING,
                new BookingItemDto.Booker(3L, "John"),
                new BookingItemDto.Item(4, "Предмет")
        );
        JsonContent<BookingItemDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-03-06T00:26:37.6943719");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-04-06T00:26:37.6943719");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathValue("$.booker").extracting("id").isEqualTo(3);
        assertThat(result).extractingJsonPathValue("$.booker").extracting("name").isEqualTo("John");
        assertThat(result).extractingJsonPathValue("$.item").extracting("id").isEqualTo(4);
        assertThat(result).extractingJsonPathValue("$.item").extracting("name").isEqualTo("Предмет");
    }
}
