package org.example.back.models.mapper;

import org.example.back.annotations.CurrentUser;
import org.example.back.models.NoticeOfferModel;
import org.example.back.models.OffersModel;
import org.example.back.models.UserModel;
import org.example.back.models.dto.OffersDTO;
import org.springframework.stereotype.Component;

@Component
public class OffersMapper extends GenericMapperImpl<OffersModel, OffersDTO> {
    public OffersMapper() {
        super(OffersModel.class, OffersDTO.class);
    }
}
