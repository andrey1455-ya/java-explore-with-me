package ru.practicum.client;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());
        return makeAndSendRequest(HttpMethod.POST, path, requestEntity);
    }

    protected ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(defaultHeaders());
        return makeAndSendRequest(HttpMethod.GET, path, requestEntity, parameters);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                      HttpEntity<?> requestEntity) {
        return makeAndSendRequest(method, path, requestEntity, null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                      HttpEntity<?> requestEntity,
                                                      Map<String, Object> parameters) {
        try {
            if (parameters != null) {
                return rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                return rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsByteArray());
        }
    }
}
