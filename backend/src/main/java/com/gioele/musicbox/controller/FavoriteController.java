package com.gioele.musicbox.controller;

import com.gioele.musicbox.dto.AddTrackRequest;
import com.gioele.musicbox.dto.ApiMessage;
import com.gioele.musicbox.dto.FavoriteResponse;
import com.gioele.musicbox.entity.User;
import com.gioele.musicbox.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<FavoriteResponse> getAll(@AuthenticationPrincipal User user) {
        return favoriteService.getFavorites(user);
    }

    @PostMapping
    public FavoriteResponse add(@AuthenticationPrincipal User user, @Valid @RequestBody AddTrackRequest request) {
        return favoriteService.addFavorite(user, request);
    }

    @DeleteMapping("/{trackId}")
    public ApiMessage remove(@AuthenticationPrincipal User user, @PathVariable String trackId) {
        return favoriteService.removeFavorite(user, trackId);
    }
}
