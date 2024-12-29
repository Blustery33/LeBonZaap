package org.example.back.models.mapper;

import org.example.back.models.WorkersModel;
import org.example.back.models.dto.WorkersDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkersMapper extends GenericMapperImpl<WorkersModel, WorkersDTO> {
    public WorkersMapper() {
        super(WorkersModel.class, WorkersDTO.class);
    }
}
