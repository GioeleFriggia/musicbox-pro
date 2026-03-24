package com.gioele.musicbox.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackDto {
    private String id;
    private String title;
    private String artist;
    private String album;
    private String audioUrl;
    private String coverUrl;
    private Integer durationSeconds;
    private String source;
    private String genre;
    private String releaseYear;
    private String licenseName;
    private String discogsUri;
}
