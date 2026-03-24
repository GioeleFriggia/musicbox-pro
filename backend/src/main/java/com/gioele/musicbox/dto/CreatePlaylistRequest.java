package com.gioele.musicbox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlaylistRequest {
    @NotBlank
    @Size(min = 2, max = 120)
    private String name;

    @Size(max = 500)
    private String description;
}
