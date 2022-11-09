package ru.practicum.explore.with.me.stats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;


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

}
