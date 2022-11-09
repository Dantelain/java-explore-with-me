package ru.practicum.explore.with.me.events.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NewEventDto extends EventBaseDto {

    @NotNull
    private Location location;
    @Builder.Default
    private Boolean requestModeration = true; //Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private Float lat; //Широта
        private Float lon; //Долгота
    }

}
