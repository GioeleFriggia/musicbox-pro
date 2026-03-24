package com.gioele.musicbox.service;

import com.gioele.musicbox.dto.AddTrackRequest;
import com.gioele.musicbox.dto.ApiMessage;
import com.gioele.musicbox.dto.FavoriteResponse;
import com.gioele.musicbox.entity.FavoriteTrack;
import com.gioele.musicbox.entity.User;
import com.gioele.musicbox.repository.FavoriteTrackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteTrackRepository favoriteTrackRepository;

    public FavoriteService(FavoriteTrackRepository favoriteTrackRepository) {
        this.favoriteTrackRepository = favoriteTrackRepository;
    }

    public List<FavoriteResponse> getFavorites(User user) {
        return favoriteTrackRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public FavoriteResponse addFavorite(User user, AddTrackRequest request) {
        return favoriteTrackRepository.findByUserIdAndExternalTrackId(user.getId(), request.getExternalTrackId())
                .map(this::toResponse)
                .orElseGet(() -> toResponse(favoriteTrackRepository.save(FavoriteTrack.builder()
                        .user(user)
                        .externalTrackId(request.getExternalTrackId())
                        .title(request.getTitle())
                        .artist(request.getArtist())
                        .album(request.getAlbum())
                        .audioUrl(request.getAudioUrl())
                        .coverUrl(request.getCoverUrl())
                        .durationSeconds(request.getDurationSeconds())
                        .createdAt(LocalDateTime.now())
                        .build())));
    }

    public ApiMessage removeFavorite(User user, String trackId) {
        favoriteTrackRepository.deleteByUserIdAndExternalTrackId(user.getId(), trackId);
        return new ApiMessage("Brano rimosso dai preferiti");
    }

    private FavoriteResponse toResponse(FavoriteTrack track) {
        return FavoriteResponse.builder()
                .id(track.getId())
                .externalTrackId(track.getExternalTrackId())
                .title(track.getTitle())
                .artist(track.getArtist())
                .album(track.getAlbum())
                .audioUrl(track.getAudioUrl())
                .coverUrl(track.getCoverUrl())
                .durationSeconds(track.getDurationSeconds())
                .createdAt(track.getCreatedAt())
                .build();
    }
}
