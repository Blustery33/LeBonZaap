package org.example.back.services.impl;

import lombok.Data;
import org.example.back.models.OffersModel;
import org.example.back.models.WorkersModel;
import org.example.back.models.dto.OffersDTO;
import org.example.back.models.dto.WorkersDTO;
import org.example.back.models.mapper.WorkersMapper;
import org.example.back.repository.WorkersRepository;
import org.example.back.services.WorkersService;
import org.example.back.services.exception.WorkerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class WorkersServiceImpl implements WorkersService {

    private final WorkersRepository workersRepository;

    @Autowired
    private WorkersMapper workersMapper; // A mapper to convert data between Workers entities and Data Transfer Objects (DTOs).

    public WorkersServiceImpl(WorkersRepository workersRepository) {
        this.workersRepository = workersRepository;
    }

    // Implements the method defined in WorkersService interface.
    @Override
    public List<WorkersDTO> getAllWorkers() {
        // Retrieves allServices entities from the database, maps them to DTOs, and collects them into a list.
        return workersRepository.findAll()  // Finds all workers in the repository (database).
                .stream()  // Converts the list of workers into a stream to perform operations on each item.
                .map(workersMapper::toDto)  // Maps each Workers entity to its corresponding WorkersDTO using the workersMapper.
                .collect(Collectors.toList());  // Collects the mapped WorkersDTO objects into a list and returns it.
    }

    // Method to retrieve a worker by the current user ID
    @Override
    public WorkersDTO getWorkerByUserId(Long userId) {
        WorkersModel worker =  workersRepository.findByUserId(userId)
                .orElseThrow(() -> new WorkerNotFoundException("L'artisan avec l'ID: " + userId + " n'existe pas."));
        return workersMapper.toDto(worker);
    }
}
