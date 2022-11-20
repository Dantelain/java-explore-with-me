package ru.practicum.explore.with.me.compilations.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {

    private Long id;
    @Builder.Default
    private Set<EventShortDto> events = new HashSet<>();
    private Boolean pinned;
    private String title;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EventShortDto {

        private Long id;
        @NotBlank
        private String annotation; //Краткое описание
        @NotNull
        private CategoryDto category;
        private Long confirmedRequests;
        @NotNull
        private LocalDateTime eventDate;
        @NotNull
        private UserShortDto initiator;
        @NotNull
        private Boolean paid;
        @NotNull
        private String title;
        private Long views;

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UserShortDto {
            private Long id; //Идентификатор
            private String name; //Имя
        }

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class CategoryDto {
            private Long id; //Идентификатор категории
            private String name; //Название категории
        }

    }

}
