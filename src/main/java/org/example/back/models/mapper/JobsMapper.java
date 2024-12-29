package org.example.back.models.mapper;

import org.example.back.models.JobsModel;
import org.example.back.models.dto.JobsDTO;
import org.springframework.stereotype.Component;

@Component
public class JobsMapper extends GenericMapperImpl<JobsModel, JobsDTO> {
    public JobsMapper() {
        super(JobsModel.class, JobsDTO.class);
    }
}
