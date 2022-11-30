package ru.practicum.explore.with.me.stats;

import ru.practicum.explore.with.me.events.dto.EventFullDto;
import ru.practicum.explore.with.me.events.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface StatsService {

    List<EventShortDto> getViewStats(HttpServletRequest request, List<EventShortDto> eventShortDtoList) throws URISyntaxException, IOException, InterruptedException;

    EventFullDto getViewStats(HttpServletRequest request, EventFullDto eventFullDto) throws URISyntaxException, IOException, InterruptedException;
}
