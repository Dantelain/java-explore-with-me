package ru.practicum.explore.with.me.controllers.open;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.events.service.EventService;
import ru.practicum.explore.with.me.events.dto.EventFullDto;
import ru.practicum.explore.with.me.events.dto.EventShortDto;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/events")
public class EventsController {

    private EventService eventService;


    @GetMapping
    public List<EventShortDto> getAll(@RequestParam String text,
                                      @RequestParam List<Long> categories,
                                      @RequestParam Boolean paid,
                                      @RequestParam(required = false, defaultValue = "") String rangeStart,
                                      @RequestParam(required = false, defaultValue = "") String rangeEnd,
                                      @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(required = false, defaultValue = "") String sort,
                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getOne(@PathVariable Long id) {
        return eventService.getOne(id);
    }

}
