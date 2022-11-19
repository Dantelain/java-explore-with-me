package ru.practicum.explore.with.me.stats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.events.dto.EventFullDto;
import ru.practicum.explore.with.me.events.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class StatsServiceImp implements StatsService {

    @Value("${stats-server.url}")
    private String serverUrl;
    private final ObjectMapper objectMapper;

    public StatsServiceImp(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void statsHit(HttpServletRequest request) throws URISyntaxException, JsonProcessingException {
        StatsHitDto statsHitDto = StatsHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .ip(request.getRemoteAddr())
                .build();
        sendStats(statsHitDto);
    }

    @Override
    public List<EventShortDto> getViewStats(HttpServletRequest request, List<EventShortDto> eventShortDtoList) throws URISyntaxException, IOException, InterruptedException {
        List<String> uris = eventShortDtoList.stream()
                .map(el -> "/events/" + el.getId().toString())
                .collect(Collectors.toList());
        List<StatsViewDto> statsViewDtoList = getStats(uris);
        Map<String, StatsViewDto> statsViewDtoMap = statsViewDtoList.stream().collect(Collectors.toMap(StatsViewDto::getUri, v -> v));
        eventShortDtoList.forEach(el -> {
            String key = "/events/" + el.getId().toString();
            StatsViewDto stats = new StatsViewDto();
            Long hits = statsViewDtoMap.getOrDefault(key, stats).getHits();
            el.setViews(hits);
        });
        statsHit(request, uris);
        return eventShortDtoList;
    }

    @Override
    public EventFullDto getViewStats(HttpServletRequest request, EventFullDto eventFullDto) throws URISyntaxException, IOException, InterruptedException {
        List<String> uris = List.of("/events/" + eventFullDto.getId().toString());
        List<StatsViewDto> statsViewDto = getStats(uris);
        Long view = (statsViewDto.size() != 0) ? statsViewDto.get(0).getHits() : 0L;
        eventFullDto.setViews(view);
        statsHit(request, uris);
        return eventFullDto;
    }

    private void statsHit(HttpServletRequest request, List<String> uris) throws JsonProcessingException, URISyntaxException {
        List<StatsHitDto> statsHitDtoList = uris.stream()
                .map(uri -> StatsHitDto.builder()
                        .app("ewm-main-service")
                        .uri(uri)
                        .timestamp(LocalDateTime.now())
                        .ip(request.getRemoteAddr())
                        .build())
                .collect(Collectors.toList());
        for (StatsHitDto statsHitDto : statsHitDtoList) {
            sendStats(statsHitDto);
        }
    }

    private void sendStats(StatsHitDto statsHitDto) throws JsonProcessingException, URISyntaxException {
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(statsHitDto);

        HttpRequest statRequest = HttpRequest.newBuilder(new URI(serverUrl + "/hit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient.newHttpClient()
                .sendAsync(statRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode);
    }

    private List<StatsViewDto> getStats(List<String> uris) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URIBuilder(serverUrl + "/stats")
                .addParameter("start", LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addParameter("end", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addParameter("uris", listToString(uris))
                .build();

        HttpRequest statRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(statRequest, HttpResponse.BodyHandlers.ofString());
        log.warn("response.body() - {}", response.body());
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    private String listToString(List<String> uris) {
        StringBuilder sb = new StringBuilder();
        var endSize = uris.size() - 1;
        for (int i = 0; i < uris.size(); i++) {
            sb.append(uris.get(i));
            if (i != endSize) sb.append(",");
        }
        return sb.toString();
    }
}
