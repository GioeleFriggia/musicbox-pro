package com.gioele.musicbox.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlaylistResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<PlaylistTrackResponse> tracks;
}
