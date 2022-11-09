package ru.practicum.explore.with.me.categories.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesDto {

    private Long id;
    @NotBlank
    private String name;

}
