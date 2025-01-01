package org.example.back.models.mapper;

import org.example.back.models.FavoriteModel;
import org.example.back.models.dto.FavoriteDTO;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper extends GenericMapperImpl<FavoriteModel, FavoriteDTO>{
    public FavoriteMapper() {
        super(FavoriteModel.class, FavoriteDTO.class);
    }
}
