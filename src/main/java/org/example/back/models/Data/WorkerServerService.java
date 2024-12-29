package org.example.back.models.Data;

import lombok.Data;

@Data
public class WorkerServerService {
    private Long id;
    private Long serviceId;
    private String serviceName;
    private Long JobId;
    private String JobName;
}
