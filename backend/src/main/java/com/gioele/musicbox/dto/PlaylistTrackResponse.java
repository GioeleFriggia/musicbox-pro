package com.gioele.musicbox.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaylistTrackResponse {
    private Long id;
    private String externalTrackId;
    private String title;
    private String artist;
    private String album;
    private String audioUrl;
    private String coverUrl;
    private Integer durationSeconds;
    private Integer position;
}
