package ru.practicum.explore.with.me.controllers.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.users.service.UserService;
import ru.practicum.explore.with.me.users.dto.UsersDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUsersController {

    private UserService userService;

    @PostMapping
    public UsersDto add(@Valid @RequestBody UsersDto usersDto) {
        return userService.add(usersDto);
    }

    @PatchMapping
    public UsersDto edit(@Valid @RequestBody UsersDto usersDto) {
        return userService.edit(usersDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping
    public List<UsersDto> search(@RequestParam List<Long> ids) {
        return userService.search(ids);
    }

}
