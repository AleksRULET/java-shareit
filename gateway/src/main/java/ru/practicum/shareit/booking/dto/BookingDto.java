package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.validation.DateStartBeforeEnd;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DateStartBeforeEnd(groups = {Create.class})
public class BookingDto {
	@FutureOrPresent(groups = {Create.class})
	private LocalDateTime start;
	@Future(groups = {Create.class})
	private LocalDateTime end;
	@NotNull(groups = {Create.class})
	private Long itemId;
}
