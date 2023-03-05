package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto(LocalDateTime.parse("2023-03-06T00:26:37.6943719"),
                LocalDateTime.parse("2023-04-06T00:26:37.6943719"), 1L
        );
        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-03-06T00:26:37.6943719");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-04-06T00:26:37.6943719");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}
