package org.example.back.models.dto;

import lombok.Data;

@Data
public class FavoriteDTO {
    private Long id;
    private Long userId;
    private Long offerId;
    private Boolean isFavorite;
}
