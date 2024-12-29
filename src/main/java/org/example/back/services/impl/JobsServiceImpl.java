package org.example.back.services.impl;

import lombok.Data;
import org.example.back.models.dto.JobsDTO;
import org.example.back.models.mapper.JobsMapper;
import org.example.back.repository.JobsRepository;
import org.example.back.services.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class JobsServiceImpl implements JobsService {

    private final JobsRepository jobsRepository; // A repository that handles database operations for Jobs. It is marked as final to ensure it's injected via the constructor.

    @Autowired
    private JobsMapper jobsMapper; // A mapper to convert data between Jobs entities and Data Transfer Objects (DTOs).

    // Constructor-based dependency injection for the JobsRepository.
    public JobsServiceImpl(JobsRepository jobsRepository) {
        this.jobsRepository = jobsRepository; // Assigns the provided repository to the jobsRepository field.
    }

    // Implements the method defined in JobsService interface.
    @Override
    public List<JobsDTO> getAllJobs() {
        // Retrieves all Job entities from the database, maps them to DTOs, and collects them into a list.
        return jobsRepository.findAll()  // Finds all jobs in the repository (database).
                .stream()  // Converts the list of jobs into a stream to perform operations on each item.
                .map(jobsMapper::toDto)  // Maps each Job entity to its corresponding JobsDTO using the jobsMapper.
                .collect(Collectors.toList());  // Collects the mapped JobsDTO objects into a list and returns it.
    }
}
