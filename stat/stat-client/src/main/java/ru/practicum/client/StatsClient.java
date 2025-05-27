package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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

    public void sendHit(String uri, String ip) {
        EndpointHitDto hit = new EndpointHitDto(appName, uri, ip, LocalDateTime.now());
        log.debug("Sending hit: {}", hit);
        post("/hit", hit);
    }

    public Object getStats(LocalDateTime start, LocalDateTime end, String uri, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", uri,
                "unique", unique
        );
        log.debug("Requesting stats with params: {}", parameters);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
