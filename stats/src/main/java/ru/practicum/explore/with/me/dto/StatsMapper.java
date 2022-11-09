package ru.practicum.explore.with.me.dto;

import ru.practicum.explore.with.me.model.StatsHit;

import java.net.URISyntaxException;

public class StatsMapper {

    public static StatsHit toStatsHit(StatsHitDto statsHitDto) throws URISyntaxException {
        return StatsHit.builder()
                .app(statsHitDto.getApp())
                .uri(statsHitDto.getUri())
                .timestamp(statsHitDto.getTimestamp())
                .ip(statsHitDto.getIp())
                .build();
    }
}
