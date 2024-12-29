package org.example.back.models.mapper;

import org.example.back.models.NoticeOfferModel;
import org.example.back.models.dto.NoticeOfferDTO;
import org.springframework.stereotype.Component;

@Component
public class NoticeOfferMapper extends GenericMapperImpl<NoticeOfferModel, NoticeOfferDTO> {
    public NoticeOfferMapper() {
        super(NoticeOfferModel.class, NoticeOfferDTO.class);
    }
}
