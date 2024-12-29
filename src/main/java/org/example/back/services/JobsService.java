package org.example.back.services;

import org.example.back.models.dto.JobsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobsService {
    List<JobsDTO> getAllJobs();
}
