package ru.practicum.explore.with.me.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.dto.StatsHitDto;
import ru.practicum.explore.with.me.dto.StatsMapper;
import ru.practicum.explore.with.me.dto.StatsViewDto;
import ru.practicum.explore.with.me.model.StatsHit;
import ru.practicum.explore.with.me.repo.StatsRepo;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private StatsRepo statsRepo;

    @Override
    public void addHit(StatsHitDto statsHitDto) {
        try {
            StatsHit statsHit = StatsMapper.toStatsHit(statsHitDto);
            statsRepo.save(statsHit);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<StatsViewDto> getViewStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime rangeStart = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime rangeEnd = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (unique) {
            return statsRepo.countTotalIpDistinct(rangeStart, rangeEnd, uris);
        } else {
            return statsRepo.countTotalIp(rangeStart, rangeEnd, uris);
        }
    }

}
