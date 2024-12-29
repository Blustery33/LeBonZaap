package org.example.back.models.dto;

import lombok.Data;
import org.example.back.models.OffersModel;
import org.example.back.models.UserModel;
import org.example.back.models.WorkerServerModel;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkersDTO {
    private Long id;
    private UserDTO user;
    private List<WorkerServerDTO> workerServers;
}
