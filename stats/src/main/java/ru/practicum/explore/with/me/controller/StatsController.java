package ru.practicum.explore.with.me.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.dto.StatsHitDto;
import ru.practicum.explore.with.me.dto.StatsViewDto;
import ru.practicum.explore.with.me.service.StatsService;


import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class StatsController {

    private StatsService statsService;

    @PostMapping("/hit")
    public void addHit(@Valid @RequestBody StatsHitDto statsHitDto) {
        statsService.addHit(statsHitDto);
    }

    @GetMapping("/stats")
    public List<StatsViewDto> getViewStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris, @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statsService.getViewStats(start, end, uris, unique);
    }

}
