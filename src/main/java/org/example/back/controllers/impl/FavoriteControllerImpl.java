package org.example.back.controllers.impl;

import org.example.back.controllers.FavoriteController;
import org.example.back.models.FavoriteModel;
import org.example.back.models.dto.FavoriteDTO;
import org.example.back.models.mapper.FavoriteMapper;
import org.example.back.services.FavoriteService;
import org.example.back.services.exception.ErrorResponse;
import org.example.back.services.exception.NoFavoritesFoundException;
import org.example.back.services.exception.OfferNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FavoriteControllerImpl implements FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteMapper favoriteMapper;


    public ResponseEntity<?> getFavoriteByUserId(@PathVariable Long userId) {
        try {
            List<FavoriteDTO> favoriteDTOs = favoriteService.getAllFavoriteByUserId(userId);
            return new ResponseEntity<>(favoriteDTOs, HttpStatus.OK);
        } catch (NoFavoritesFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Une erreur est survenue", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // Retourner 500 avec message d'erreur
        }
    }

    @Override
    public ResponseEntity<?> updateFavorite(FavoriteDTO favoriteDTO) {
        try {
            FavoriteModel updatedFavorite = favoriteService.updateFavorite(favoriteDTO);
            // Mapper le modèle vers DTO et retourner la réponse
            FavoriteDTO response = new FavoriteDTO();
            response.setId(updatedFavorite.getId());
            response.setUserId(updatedFavorite.getUser().getId());
            response.setOfferId(updatedFavorite.getOffer().getId());
            response.setIsFavorite(updatedFavorite.getIsFavorite());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (OfferNotFoundException ex) {
            // Gestion d'exception spécifique pour l'offre non trouvée
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Gestion d'autres erreurs génériques
            ErrorResponse errorResponse = new ErrorResponse("Une erreur est survenue.", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
