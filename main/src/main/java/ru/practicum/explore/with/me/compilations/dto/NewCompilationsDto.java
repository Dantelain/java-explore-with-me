package ru.practicum.explore.with.me.compilations.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationsDto {

    private Set<Long> events;
    @Builder.Default
    private Boolean pinned = false;
    @NotBlank
    private String title;

}
