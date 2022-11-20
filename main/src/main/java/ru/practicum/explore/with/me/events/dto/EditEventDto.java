package ru.practicum.explore.with.me.events.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EditEventDto extends EventBaseDto {

    private Long eventId;

}
