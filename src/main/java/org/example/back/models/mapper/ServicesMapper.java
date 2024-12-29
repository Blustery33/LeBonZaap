package org.example.back.models.mapper;

import org.example.back.models.ServicesModel;
import org.example.back.models.dto.ServicesDTO;
import org.springframework.stereotype.Component;


@Component
public class ServicesMapper extends GenericMapperImpl<ServicesModel, ServicesDTO> {
    public ServicesMapper() {
        super(ServicesModel.class, ServicesDTO.class);
    }
}
