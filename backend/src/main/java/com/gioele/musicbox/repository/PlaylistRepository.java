package com.gioele.musicbox.repository;

import com.gioele.musicbox.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Playlist> findByIdAndUserId(Long id, Long userId);
}
