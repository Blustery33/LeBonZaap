package org.example.back.models.mapper;

import org.example.back.models.ServersModel;
import org.example.back.models.dto.ServersDTO;
import org.springframework.stereotype.Component;

@Component
public class ServersMapper extends GenericMapperImpl<ServersModel, ServersDTO> {
    public ServersMapper() {
        super(ServersModel.class, ServersDTO.class);
    }
}