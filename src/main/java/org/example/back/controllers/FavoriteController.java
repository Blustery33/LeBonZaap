package org.example.back.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.back.models.FavoriteModel;
import org.example.back.models.dto.FavoriteDTO;
import org.example.back.models.dto.OffersDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favorite", description = "The Favorite API")
@RequestMapping("/favorite")
public interface FavoriteController {

    @Operation(summary = "Get all favorite by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "favorite retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Favorite not found")
    })
    @GetMapping("/user/{userId}")
    ResponseEntity<?> getFavoriteByUserId(@PathVariable Long userId);

    @Operation(summary = "Update favorite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Favorite not found")
    })
    @PostMapping("/update")
    ResponseEntity<?> updateFavorite(@RequestBody FavoriteDTO favoriteDTO);

}
