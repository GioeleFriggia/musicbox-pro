package com.gioele.musicbox.service;

import com.gioele.musicbox.dto.*;
import com.gioele.musicbox.entity.Playlist;
import com.gioele.musicbox.entity.PlaylistTrack;
import com.gioele.musicbox.entity.User;
import com.gioele.musicbox.repository.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public List<PlaylistResponse> getUserPlaylists(User user) {
        return playlistRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlaylistResponse createPlaylist(User user, CreatePlaylistRequest request) {
        Playlist playlist = Playlist.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        return toResponse(playlistRepository.save(playlist));
    }

    public PlaylistResponse addTrack(User user, Long playlistId, AddTrackRequest request) {
        Playlist playlist = findOwnedPlaylist(user, playlistId);
        int nextPosition = playlist.getTracks().stream()
                .map(PlaylistTrack::getPosition)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        playlist.getTracks().add(PlaylistTrack.builder()
                .playlist(playlist)
                .externalTrackId(request.getExternalTrackId())
                .title(request.getTitle())
                .artist(request.getArtist())
                .album(request.getAlbum())
                .audioUrl(request.getAudioUrl())
                .coverUrl(request.getCoverUrl())
                .durationSeconds(request.getDurationSeconds())
                .position(nextPosition)
                .build());

        return toResponse(playlistRepository.save(playlist));
    }

    public PlaylistResponse deleteTrack(User user, Long playlistId, Long trackId) {
        Playlist playlist = findOwnedPlaylist(user, playlistId);
        playlist.getTracks().removeIf(track -> track.getId().equals(trackId));
        normalizePositions(playlist);
        return toResponse(playlistRepository.save(playlist));
    }

    public ApiMessage deletePlaylist(User user, Long playlistId) {
        Playlist playlist = findOwnedPlaylist(user, playlistId);
        playlistRepository.delete(playlist);
        return new ApiMessage("Playlist eliminata");
    }

    private Playlist findOwnedPlaylist(User user, Long playlistId) {
        return playlistRepository.findByIdAndUserId(playlistId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Playlist non trovata"));
    }

    private void normalizePositions(Playlist playlist) {
        List<PlaylistTrack> ordered = playlist.getTracks().stream()
                .sorted(Comparator.comparing(PlaylistTrack::getPosition))
                .toList();
        for (int i = 0; i < ordered.size(); i++) {
            ordered.get(i).setPosition(i + 1);
        }
    }

    private PlaylistResponse toResponse(Playlist playlist) {
        return PlaylistResponse.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .createdAt(playlist.getCreatedAt())
                .tracks(playlist.getTracks().stream()
                        .sorted(Comparator.comparing(PlaylistTrack::getPosition))
                        .map(track -> PlaylistTrackResponse.builder()
                                .id(track.getId())
                                .externalTrackId(track.getExternalTrackId())
                                .title(track.getTitle())
                                .artist(track.getArtist())
                                .album(track.getAlbum())
                                .audioUrl(track.getAudioUrl())
                                .coverUrl(track.getCoverUrl())
                                .durationSeconds(track.getDurationSeconds())
                                .position(track.getPosition())
                                .build())
                        .toList())
                .build();
    }
}
