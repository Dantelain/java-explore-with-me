package ru.practicum.explore.with.me.controllers.open;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.events.service.EventService;
import ru.practicum.explore.with.me.events.dto.EventFullDto;
import ru.practicum.explore.with.me.events.dto.EventShortDto;
import ru.practicum.explore.with.me.stats.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/events")
public class EventsController {

    private EventService eventService;
    private StatsService statsService;


    @GetMapping
    public List<EventShortDto> getAll(HttpServletRequest request,
                                      @RequestParam String text,
                                      @RequestParam List<Long> categories,
                                      @RequestParam Boolean paid,
                                      @RequestParam(required = false, defaultValue = "") String rangeStart,
                                      @RequestParam(required = false, defaultValue = "") String rangeEnd,
                                      @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(required = false, defaultValue = "ID") String sort,
                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) throws URISyntaxException, IOException, InterruptedException {

        List<EventShortDto> eventShortDtoList = eventService.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return statsService.getViewStats(request, eventShortDtoList);

    }

    @GetMapping("/{id}")
    public EventFullDto getOne(HttpServletRequest request, @PathVariable Long id) throws URISyntaxException, IOException, InterruptedException {
        EventFullDto eventFullDto = eventService.getOne(id);
        return statsService.getViewStats(request, eventFullDto);
    }

}
