package ru.practicum.explore.with.me.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class EventBaseDto {

    @Size(min = 20)
    @Size(max = 2000)
    @NotBlank
    protected String annotation; //Краткое описание события
    @NotNull
    protected Long category; //id категории к которой относится событие
    @Size(min = 20)
    @Size(max = 7000)
    @NotBlank
    protected String description; //Полное описание события
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime eventDate; //Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    @Builder.Default
    protected Boolean paid = false; //Нужно ли оплачивать участие в событии
    @Builder.Default
    protected Integer participantLimit = 0; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @Size(min = 3)
    @Size(max = 120)
    protected String title;//Заголовок события

}
