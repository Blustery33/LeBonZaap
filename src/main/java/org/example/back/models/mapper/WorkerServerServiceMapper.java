package org.example.back.models.mapper;


import org.example.back.models.WorkerServerServiceModel;
import org.example.back.models.dto.WorkerServerServiceDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkerServerServiceMapper extends GenericMapperImpl<WorkerServerServiceModel, WorkerServerServiceDTO> {
    public WorkerServerServiceMapper() {
        super(WorkerServerServiceModel.class, WorkerServerServiceDTO.class);
    }
}
