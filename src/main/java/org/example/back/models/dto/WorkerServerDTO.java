package org.example.back.models.dto;

import lombok.Data;
import org.example.back.models.ServersModel;
import org.example.back.models.WorkerServerJobModel;
import org.example.back.models.WorkerServerServiceModel;
import org.example.back.models.WorkersModel;

import java.util.List;

@Data
public class WorkerServerDTO {
    private Long id;
    private ServersDTO server;
    private List<OffersDTO> offers;
    private List<WorkerServerJobDTO> workerServerJobs;
    private List<WorkerServerServiceDTO> workerServerServices;
}
