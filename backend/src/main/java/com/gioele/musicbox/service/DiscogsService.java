package com.gioele.musicbox.service;

import com.gioele.musicbox.config.AppProperties;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class DiscogsService {
    private final RestClient restClient;
    private final AppProperties appProperties;

    public DiscogsService(RestClient restClient, AppProperties appProperties) {
        this.restClient = restClient;
        this.appProperties = appProperties;
    }

    public DiscogsMatch searchBestMatch(String artist, String title) {
        if (!StringUtils.hasText(appProperties.getDiscogsToken())) {
            return null;
        }

        try {
            Map<String, Object> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("api.discogs.com")
                            .path("/database/search")
                            .queryParam("track", title)
                            .queryParam("artist", artist)
                            .queryParam("type", "release")
                            .queryParam("token", appProperties.getDiscogsToken())
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("User-Agent", appProperties.getDiscogsUserAgent())
                    .retrieve()
                    .body(Map.class);

            if (response == null || response.get("results") == null) {
                return null;
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            if (results.isEmpty()) {
                return null;
            }

            Map<String, Object> first = results.getFirst();
            return DiscogsMatch.builder()
                    .year(valueAsString(first.get("year")))
                    .genre(extractFirstString(first.get("genre")))
                    .coverUrl(valueAsString(first.get("cover_image")))
                    .uri(valueAsString(first.get("uri")))
                    .build();
        } catch (Exception ex) {
            return null;
        }
    }

    private String extractFirstString(Object value) {
        if (value instanceof List<?> list && !list.isEmpty()) {
            Object first = list.getFirst();
            return first == null ? null : first.toString();
        }
        return null;
    }

    private String valueAsString(Object value) {
        return value == null ? null : value.toString();
    }

    @Getter
    @Builder
    public static class DiscogsMatch {
        private String year;
        private String genre;
        private String coverUrl;
        private String uri;
    }
}
