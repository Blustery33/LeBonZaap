package org.example.back.services.impl;

import lombok.Data;
import org.example.back.models.FavoriteModel;
import org.example.back.models.OffersModel;
import org.example.back.models.UserModel;
import org.example.back.models.dto.FavoriteDTO;
import org.example.back.models.mapper.FavoriteMapper;
import org.example.back.repository.FavoriteRepository;
import org.example.back.repository.OffersRepository;
import org.example.back.repository.UserRepository;
import org.example.back.services.FavoriteService;
import org.example.back.services.exception.NoFavoritesFoundException;
import org.example.back.services.exception.OfferNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final FavoriteMapper favoriteMapper;

    private final UserRepository userRepository;

    private final OffersRepository offerRepository;

    @Override
    public List<FavoriteDTO> getAllFavoriteByUserId(Long userId) {
         List<FavoriteModel> favorite = favoriteRepository.findByUserId(userId);

        if (favorite.isEmpty()) {
            throw new NoFavoritesFoundException("Aucun favori trouvé pour l'utilisateur avec l'identifiant: " + userId);
        }

        return favorite.stream()
                .map(favoriteMapper::toDto)
                .toList();
    }

    @Override
    public FavoriteModel updateFavorite(FavoriteDTO favoriteDTO) {
        FavoriteModel favorite = favoriteRepository.findByUserIdAndOfferId(
                favoriteDTO.getUserId(),
                favoriteDTO.getOfferId()
        );

        if (favorite == null) {
            // Create new favorite when favori not found
            UserModel user = userRepository.findById(favoriteDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            OffersModel offer = offerRepository.findById(favoriteDTO.getOfferId())
                    .orElseThrow(() -> new OfferNotFoundException("Offre introuvable avec l'identifiant: " + favoriteDTO.getOfferId()));

            favorite = new FavoriteModel();
            favorite.setUser(user);
            favorite.setOffer(offer);
        }

        // Mise à jour de isFavorite
        favorite.setIsFavorite(favoriteDTO.getIsFavorite());

        // Save in database
        return favoriteRepository.save(favorite);
    }
}
