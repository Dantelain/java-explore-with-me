package ru.practicum.explore.with.me.service;

import ru.practicum.explore.with.me.dto.StatsHitDto;
import ru.practicum.explore.with.me.dto.StatsViewDto;

import java.util.List;

public interface StatsService {

    void addHit(StatsHitDto statsHitDto);

    List<StatsViewDto> getViewStats(String start, String end, List<String> uris, Boolean unique);

}
