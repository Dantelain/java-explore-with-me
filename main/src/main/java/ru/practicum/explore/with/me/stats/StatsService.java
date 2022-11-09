package ru.practicum.explore.with.me.stats;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;

public interface StatsService {
    void statsHit(HttpServletRequest request) throws URISyntaxException, JsonProcessingException;
}
