package ru.practicum.explore.with.me.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsCreateDto {

    @NotNull
    private Long eventId;
    @NotBlank
    @Size(max = 2000)
    private String text;

}
