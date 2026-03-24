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
            throw new ResponseStatusException(BAD_REQUEST, "Configura JAMENDO_CLIENT_ID nel backend");
        }

        if (!StringUtils.hasText(query)) {
            return List.of();
        }

        Map<?, ?> response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host("api.jamendo.com")
                .path("/v3.0/tracks")
                .queryParam("client_id", appProperties.getJamendoClientId())
                .queryParam("format", "json")
                .queryParam("limit", 20)
                .queryParam("audioformat", "mp32")
                .queryParam("include", "musicinfo licenses")
                .queryParam("namesearch", query)
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Map.class);

        @SuppressWarnings("unchecked")
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

            Object license = item.get("license_ccurl");
            String licenseName = license == null ? null : license.toString();

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

    @SuppressWarnings("unchecked")
    private String extractJamendoGenre(Map<String, Object> item) {
        Object musicInfoObj = item.get("musicinfo");
        if (!(musicInfoObj instanceof Map<?, ?> musicInfo)) {
            return null;
        }

        Object tagsObj = musicInfo.get("tags");
        if (!(tagsObj instanceof Map<?, ?> tags)) {
            return null;
        }

        Object genresObj = tags.get("genres");
        if (genresObj instanceof List<?> genres && !genres.isEmpty()) {
            Object first = genres.get(0);
            return first == null ? null : first.toString();
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