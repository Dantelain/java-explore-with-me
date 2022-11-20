package ru.practicum.explore.with.me.users.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDto {

    private Long id;
    @NotBlank
    private String name;
    @Email
    private String email;

}
