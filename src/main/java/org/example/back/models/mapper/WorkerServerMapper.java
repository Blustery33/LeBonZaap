package org.example.back.models.mapper;

import org.example.back.models.WorkerServerModel;
import org.example.back.models.dto.WorkerServerDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkerServerMapper extends GenericMapperImpl<WorkerServerModel, WorkerServerDTO> {
    public WorkerServerMapper() {
        super(WorkerServerModel.class, WorkerServerDTO.class);
    }
}
