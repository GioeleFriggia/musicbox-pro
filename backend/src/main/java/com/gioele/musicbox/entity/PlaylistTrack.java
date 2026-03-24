package com.gioele.musicbox.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "playlist_tracks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

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
    private Integer position;
}
