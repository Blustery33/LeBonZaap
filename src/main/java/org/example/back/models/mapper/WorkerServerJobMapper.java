package org.example.back.models.mapper;

import org.example.back.models.WorkerServerJobModel;
import org.example.back.models.dto.WorkerServerJobDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkerServerJobMapper extends GenericMapperImpl<WorkerServerJobModel, WorkerServerJobDTO> {
    public WorkerServerJobMapper() {
        super(WorkerServerJobModel.class, WorkerServerJobDTO.class);
    }
}
