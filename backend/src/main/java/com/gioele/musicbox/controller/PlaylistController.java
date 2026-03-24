package com.gioele.musicbox.controller;

import com.gioele.musicbox.dto.*;
import com.gioele.musicbox.entity.User;
import com.gioele.musicbox.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public List<PlaylistResponse> getAll(@AuthenticationPrincipal User user) {
        return playlistService.getUserPlaylists(user);
    }

    @PostMapping
    public PlaylistResponse create(@AuthenticationPrincipal User user, @Valid @RequestBody CreatePlaylistRequest request) {
        return playlistService.createPlaylist(user, request);
    }

    @PostMapping("/{playlistId}/tracks")
    public PlaylistResponse addTrack(@AuthenticationPrincipal User user,
                                     @PathVariable Long playlistId,
                                     @Valid @RequestBody AddTrackRequest request) {
        return playlistService.addTrack(user, playlistId, request);
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public PlaylistResponse deleteTrack(@AuthenticationPrincipal User user,
                                        @PathVariable Long playlistId,
                                        @PathVariable Long trackId) {
        return playlistService.deleteTrack(user, playlistId, trackId);
    }

    @DeleteMapping("/{playlistId}")
    public ApiMessage deletePlaylist(@AuthenticationPrincipal User user, @PathVariable Long playlistId) {
        return playlistService.deletePlaylist(user, playlistId);
    }
}
