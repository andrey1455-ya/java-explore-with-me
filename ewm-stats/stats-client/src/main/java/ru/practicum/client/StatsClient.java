package ru.practicum.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatsClient extends BaseClient {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String appName;

    public StatsClient(@Value("${stats.server.url}") String serverUrl,
                       @Value("${app.name}") String appName) {
        super(new RestTemplateBuilder().rootUri(serverUrl).build());
        this.appName = appName;
    }

    public void addHit(EndpointHitDto endpointHitDto) {
        post("/hit", endpointHitDto);
    }


    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath("/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris)
                .queryParam("unique", unique);
        try {
            ResponseEntity<List<ViewStatsDto>> response = rest.exchange(
                    uriComponentsBuilder.build().toString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    }
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return List.of();
            }
        } catch (Exception e) {
            return List.of();
        }
    }
}