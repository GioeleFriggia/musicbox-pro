package com.gioele.musicbox.repository;

import com.gioele.musicbox.entity.FavoriteTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteTrackRepository extends JpaRepository<FavoriteTrack, Long> {
    List<FavoriteTrack> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<FavoriteTrack> findByUserIdAndExternalTrackId(Long userId, String externalTrackId);
    boolean existsByUserIdAndExternalTrackId(Long userId, String externalTrackId);
    void deleteByUserIdAndExternalTrackId(Long userId, String externalTrackId);
}
