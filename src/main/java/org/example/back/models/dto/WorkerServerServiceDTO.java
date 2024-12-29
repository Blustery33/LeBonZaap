package org.example.back.models.dto;

import lombok.Data;
import org.example.back.models.JobsModel;
import org.example.back.models.ServicesModel;
import org.example.back.models.WorkerServerModel;

@Data
public class WorkerServerServiceDTO {
    private Long id;
    private JobsDTO job;
    private ServicesDTO service;
}
