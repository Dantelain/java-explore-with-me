package ru.practicum.explore.with.me.controllers.secret;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.events.dto.EditEventDto;
import ru.practicum.explore.with.me.events.dto.EventFullDto;
import ru.practicum.explore.with.me.events.dto.EventShortDto;
import ru.practicum.explore.with.me.events.dto.NewEventDto;
import ru.practicum.explore.with.me.events.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class UsersEventsController {

    private EventService eventService;

    @GetMapping
    public List<EventShortDto> getAll(@PathVariable Long userId,
                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {

        return eventService.getAll(userId, from, size);
    }

    @PatchMapping
    public EventFullDto editEvent(@PathVariable Long userId, @RequestBody EditEventDto editEventDto) {
        return eventService.edit(userId, editEventDto);
    }

    @PostMapping
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.create(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getOne(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelUserEvent(userId, eventId);
    }
}
