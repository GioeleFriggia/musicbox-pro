package com.gioele.musicbox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_tracks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "externalTrackId"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String externalTrackId;

    @Column(nullable = false, length = 220)
    private String title;

    @Column(nullable = false, length = 220)
    private String artist;

    @Column(length = 220)
    private String album;

    @Column(length = 1000)
    private String audioUrl;

    @Column(length = 1000)
    private String coverUrl;

    @Column
    private Integer durationSeconds;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
