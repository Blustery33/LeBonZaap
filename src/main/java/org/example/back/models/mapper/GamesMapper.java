package org.example.back.models.mapper;

import org.example.back.models.GamesModel;
import org.example.back.models.dto.GamesDTO;
import org.springframework.stereotype.Component;

@Component
public class GamesMapper extends GenericMapperImpl<GamesModel, GamesDTO> {
    public GamesMapper() {
        super(GamesModel.class, GamesDTO.class);
    }
}
