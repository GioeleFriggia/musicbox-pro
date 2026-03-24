package com.gioele.musicbox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddTrackRequest {
    @NotBlank
    private String externalTrackId;

    @NotBlank
    private String title;

    @NotBlank
    private String artist;

    private String album;
    private String audioUrl;
    private String coverUrl;

    @NotNull
    private Integer durationSeconds;
}
