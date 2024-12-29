package org.example.back.models.dto;

import lombok.Data;
import org.example.back.models.JobsModel;
import org.example.back.models.WorkerServerModel;

@Data
public class WorkerServerJobDTO {
    private Integer id;
    private JobsDTO job;
    private Integer level;
}
