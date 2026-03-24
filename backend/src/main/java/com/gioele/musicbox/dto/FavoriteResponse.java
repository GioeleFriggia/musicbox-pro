package com.gioele.musicbox.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FavoriteResponse {
    private Long id;
    private String externalTrackId;
    private String title;
    private String artist;
    private String album;
    private String audioUrl;
    private String coverUrl;
    private Integer durationSeconds;
    private LocalDateTime createdAt;
}
