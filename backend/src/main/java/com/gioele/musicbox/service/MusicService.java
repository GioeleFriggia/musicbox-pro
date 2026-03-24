package com.gioele.musicbox.service;

import com.gioele.musicbox.config.AppProperties;
import com.gioele.musicbox.dto.TrackDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class MusicService {
    private final RestClient restClient;
    private final AppProperties appProperties;
    private final DiscogsService discogsService;

    public MusicService(RestClient restClient, AppProperties appProperties, DiscogsService discogsService) {
        this.restClient = restClient;
        this.appProperties = appProperties;
        this.discogsService = discogsService;
    }

    public List<TrackDto> searchTracks(String query) {
        if (!StringUtils.hasText(appProperties.getJamendoClientId())) {
            throw new ResponseStatusException(BAD_REQUEST, "Configura JAMENDO_CLIENT_ID nel backend/.env");
        }
        if (!StringUtils.hasText(query)) {
            return List.of();
        }

        Map<String, Object> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.jamendo.com")
                        .path("/v3.0/tracks")
                        .queryParam("client_id", appProperties.getJamendoClientId())
                        .queryParam("format", "json")
                        .queryParam("limit", 20)
                        .queryParam("audioformat", "mp32")
                        .queryParam("include", "musicinfo licenses")
                        .queryParam("search", query)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        List<TrackDto> tracks = new ArrayList<>();
        if (results == null) {
            return tracks;
        }

        for (Map<String, Object> item : results) {
            String artist = getString(item, "artist_name");
            String title = getString(item, "name");
            DiscogsService.DiscogsMatch match = discogsService.searchBestMatch(artist, title);

            String cover = getString(item, "album_image");
            if (!StringUtils.hasText(cover) && match != null) {
                cover = match.getCoverUrl();
            }

            Map<String, Object> license = (Map<String, Object>) item.get("license_ccurl");
            String licenseName = null;
            if (license != null) {
                licenseName = license.toString();
            }

            tracks.add(TrackDto.builder()
                    .id(getString(item, "id"))
                    .title(title)
                    .artist(artist)
                    .album(getString(item, "album_name"))
                    .audioUrl(getString(item, "audio"))
                    .coverUrl(cover)
                    .durationSeconds(getInteger(item.get("duration")))
                    .source("JAMENDO")
                    .genre(match != null ? match.getGenre() : extractJamendoGenre(item))
                    .releaseYear(match != null ? match.getYear() : null)
                    .licenseName(licenseName)
                    .discogsUri(match != null ? match.getUri() : null)
                    .build());
        }
        return tracks;
    }

    private String extractJamendoGenre(Map<String, Object> item) {
        Map<String, Object> musicInfo = (Map<String, Object>) item.get("musicinfo");
        if (musicInfo == null) {
            return null;
        }
        Map<String, Object> tags = (Map<String, Object>) musicInfo.get("tags");
        if (tags == null) {
            return null;
        }
        Object genres = tags.get("genres");
        if (genres instanceof List<?> list && !list.isEmpty()) {
            return list.getFirst().toString();
        }
        return null;
    }

    private String getString(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value == null ? null : value.toString();
    }

    private Integer getInteger(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }
}
