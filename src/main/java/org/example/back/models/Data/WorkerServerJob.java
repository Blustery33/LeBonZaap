package org.example.back.models.Data;

import lombok.Data;

@Data
public class WorkerServerJob {
    private Long id;
    private Integer level;
    private Long jobId;
    private String JobName;
}
