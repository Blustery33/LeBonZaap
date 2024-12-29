package org.example.back.models.dto;

import lombok.Data;
import org.example.back.models.Data.ServerInfo;
import org.example.back.models.Data.WorkerServerJob;
import org.example.back.models.Data.WorkerServerService;

import java.time.LocalDateTime;

@Data
public class OffersDTO {
    private Long id;
    private Long workerServerWorkerId;
    private String titleOffer;
    private String description;
    private String price;
    private String pseudoInGame;
    private Boolean offerHidden;
    private LocalDateTime createdAt;
    private ServerInfo server;
    private WorkerServerJob workerServerJob;
    private WorkerServerService workerServerService;
}
