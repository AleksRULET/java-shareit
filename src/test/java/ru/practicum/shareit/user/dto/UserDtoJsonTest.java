package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(
                "1@mail.com",
                "John");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("1@mail.com");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
    }
}
