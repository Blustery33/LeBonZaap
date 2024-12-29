package org.example.back.services;

import org.example.back.models.dto.WorkersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkersService {
    List<WorkersDTO> getAllWorkers();

    WorkersDTO getWorkerByUserId(Long userId);
}
