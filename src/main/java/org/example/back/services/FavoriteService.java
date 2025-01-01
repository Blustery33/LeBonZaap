package org.example.back.services;

import org.example.back.models.FavoriteModel;
import org.example.back.models.dto.FavoriteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoriteService {

    List<FavoriteDTO> getAllFavoriteByUserId(Long userId);

    FavoriteModel updateFavorite(FavoriteDTO favoriteDTO);
}
