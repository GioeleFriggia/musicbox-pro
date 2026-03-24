package com.gioele.musicbox.controller;

import com.gioele.musicbox.dto.TrackDto;
import com.gioele.musicbox.service.MusicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicController {
    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/search")
    public List<TrackDto> search(@RequestParam String query) {
        return musicService.searchTracks(query);
    }
}
